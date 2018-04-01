import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    // this is the hash of out transaction
    public String transactionId;
    public PublicKey sender;
    public PublicKey reciepient;
    public float value;

    // this is some kind of protection of our wallet
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    // a count of how many transactions have been generated
    private static int sequence = 0;

    public Transaction(PublicKey sender, PublicKey reciepient, float value, ArrayList<TransactionInput> inputs) {
        this.sender = sender;
        this.reciepient = reciepient;
        this.value = value;
        this.inputs = inputs;
    }

    public boolean processTransaction() {
        if (!verifySignature()) {
            System.out.println("#Transaction signature is not verified");
            return false;
        }

        // gather transaction inputs (Make sure they are not unspent)
        for (TransactionInput i : inputs) {
            i.UTXO = DeniChain.UTXOs.get(i.transactionOutputId);
        }

        // check if transaction is valid
        if (getInputsValue() < DeniChain.minimumTransaction) {
            System.out.println("#Transaction inputs too small: " + getInputsValue());
            return false;
        }

        // generate transaction outputs
        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        // send value to recipient
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId));
        // send the left over 'change' to sender
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        // add outputs to unspent list
        for (TransactionOutput o : outputs) {
            DeniChain.UTXOs.put(o.id, o);
        }

        // remove transaction inputs from UTXO lists as spent
        for (TransactionInput i : inputs) {
            // if transaction can't be found
            if (i.UTXO == null) {
                continue;
            }
            DeniChain.UTXOs.remove(i.UTXO.id);
        }
        return true;
    }

    // returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            // if transaction can't be found
            if (i.UTXO == null) {
                continue;
            }
            total += i.UTXO.value;
        }
        return total;
    }

    // returns sum of outputs
    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence
        );
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        signature =  StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}

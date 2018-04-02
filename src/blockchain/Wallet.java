package blockchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    // used for making signed transaction. We should keep it on secret
    public PrivateKey privateKey;
    // out address
    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    // requires key pairing using Elliptic-curve cryptography
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // initialize out key generator and generate a keyPair
            keyPairGenerator.initialize(ecSpec, random);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    // returns the balance of the wallet
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : DeniChain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            // if ouput belongs to me
            if (UTXO.isMine(publicKey)) {
                // add it to our list of unspent transactions
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey reciepient, float value) {
        if (getBalance() < value) {
            System.out.println("#Not enough funds to sentf transaction: Balance is: " + getBalance() + ", value is: " + value);
            return null;
        }
        // create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value)
                break;
        }
        Transaction transaction = new Transaction(publicKey, reciepient, value, inputs);
        transaction.generateSignature(privateKey);

        for (TransactionInput i : inputs) {
            UTXOs.remove(i.transactionOutputId);
        }
        return transaction;
    }
}

package blockchain;

public class TransactionInput {
    // blockchain.TransactionOutput -> transactionId
    public String transactionOutputId;
    // contains the Unspent transaction output;
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}

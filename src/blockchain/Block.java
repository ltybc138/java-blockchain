package blockchain;

import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String merkleRoot;
    // our data will mbe a simple message
    public ArrayList<Transaction> transactions = new ArrayList<>();
    private long timeStamp;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculatedHash();
    }

    public String calculatedHash() {
        String calculatedHash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedHash;
    }

    public void mineBlock(int difficulty) {
        // string with difficulty * "0"
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculatedHash();
        }
        System.out.println("blockchain.Block mined!!! : " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        // process transaction and check if valid, unless block is genesis block - ignore
        if (transaction == null)
            return false;
        if (!previousHash.equals("0")) {
            if (!transaction.processTransaction()) {
                System.out.println("#blockchain.Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("blockchain.Transaction successfully added to block");
        return true;
    }
}

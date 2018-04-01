import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class DeniChain {
    public static ArrayList<Block> blocks = new ArrayList<>();
    public static int difficulty = 2;
    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        blocks.add(new Block("First block", "0"));
        System.out.println("Trying to mine block 1... ");
        blocks.get(0).mineBlock(difficulty);

        blocks.add(new Block("Second block", blocks.get(blocks.size() - 1).hash));
        System.out.println("Trying to mine block 2... ");
        blocks.get(1).mineBlock(difficulty);

        blocks.add(new Block("Third block", blocks.get(blocks.size() - 1).hash));
        System.out.println("Trying to mine block 3... ");
        blocks.get(2).mineBlock(difficulty);

        // printing all hashes of our blockchain
//        int c = 1;
//        for (Block block : blocks) {
//            System.out.println("Hash for block " + (c++) + " : " + block.hash);
//        }

        // generate json file from our blockchain
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blocks);
        System.out.println("Mining all blocks took : " + (System.currentTimeMillis() - timeStart) + " millis");
        System.out.println("\n\n");

        System.out.println(blockchainJson);
    }

    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // watch through all blocks
        for (int i = 1; i < blocks.size(); i++) {
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i-1);

            // compare registered hash and calculated hash
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.out.println("Current hashes are not equal");
                return false;
            }

            // compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous hashes are not equal");
                return false;
            }

            // check if hash is calculated
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}

package interfaces.impls;

import blockchain.Block;
import com.google.gson.GsonBuilder;
import interfaces.BlockchainData;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LocalDataStorage implements BlockchainData {
    @Override
    public void save(ArrayList<Block> blocks) {
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blocks);

        try {
            String fileName = calculateName() + "_data.json";
            System.out.println("\nSaving data into file...");

            PrintWriter writer = new PrintWriter("data/" + fileName);
            writer.append(blockchainJson);
            writer.close();

            System.out.println("Data successfully saved into file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create a random name
    private String calculateName() {
        float name = System.currentTimeMillis();
        return String.valueOf((long)name);
    }
}

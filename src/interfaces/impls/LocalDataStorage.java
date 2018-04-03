package interfaces.impls;

import blockchain.Block;
import com.google.gson.GsonBuilder;
import interfaces.BlockchainData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class LocalDataStorage implements BlockchainData {
    private String dir = "data/";

    @Override
    public void save(ArrayList<Block> blocks) {
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blocks);

        try {
            String fileName = calculateName() + "_data.json";
            System.out.println("\nSaving data into file...");

            PrintWriter writer = new PrintWriter(dir + fileName);
            writer.append(blockchainJson);
            writer.close();

            System.out.println("Data successfully saved into file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllData() {
        Path folder = Paths.get("/data/");

        Function<Path, Stream<Path>> walk = path -> {
            try {
                return Files.walk(path);
            } catch (IOException e) {
                return Stream.empty();
            }
        };

        Consumer<Path> delete = path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        try {
            Files.list(folder)
                    .flatMap(walk)
                    .sorted(Comparator.reverseOrder())
                    .forEach(delete);
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

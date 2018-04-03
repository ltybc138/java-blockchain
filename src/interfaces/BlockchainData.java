package interfaces;

import blockchain.Block;

import java.util.ArrayList;

public interface BlockchainData {

    // saves all blocks of the blockchain
    void save(ArrayList<Block> blocks);

    // delete everything
    void deleteAllData();
}

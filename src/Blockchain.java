import java.util.ArrayList;

public class Blockchain {
    ArrayList<Block> chain;
    private int difficulty = 0;

    void setDifficulty(int diff){
        this.difficulty = diff;
    }

    void add(){
        // Addition code for a new block
    }

    boolean isValid(){
        // Checks for the validity of the block-chain.
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Block-chain!");
    }
}


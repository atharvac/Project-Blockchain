import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

class Blockchain {
    ArrayList<Block> chain;
    ArrayList<Transaction> pendingTransactions;
    private int difficulty = 0;

    public Blockchain() throws NoSuchAlgorithmException {
        chain = new ArrayList<>();
        chain.add(generateFirstBlock());
    }

    private Block generateFirstBlock() throws NoSuchAlgorithmException {
        Block generate = new Block("null",pendingTransactions,"27/05/1999",difficulty);
        generate.setPrevHash(null);
        generate.calcHash();
        return generate;
    }
    void setDifficulty(int diff){
        this.difficulty = diff;
    }

    void add(Block bk) throws NoSuchAlgorithmException {
        // Addition code for a new block

        bk.setPrevHash(chain.get((int) (chain.size()-1.)).getCurrentHash());
        bk.calcHash();
        this.chain.add(bk);
    }

    boolean isValid(){
        // Checks for the validity of the block-chain.
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Block-chain!");
    }
}


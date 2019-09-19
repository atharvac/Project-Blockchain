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

    boolean isValid() throws NoSuchAlgorithmException {
        // Checks for the validity of the block-chain.
        for (int i = chain.size()-1; i>0; i--){
            if (chain.get(i).getCurrentHash().equals(chain.get(i).calcHash())){
                return false;
            }

            if ((!chain.get(i).getPrevHash().equals(chain.get(i-1).calcHash()))){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Block-chain!");
    }
}

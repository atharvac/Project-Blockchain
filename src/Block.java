import java.util.ArrayList;
import java.security.MessageDigest;

class Block {
    static int blockId;
    private String hash;
    private String prevHash;
    private ArrayList<Transaction> transactions;
    private String time;
    private int difficulty;
    int nonce;
    Block(String preH, ArrayList<Transaction> tr, String time, int difficulty){
        this.prevHash = preH;
        this.transactions = tr;
        this.time = time;
        this.difficulty = difficulty;
    }

    // Import or implement hash function SHA256 or any other.
    // Returns a Hex Hash value
    String calcHash(){
        return "";
    }

    public String getCurrentHash(){
        return hash;
    }
    public String getPrevHash(){
        return prevHash;
    }
    public ArrayList<Transaction> getTransactions(){
        return transactions;
    }
    public String getTime(){
        return time;
    }
}

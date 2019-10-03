import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


class Block implements Serializable {
    String blockId;
    private String hash;
    private String prevHash;
    private ArrayList<Transaction> transactions;
    private String time;
    private int difficulty;
    int nonce;
    Block(String preH, ArrayList<Transaction> tr, String time, int difficulty) throws NoSuchAlgorithmException {
        this.blockId = String.valueOf((int) (Math.random() * 100000));
        this.prevHash = preH;
        this.transactions = tr;
        this.time = time;
        this.difficulty = difficulty;
        this.hash = calcHash();
    }

    // Import or implement hash function SHA256 or any other.
    // Returns a Hex Hash value
    String calcHash() throws NoSuchAlgorithmException {

        String msg = prevHash + blockId + String.valueOf(time) + transactions.size() + nonce;

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] bytes = digest.digest(msg.getBytes());
        final StringBuilder hexString = new StringBuilder();
        for(final byte b :bytes){
            String hex = Integer.toHexString(0xff &b);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean mineBlock() throws NoSuchAlgorithmException {
        StringBuilder tar = new StringBuilder();
        for (int x=0; x < difficulty; x++){
            tar.append("0");
        }
        while(!hash.substring(0,difficulty).equals(tar.toString()) && !Blockchain.mineInterrupt){
            nonce++;
            hash = calcHash();
        }
        if (hash.substring(0,difficulty).equals(tar.toString())){return true;}
        else {return false;}

    }

    String getCurrentHash(){
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

    public String getBlockId(){
        return blockId;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        ArrayList<Transaction> a = new ArrayList<>();
        Block b = new Block("", a, "12-09-19", 5);
        System.out.println(b.mineBlock());
        System.out.println(b.getCurrentHash());
        System.out.println(b.getBlockId());
    }

}
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


class Block {
    static int blockId;
    private String hash;
    private String prevHash;
    private ArrayList<Transaction> transactions;
    private String time;
    private int difficulty;
    int nonce = 0000;
    Block(String preH, ArrayList<Transaction> tr, String time, int difficulty) throws NoSuchAlgorithmException {
        this.prevHash = preH;
        this.transactions = tr;
        this.time = time;
        this.difficulty = difficulty;
        this.hash = calcHash();
    }

    // Import or implement hash function SHA256 or any other.
    // Returns a Hex Hash value
    String calcHash() throws NoSuchAlgorithmException {

        String msg = blockId + String.valueOf(time) + transactions.size() + nonce;

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

    public void mineBlock(int difficulty) throws NoSuchAlgorithmException {
        String tar = new String(new char[difficulty]);
        while(!hash.substring(0,difficulty).equals(tar)){
            nonce++;
            hash = calcHash();
        }
        System.out.println("Block is Mined!! :" + hash);
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

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

}

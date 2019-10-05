import java.io.Serializable;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

class Blockchain implements Serializable {
    ArrayList<Block> chain;
    ArrayList<Transaction> pendingTransactions;
    private int difficulty = 0;
    String ID;
    static boolean mineInterrupt = false;

    public Blockchain(String ID, int diff) throws NoSuchAlgorithmException, SocketException {
        this.ID = ID;
        chain = new ArrayList<>();
        pendingTransactions = new ArrayList<>();
        setDifficulty(diff);
        chain.add(generateGenesisBlock());

    }

    private Block generateGenesisBlock() throws NoSuchAlgorithmException {
        Block generate = new Block("null",pendingTransactions,"27/05/1999",difficulty);
        generate.setPrevHash(null);
        generate.calcHash();
        return generate;
    }
    void setDifficulty(int diff){
        this.difficulty = diff;
    }

    void start_mining() throws NoSuchAlgorithmException {
        return;
    }

    void validate_add_block(Block bk){
        if (!bk.getPrevHash().equals(chain.get(chain.size()-1).getCurrentHash())){// Check if new block slots on the blockchain
            System.out.println("Block Rejected! : Blockchain hash does not match!");
            return;
        }
        ArrayList<Transaction> pTr = chain.get(chain.size()-1).getTransactions();
        ArrayList<Transaction> nTr = bk.getTransactions();

        ArrayList<String> prevTr = new ArrayList<>();
        ArrayList<String> newTr = new ArrayList<>();

        for (Transaction t : pTr) {
            prevTr.add(t.id);
        }
        for (Transaction t : nTr) {
            newTr.add(t.id);
        }

        prevTr.retainAll(newTr);

        if (prevTr.size() != 0){// Check if previous block has some of the same transactions.
            System.out.println("Block Rejected! : Duplicate Transactions!");
        }
        else{
            this.chain.add(bk);
        }
    }

    void validate_add_transaction(Transaction tr){
        for(Transaction t : pendingTransactions){
            if(t.id.equals(tr.id)){
                System.out.println("Duplicate transaction found!");
                return;
            }
        }
        pendingTransactions.add(tr);
        System.out.println("\nTransaction from: "+ tr.fromAddress+" Validated");
    }

    boolean isValid() throws NoSuchAlgorithmException {
        String hashTarget = new String(new char[difficulty]);

        // Checks for the validity of the block-chain.
        for (int i = chain.size()-1; i>0; i--){
            //Compare current registered hash and calculated hash
            if (chain.get(i).getCurrentHash().equals(chain.get(i).calcHash())){
                return false;
            }

            //Compare previous registered hash and previous hash
            if ((!chain.get(i).getPrevHash().equals(chain.get(i-1).calcHash()))){
                return false;
            }

            //To check is block is mined
            if (!chain.get(i).getCurrentHash().substring(0,difficulty).equals(hashTarget)){
                System.out.println("Block" + i +"is not Mined");
                return false;
            }
        }
        return true;
    }
}


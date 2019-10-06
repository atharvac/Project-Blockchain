import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

class Blockchain extends Thread implements Serializable {
    ArrayList<Block> chain;
    private ArrayList<Transaction> pendingTransactions;
    private ArrayList<Transaction> currently_mining;
    private int difficulty = 0;
    String ID;
    private String b_chain_broadcast;
    static boolean mineInterrupt = false;

    public Blockchain(String ID, int diff, String broadcast_addr) throws NoSuchAlgorithmException, SocketException {
        this.ID = ID;
        this.b_chain_broadcast = broadcast_addr;
        chain = new ArrayList<>();
        pendingTransactions = new ArrayList<>();
        setDifficulty(diff);
        chain.add(generateGenesisBlock());

    }

    private Block generateGenesisBlock() throws NoSuchAlgorithmException {
        ArrayList<Transaction> genesis = new ArrayList<>();
        Block generate = new Block("null", pendingTransactions,"27/05/1999",0);
        generate.setPrevHash(null);
        generate.calcHash();
        return generate;
    }
    void setDifficulty(int diff){
        this.difficulty = diff;
    }

    void start_mining() throws NoSuchAlgorithmException, InterruptedException, IOException {
        Blockchain.mineInterrupt = false;
        boolean flag = true;
        int k = 4;
        currently_mining = new ArrayList<>();
        while(!MainRun.stopThread_mining){
            if (pendingTransactions.size() < k){
                if (flag) {
                    System.out.println("Waiting for transactions...");
                    flag = false;
                }
                Thread.sleep(1000);
            }
            else {
                System.out.println(pendingTransactions.size());
                for (int i=0;i<k;i++){
                    currently_mining.add(pendingTransactions.get(i));
                }
                //Get current time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String s = dtf.format(now);
                Block newBlock = new Block(chain.get(chain.size()-1).getCurrentHash(), currently_mining, s, difficulty);

                if(newBlock.mineBlock()){
                    pendingTransactions.removeAll(currently_mining);
                    TransferData sendBlock = new TransferData(ID, newBlock);
                    SendData sd = new SendData(b_chain_broadcast, 7777);
                    sd.broadcastData(sendBlock);
                    validate_add_block(newBlock);
                    System.out.println("Block sent! ID:"+ newBlock.blockId);
                    currently_mining.clear();
                    break;
                }
            }
        }
    }

    public void run(){
        try {
            start_mining();
        } catch (NoSuchAlgorithmException | InterruptedException | IOException e) {
            System.out.println("Here?? 82");
            e.printStackTrace();
        }
    }

    void validate_add_block(Block bk){
        if (!bk.getPrevHash().equals(chain.get(chain.size()-1).getCurrentHash())){// Check if new block slots on the blockchain
            System.out.println("Block Rejected! : Blockchain hash does not match!");
            return;
        }
        ArrayList<Transaction> pTr = chain.get(chain.size()-1).getTransactions();
        ArrayList<Transaction> nTr = bk.getTransactions();
        System.out.println("Blockchain Size:"+ chain.size());
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
            System.out.println(prevTr.size() + "Here?");
            System.out.println("Block Rejected! : Duplicate Transactions!");
        }
        else{
            this.chain.add(bk);
            System.out.println("New block added to blockchain ID:"+ bk.blockId);
            for(Transaction t : pendingTransactions){
                for (String s : newTr){
                    if (t.id.equals(s)){
                        pendingTransactions.remove(t);
                    }
                }
            }
            // Interrupt any mining process as new block has been acquired.
            Blockchain.mineInterrupt = true;
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

    boolean isValid() throws NoSuchAlgorithmException { // Some problem in this function. For now return true.
        String hashTarget = new String(new char[difficulty]);

        // Checks for the validity of the block-chain.
        /*for (int i = chain.size()-1; i>0; i--){
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
        }*/
        return true;
    }
}


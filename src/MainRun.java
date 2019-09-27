import com.sun.tools.javac.Main;

import java.io.*;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainRun {
    private int DIFFICULTY = 2;
    public Blockchain b_chain;
    ReceiveData server;
    SendData sd;
    private void setup() throws SocketException {
        File file = new File("Ledger.txt");
        if (file.exists()){
            System.out.println("Ledger Found, Using That.\n");
            try {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fi);

                b_chain = (Blockchain) oi.readObject();
                oi.close();
                fi.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            String ID = String.valueOf((int) (Math.random() * 100000));
            try {
                b_chain = new Blockchain(ID, DIFFICULTY);
            } catch (NoSuchAlgorithmException | SocketException e) {
                e.printStackTrace();
            }
        }
        server = new ReceiveData(7777, b_chain);
        Thread t1 = new Thread(server);
        t1.start();

    }

    void exit() throws IOException {
        sd = new SendData("localhost", 7777);
        sd.endBroadcast();
        server.stopRunning();
        try{
            FileOutputStream f = new FileOutputStream(new File("Ledger.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(b_chain);
            o.close();
            f.close();
            System.out.println("Good-Bye!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        //
        //TEST CODE
        //
        ArrayList<Transaction> test2= new ArrayList<>();
        Block test = new Block(null, test2, "27/09/2019", 2);
        MainRun m = new MainRun();
        m.setup();
        TransferData td = new TransferData(m.b_chain.ID, test);
        System.out.println("\nClient:");
        SendData sd = new SendData("localhost", 7777);
        sd.broadcastData(td);
        System.out.println("Size is:"+m.b_chain.chain.size());
        m.exit();
    }
}

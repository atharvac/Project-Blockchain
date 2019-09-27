import com.sun.tools.javac.Main;

import java.io.*;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class MainRun {
    public Blockchain b_chain;

    private void setup() {
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
                b_chain = new Blockchain(ID);
            } catch (NoSuchAlgorithmException | SocketException e) {
                e.printStackTrace();
            }
        }
    }

    void exit(){
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

        MainRun m = new MainRun();
        m.setup();
        ReceiveData server = new ReceiveData(7777, m.b_chain);
        TransferData td = new TransferData("Block1", m.b_chain.ID, "Test");
        Thread t1 = new Thread(server);
        t1.start();
        System.out.println("\nClient:");
        SendData sd = new SendData("localhost", 7777);
        sd.broadcastData(td);
        m.exit();
    }
}

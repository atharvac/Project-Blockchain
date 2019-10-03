import com.sun.tools.javac.Main;

import java.io.*;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainRun {
    static String BROADCAST_ADDRESS = "localhost"; //Set the current broadcast address
    private int DIFFICULTY = 2;
    public Blockchain b_chain;
    ReceiveData server;
    SendData sd;
    void setup() throws SocketException {
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
        try {
            sd.endBroadcast();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    String makeTransaction() throws IOException {
        Transaction tr = new Transaction();
        TransferData td = new TransferData(b_chain.ID, tr);
        SendData sd = new SendData(MainRun.BROADCAST_ADDRESS, 7777);
        sd.broadcastData(td);
        return b_chain.ID;// Here goes Transaction Information.
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        boolean run = true;
        Scanner sc = new Scanner(System.in);
        MainRun m = new MainRun();
        m.setup();
        while(run){
            System.out.print("\n1)Create a transaction\n2)Start Mining\n3)Exit\n:");
            switch (sc.nextLine()){
                case "1":
                    m.makeTransaction();
                    break;
                case "2":

                    break;
                default:
                    m.exit();
                    run = false;
                    break;
            }
        }
        /*
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
        */

    }
}

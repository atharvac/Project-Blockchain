import java.io.*;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MainRun {
    private static String BROADCAST_ADDRESS = "localhost"; //Set the current broadcast address
    private int DIFFICULTY = 6;
    private Blockchain b_chain;
    private ReceiveData server;
    private SendData sd;
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
                b_chain = new Blockchain(ID, DIFFICULTY, BROADCAST_ADDRESS);
            } catch (NoSuchAlgorithmException | SocketException e) {
                e.printStackTrace();
            }
        }
        server = new ReceiveData(7777, b_chain);
        Thread t1 = new Thread(server);
        t1.start();

    }

    private void exit() throws IOException {
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

    String makeTransaction() throws IOException { // Only for console TAG{CONSOLE}
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Blockchain ID of recipient:");
        String SendTO = sc.nextLine();
        System.out.print("\n1) Medical Object\n2) Medical History\n3) Funds\n->");
        String switch_str = sc.nextLine();
        Transaction tr = new Transaction(String.valueOf((float) (Math.random() * 1000000)), b_chain.ID, SendTO);
        // Transaction tr = new Transaction("1", b_chain.ID);
        switch(switch_str){
            case "1":
                break;
            case "2":
                break;
            case "3":
                System.out.print("Enter amount:");
                tr.setAmount(sc.nextFloat());
                break;
            default:
                return "Cancelled";
        }

        TransferData td = new TransferData(b_chain.ID, tr);
        SendData sd = new SendData(MainRun.BROADCAST_ADDRESS, 7777);
        sd.broadcastData(td);
        return "Transaction from "+b_chain.ID + " to "+ SendTO + "Type -" + tr.Header;// Here goes Transaction Information.
    }

    public static void main(String[] args) throws IOException { //{CONSOLE}
        boolean run = true;
        Scanner sc = new Scanner(System.in);
        MainRun m = new MainRun();
        m.setup();
        while(run){
            System.out.print("\n1)Create a transaction\n2)Start Mining\n3)Exit\n->");
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

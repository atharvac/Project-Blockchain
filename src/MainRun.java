import com.sun.tools.javac.Main;

import java.io.*;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

public class MainRun {
    private static String BROADCAST_ADDRESS = "localhost"; //Set the current broadcast address
    public static boolean stopThread_mining = false;
    public static boolean mining_flag = false;
    private int DIFFICULTY = 5;
    private Blockchain b_chain;
    private ReceiveData server;
    private SendData sd;

    private void createNewBChain(){
        String ID = String.valueOf((int) (Math.random() * 100000));
        try {
            b_chain = new Blockchain(ID, DIFFICULTY, BROADCAST_ADDRESS);
        } catch (NoSuchAlgorithmException | SocketException e) {
            e.printStackTrace();
        }
    }

    void setup() throws SocketException, NoSuchAlgorithmException {
        File file = new File("Ledger.txt");
        if (file.exists()){
            System.out.println("Ledger Found, Using That.\n");
            try {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fi);

                b_chain = (Blockchain) oi.readObject();
                System.out.println("Chain size:" + b_chain.chain.size());
                oi.close();
                fi.close();
                if(!b_chain.isValid()){
                    for (Block b : b_chain.chain){
                        System.out.println(b.getPrevHash() + "->" + b.getCurrentHash());
                    }
                    System.out.println("Invalid Blockchain!, creating a new one.");
                    b_chain = null;
                    createNewBChain();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            createNewBChain();
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
            System.out.println("Good-Bye!"+ "Chain size = " + b_chain.chain.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    MedicalHistory create_medical_history(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the name:");
        String _name = sc.nextLine();
        System.out.print("Enter Address:");
        String _addr = sc.nextLine();
        System.out.print("Enter Blood Group:");
        String _bg = sc.nextLine();
        System.out.print("Enter Age:");
        int _age = sc.nextInt();
        MedicalHistory mh = new MedicalHistory(_name, _addr, _bg, _age);

        // Extract data from string and put into array-list.
        System.out.println("Enter diseases (Separated by ','):");
        String _diseases = sc.nextLine();
        System.out.println("Enter allergies (Separated by ',')");
        String _allergies = sc.nextLine();
        return mh;
    }

    private MedicalObject create_medical_object(){
        Scanner sc = new Scanner(System.in);
        String _type, _name;
        int _quantity;
        float _amount;
        System.out.print("Enter type of object (Organ/Drug):");
        _type = sc.nextLine();
        System.out.print("Enter name of the object:");
        _name = sc.nextLine();
        System.out.print("Enter the quantity:");
        _quantity = sc.nextInt();
        System.out.print("Enter the funds to be transferred:");
        _amount = sc.nextFloat();
        System.out.println("Enter blood type:");
        String _bg = sc.nextLine();
        MedicalObject mobj = new MedicalObject(_type, _name, _quantity, _amount);
        try {
            mobj.setBloodType(_bg);
            mobj.store();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mobj;
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
                tr.setObject(create_medical_object());
                break;
            case "2":
                tr.setHistory(create_medical_history());
                break;
            case "3":
                System.out.print("Enter amount:");
                tr.setAmount(sc.nextFloat());
                break;
            default:
                return "Cancelled";
        }
        b_chain.validate_add_transaction(tr);
        TransferData td = new TransferData(b_chain.ID, tr);
        SendData sd = new SendData(MainRun.BROADCAST_ADDRESS, 7777);
        sd.broadcastData(td);
        return "Transaction from "+b_chain.ID + " to "+ SendTO + "Type -" + tr.Header;// Here goes Transaction Information.
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException { //{CONSOLE}
        boolean run = true;
        Scanner sc = new Scanner(System.in);
        MainRun m = new MainRun();
        m.setup();

        while(run){
            if(MainRun.mining_flag){
                Thread miningThread = new Thread(m.b_chain);
                MainRun.mining_flag = false;
                miningThread.start();

            }
            System.out.print("\n1)Create a transaction\n2)Start Mining\n3)Stop Mining\n4)Exit\n->");
            switch (sc.nextLine()){
                case "1":
                    m.makeTransaction();
                    break;
                case "2":
                    Thread miningThread = new Thread(m.b_chain);
                    MainRun.stopThread_mining = false;
                    miningThread.start();
                    break;
                case "3":
                    MainRun.stopThread_mining = true;
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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MainRun {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        //
        //TEST CODE
        //
        String ID = String.valueOf((int) (Math.random() * 100000));
        Blockchain b_chain = new Blockchain(ID);
        ReceiveData server = new ReceiveData(7777, b_chain);
        TransferData td = new TransferData("Block1", b_chain.ID, "Test");
        Thread t1 = new Thread(server);
        t1.start();
        System.out.println("\nClient:");
        SendData sd = new SendData("localhost", 7777);
        sd.broadcastData(td);
    }
}

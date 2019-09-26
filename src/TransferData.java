import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;

//Create a transfer-data object then pass it to SendData to broadcast it.
//This will also receive all the Traffic from ReceiveData class.
class TransferData implements Serializable {
    //Header determines how the broadcast is handled at the receiver's end
    private String header;
    private String senderID;
    private String type;
    private Block b;
    private Transaction t;
    TransferData(String head, String ID, Block b){
        header = head;
        senderID = ID;
        this.b = b;
    }
    TransferData(String head, String ID, Transaction t){
        header = head;
        senderID = ID;
        this.t = t;
    }
    TransferData(String head, String ID, String type){
        header = head;
        senderID = ID;
        this.type = type;
    }
    String getHeader(){
        return this.header;
    }
    String getSenderID(){
        return this.senderID;
    }
    String getType(){
        return this.type;
    }
    Block getBlock(){
        return this.b;
    }
    Transaction getTransaction(){
        return this.t;
    }
}

//For all broadcast purposes (Block, Transaction, Request)
class SendData {

    private DatagramSocket socketBroadcast;//Broadcast socket
    private InetAddress address;
    private int port;
    SendData(String addr, int port){
        try {
            socketBroadcast = new DatagramSocket();
            socketBroadcast.setBroadcast(true);
            address = InetAddress.getByName(addr);
            this.port = port;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connect all clients");
    }

    //Method to broadcast the TransferData object.
    void broadcastData(TransferData t) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(t);
        oos.flush();
        byte [] data = baos.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socketBroadcast.send(packet);
        socketBroadcast.close();
    }

}

class ReceiveData {
    String header;
    private InetAddress address;
    private int port;

    ReceiveData(int port){
        this.port = port;
        System.out.println("Connect all clients");
    }
}

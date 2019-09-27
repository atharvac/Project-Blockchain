import java.io.*;
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
        System.out.println("Broadcasting to:"+ addr);
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

class ReceiveData extends Thread {
    String header;
    private InetAddress address;
    private int port;
    private boolean running;
    DatagramSocket socket;
    byte[] buf = new byte[1024];

    ReceiveData(int port) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
        System.out.println("Server is listening on port:" + port);
    }

    public void run() {
        running = true;
        String received = "";
        boolean flag = false;
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            if(flag){
                received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);


                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                packet = new DatagramPacket(buf, buf.length, address, port);
                flag = false;
            }
            else{
                ByteArrayInputStream bis = new ByteArrayInputStream(buf);
                try {
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    TransferData e1  = (TransferData) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Exception" + e);
                }
                flag = true;
            }
        }
        socket.close();
    }
}

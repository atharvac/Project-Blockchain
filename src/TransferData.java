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
    TransferData(String ID, Block b){// Constructor for block broadcast.
        header = "Block";
        senderID = ID;
        this.b = b;
    }
    TransferData(String ID, Transaction t){// Constructor for Transaction broadcast.
        header = "Transaction";
        senderID = ID;
        this.t = t;
    }
    TransferData(String head, String ID, String type){// Constructor for any other broadcast.
        header = head;
        senderID = ID;
        this.type = type;
    }
    // Getter methods
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

    private DatagramSocket socketBroadcast;// Broadcast socket
    private InetAddress address;// Set the subnet.
    private int port;// Set the port to which to broadcast.
    SendData(String addr, int port){
        try {
            socketBroadcast = new DatagramSocket();
            socketBroadcast.setBroadcast(true);// Set broadcast to true
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
        oos.writeObject(t);// Write object to byteStream
        oos.flush();
        byte [] data = baos.toByteArray();// Serialize

        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);// Create a UDP packet
        socketBroadcast.send(packet);// Send the packet
    }

     void endBroadcast() throws IOException{
        byte [] buf;
        buf = "end".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socketBroadcast.send(packet);
        socketBroadcast.close();
    }

}


class ReceiveData extends Thread {
    private InetAddress address;
    private int port;
    private boolean running;
    DatagramSocket socket;
    Blockchain b_chain;
    byte[] buf = new byte[1024];

    ReceiveData(int port, Blockchain chain) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);// Set port on socket
        this.b_chain = chain;
        System.out.println("Server is listening on port:" + port);

    }


    // Check headers from transmissions and perform certain actions.
    void checkHeaders(TransferData t) {
        switch(t.getHeader()){
            case "Block":
                b_chain.chain.add(t.getBlock());
                System.out.println("Block added!");
                break;
            case "Transaction":
                b_chain.pendingTransactions.add(t.getTransaction());
                System.out.println("Transaction Added");
                break;
            case "ChainLength":
                if (Integer.parseInt(t.getType()) > b_chain.chain.size()){
                    break;
                }
                break;
            default:
                break;
        }
    }

    public void run() {// Thread start() method calls this method.
        running = true;
        String received = "";
        boolean flag = false;
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);// Create a UDP packet (empty).
            try {
                socket.receive(packet);// Receive the packet.
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();// Get the broadcaster.
            int port = packet.getPort();
            /*if(flag){
                received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received " + received + " From " + address.toString() + " On port:"+port);
                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                //packet = new DatagramPacket(buf, buf.length, address, port);
                flag = false;
            }*/
            // CODE TO ACCEPT AND DESERIALIZE OBJECT (TransferData).
            ByteArrayInputStream bis = new ByteArrayInputStream(buf);// Get input stream from byte array.
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);// Create an object from input-stream.
                TransferData e1  = (TransferData) ois.readObject();// Create the specific object.
                checkHeaders(e1);
                //TEST CODE
                System.out.println(e1.getHeader() + "  " + e1.getSenderID());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Exception" + e);
            }
            //flag = true;
        }
        socket.close();
    }

    public void stopRunning(){
        running = false;
    }
}

import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

class TransferData {
    ConcurrentHashMap<String, Socket> activeClients = new ConcurrentHashMap<>();
    TransferData(){
        System.out.println("Connect all clients");
    }

}

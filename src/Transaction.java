import java.io.Serializable;

class Transaction implements Serializable {
    String Header;
    String fromAddress; // Both of these addresses will be blockchain ID's.
    String toAddress;
    String digital_signature;
    String id;
    float amount;
    MedicalObject object;
    MedicalHistory history;

    Transaction(String id, String myAddr){
        this.id = id;
        this.fromAddress = myAddr;
    }

    Transaction(Transaction t){
        this.id = t.id;
        this.fromAddress = t.fromAddress;
        this.toAddress = t.toAddress;
        this.digital_signature = t.digital_signature;
        this.object = t.object;
        this.history = t.history;
    }

    void setToAddress(String to_addr){
        this.toAddress = to_addr;
    }

    void setAmount(float amt){
        this.amount = amt;
    }

    void setObject(MedicalObject obj){
        Header = "Object";
        this.object = obj;
    }

    void setHistory(MedicalHistory his){
        Header = "History";
        this.history = his;
    }
}

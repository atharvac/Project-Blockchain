import java.io.Serializable;
import java.sql.*;

class Transaction implements Serializable {
    int fromAddress;
    int toAddress;
    String digital_signature;
    int id;
    float amount;
    MedicalObject object = new MedicalObject();
}

import java.io.Serializable;
import java.sql.*;

class Transaction implements Serializable {
    String fromAddress; // Both of these addresses will be blockchain ID's.
    String toAddress;
    String digital_signature;
    int id;
    float amount;
    MedicalObject object;
    MedicalHistory history;
}

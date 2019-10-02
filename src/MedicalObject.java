import java.io.Serializable;
import java.util.ArrayList;

class MedicalObject implements Serializable {
    String TYPE;
    String NAME;
    int QUANTITY;
    float AMOUNT;
    // Basic Attributes of a medical object
    // Maybe this will be a base class for other specific classes, like Drugs, syringes etc.
    // This may be populated by a database.
}
class MedicalHistory implements Serializable {
    String NAME;
    String ADDRESS;
    String BLOODGROUP;
    int AGE;
    ArrayList<String> DIEASES;
    ArrayList<String> ALLERGIES;

}

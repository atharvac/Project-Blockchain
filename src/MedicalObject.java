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
    MedicalObject(String type,String name,int quant,float amt){
        this.TYPE = type;
        this.NAME = name;
        this.QUANTITY = quant;
        this.AMOUNT = amt;
    }
}
class MedicalHistory implements Serializable {
    String NAME;
    String ADDRESS;
    String BLOODGROUP;
    int AGE;
    ArrayList<String> DISEASES;
    ArrayList<String> ALLERGIES;
    MedicalHistory(String name,String address,String bg,int age){
        this.NAME = name;
        this.ADDRESS = address;
        this.BLOODGROUP = bg;
        this.AGE = age;
    }
    void setDISEASES(ArrayList<String> d){
        DISEASES = d;
    }
    void setALLERGIES(ArrayList<String>a){
        ALLERGIES = a;
    }
}
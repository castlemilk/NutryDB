package nutritiondb.ben.db2.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by benebsworth on 13/06/16.
 */
public class Portion implements Serializable{


    private String unit;
    private String g;
    private String amt;

//    // Required default constructor for Firebase object mapping
//    @SuppressWarnings("unused")
    private Portion() {
    }

    public Portion(String unit, String g, String amt) {
        this.unit = unit;
        this.g = g;
        this.amt =amt;
    }


    public HashMap<String, String> getPortion() {
        HashMap<String,String> portion = new HashMap<String, String>();

        portion.put("unit", unit);
        portion.put("g", g);
        portion.put("amt", amt);

        return portion;
    }

    public String getUnit() {
        return unit;
    }
    public String getG() {
        return g;
    }
    public String getAmt() {
        return amt;
    }

    public String toString() {
        if (g != null) {
            return unit+" ["+g+"g] ";
        }
        else {
            return unit;
        }

    }


}
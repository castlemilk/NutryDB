package nutritiondb.ben.db2.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by benebsworth on 13/06/16.
 */
public class Portion implements Serializable{


    private String unit;
    private Double g;
    private Double amt;

//    // Required default constructor for Firebase object mapping
//    @SuppressWarnings("unused")
    private Portion() {
    }

    public Portion(String unit, Double g, Double amt) {
        this.unit = unit;
        this.g = g;
        this.amt = amt;
    }
    public Portion(String unit, String g, String amt) {
        this.unit = unit;
        this.g = Double.valueOf(g);
        this.amt = Double.valueOf(amt);
    }


    public HashMap<String, String> getPortion() {
        HashMap<String, String> portion = new HashMap<String, String>();

        portion.put("unit", unit);
        portion.put("g", Double.toString(g));
        portion.put("amt", Double.toString(amt));

        return portion;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Double getG() {
        return g;
    }
    public void setG(Double g) {
        this.g = g;
    }
    public Double getAmt() {
        return amt;
    }
    public void setAmt(Double amt) {
        this.amt = amt;
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
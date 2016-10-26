package nutritiondb.ben.db2.models;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by benebsworth on 13/06/16.
 */
public class Nutrient implements Serializable{

    private String abbrv;
    private String name;
    private String units;
    private String value;
    DecimalFormat df = new DecimalFormat("#.##");

    //    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Nutrient() {
    }
    @SuppressWarnings("unused")
    public Nutrient(String name, String units, String value) {
//        this.abbrv = abbrv;
        this.name = name;
        this.units = units;
        this.value = value;
    }

    public HashMap<String, String> getNutrient() {
        HashMap<String,String> nutrient = new HashMap<String, String>();

        nutrient.put("name", name);
        nutrient.put("units", units);
        nutrient.put("value", value);
//        nutrient.put("abbrv", abbrv);

        return nutrient;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Double getValueD() {
        return Double.valueOf(value);
    }
    public String getUnit() {
        return units;
    }
    public void setUnit(String units) {
        this.units = units;
    }
    public String getValueScaled(Double scaling_factor) {
        return df.format(Double.valueOf(value)*(scaling_factor/100));
    }
    public Nutrient getNutrientScaled(Double scaling_factor) {
        return new Nutrient(name, units, getValueScaled(scaling_factor));

    }
    public Nutrient getNutrient(String abbrev) {

    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
//        dest.writeString(units);
//        dest.writeString(value);
//
//    }
}

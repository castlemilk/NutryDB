package nutritiondb.ben.db2.models;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by benebsworth on 13/06/16.
 */
public class Nutrient implements Serializable{
    /**
     * POJO used for the de-serialisation of firebase requests which contain a nested dictionary
     * of nutrient values with parent abbreviations
     * This method aims to have the extensability of returning dynamic nutrient values as
     * different portions are selected in the food profile card.
     */

    private String name;
    private String units;
    private String value;
    DecimalFormat df = new DecimalFormat("#.##");
    private Nutrient() {
    }
    public Nutrient(String name, String units, String value) {
        this.name = name;
        this.units = units;
        this.value = value;
    }

    public HashMap<String, String> getNutrient() {
        HashMap<String,String> nutrient = new HashMap<String, String>();

        nutrient.put("name", name);
        nutrient.put("units", units);
        nutrient.put("value", value);
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
    public void setValue(Double value) {
        this.value = Double.toString(value);
    }
    public Double getValueD() {
        if (value.equals("~")){
            return 0d;
        }
        else {
            return Double.valueOf(value);
        }
    }
    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }
    public String getValueScaled(Double scaling_factor) {
        return df.format(Double.valueOf(value)*(scaling_factor/100));
    }
    public Nutrient getNutrientScaled(Double scaling_factor) {
        return new Nutrient(name, units, getValueScaled(scaling_factor));

    }

}

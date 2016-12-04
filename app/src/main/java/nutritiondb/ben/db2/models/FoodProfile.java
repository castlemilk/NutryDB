package nutritiondb.ben.db2.models;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benebsworth on 21/06/16.
 */
public class FoodProfile extends BaseFoodProfile implements Serializable{
    /**
     * This is a POJO responsible for the de-serialisation of a food profile retrieved from
     * firebase. It has a nesting of other POJO's such as nutrient, portions and the potential of
     * other nesting opportunities around additional meta data or feature sets to be offered in
     * the foodprofile view.
     */
    DecimalFormat df = new DecimalFormat("#.##");
    private HashMap<String, Nutrient> nutrients;
    private HashMap<String, Portion> portions;

    public FoodProfile() {
        //empty contructor
    }
    public FoodProfile(String NDB_no, String name, String source, String group) {
        this.source = source;
        this.NDB_no = NDB_no;
        this.name = name;
        this.group = group;
        this.nutrients = new HashMap<>();
        this.portions = new HashMap<>();
        this.createdAt = System.currentTimeMillis();

    }

    public String getNDBno() {
        return NDB_no;
    }
    public String getSource() {
        return source;
    }

    public HashMap<String, Nutrient> getNutrients() {
        return nutrients;
    }
    public HashMap<String, Nutrient> getNutrients(Portion portion) {
        if (portion != null) {
            HashMap<String, Nutrient> result = new HashMap<>();
            System.out.println("package: "+portion.getPortion().toString());
            System.out.println("g="+portion.getG());
            double scaling_factor = Double.valueOf(portion.getG());
            for (Map.Entry<String, Nutrient> nutrient : nutrients.entrySet()) {
                result.put(nutrient.getKey(),nutrient.getValue().getNutrientScaled(scaling_factor));

            }
            return result;
        }
        else {
            return nutrients;
        }
    }
    public FoodProfile getScaledProfile(Portion portion) {
        FoodProfile scaledProfile = new FoodProfile();
        scaledProfile.nutrients = this.getNutrients();
        try {
            scaledProfile = (FoodProfile) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (portion != null) {
            HashMap<String, Nutrient> result = new HashMap<>();
            System.out.println("package: "+portion.getPortion().toString());
            System.out.println("g="+portion.getG());
            double scaling_factor = Double.valueOf(portion.getG());
            for (Map.Entry<String, Nutrient> nutrient : scaledProfile.nutrients.entrySet()) {
                result.put(nutrient.getKey(),nutrient.getValue().getNutrientScaled(scaling_factor));

            }
            scaledProfile.nutrients = result;
            return scaledProfile;
        }
        else {
            return this;
        }

    }

    public HashMap<String, Portion> getPortions() {
        return portions;
    }

    public Nutrient getNutrient(String abbrev) {
        if (nutrients.containsKey(abbrev)) {
            return new Nutrient(AbbreviationMapping.getName(abbrev),
                    nutrients.get(abbrev).getUnits(),
                    nutrients.get(abbrev).getValue());
        }
        else {
            return new Nutrient(
                    AbbreviationMapping.getName(abbrev),
                    AbbreviationMapping.getDefaultUnits(abbrev),
                    "~");
        }

    }
    public Nutrient getEnergyKJ() {
        /*
        * Usually the default energy unit available in a food profile, will need to convert to KCal
        * etc.
         */
        String USDA_Energy_key = "ENERC";
        String NUTTAB_Energy_key = "ENERC1";
        if (nutrients.containsKey(USDA_Energy_key)) {
            return new Nutrient(AbbreviationMapping.getName(USDA_Energy_key),
                    nutrients.get(USDA_Energy_key).getUnits(),
                    nutrients.get(USDA_Energy_key).getValue());
        }
        else if (nutrients.containsKey(NUTTAB_Energy_key)) {
            return new Nutrient(AbbreviationMapping.getName(NUTTAB_Energy_key),
                    nutrients.get(NUTTAB_Energy_key).getUnits(),
                    nutrients.get(NUTTAB_Energy_key).getValue());
        }
        else {
            return new Nutrient("Energy",
                    AbbreviationMapping.getDefaultUnits(USDA_Energy_key),
                    "~");
        }
    }

    public Nutrient getEnergyKCal() {
        String USDA_Energy_key = "ENERC_KCAL";
        String NUTTAB_Energy_key_1 = "ENERC1_KCAL";
        if (nutrients.containsKey(USDA_Energy_key)) {
            return new Nutrient(AbbreviationMapping.getName(USDA_Energy_key),
                    nutrients.get(USDA_Energy_key).getUnits(),
                    nutrients.get(USDA_Energy_key).getValue());
        }
        else if (nutrients.containsKey(NUTTAB_Energy_key_1)) {
            return new Nutrient(AbbreviationMapping.getName(NUTTAB_Energy_key_1),
                    nutrients.get(NUTTAB_Energy_key_1).getUnits(),
                    df.format(nutrients.get(NUTTAB_Energy_key_1).getValueD()/4.14));
        }
        else if (!this.getEnergyKJ().getValue().equals("~")) {
            // we have the KJ value available so convert to KCal
            return new Nutrient("Energy, KCal",
                    "kCal",
                    df.format(this.getEnergyKJ().getValueD()/4.14));
        }
        else {
            return new Nutrient("Energy",
                    AbbreviationMapping.getDefaultUnits(USDA_Energy_key),
                    "~");
        }
    }
    public void addNutrient(String abbreviation, Nutrient nutrient) {
        String newName = AbbreviationMapping.getName(abbreviation);
        nutrient.setName(newName);
        nutrients.put(abbreviation, nutrient);
    }
}

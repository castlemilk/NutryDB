package nutritiondb.ben.db2.models;

import java.io.Serializable;
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
    private HashMap<String, Nutrient> nutrients;
    private HashMap<String, Portion> portions;

    public FoodProfile() {
        //empty contructor
    }
    public FoodProfile(String NDB_no, String name, String group) {
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
    public void addNutrient(String abbreviation, Nutrient nutrient) {
        String newName = AbbreviationMapping.getName(abbreviation);
        nutrient.setName(newName);
        nutrients.put(abbreviation, nutrient);
    }
}

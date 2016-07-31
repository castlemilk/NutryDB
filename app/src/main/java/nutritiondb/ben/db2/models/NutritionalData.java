package nutritiondb.ben.db2.models;

/**
 * Created by benebsworth on 11/07/16.
 */
public class NutritionalData {
    public String name;
    public String units;
    public String value;
    public Integer row_type;
    public NutritionalData(){
        super();
    }

    public NutritionalData(String name, String units, String value, Integer row_type) {
        super();
        this.name = name;
        this.units = units;
        this.value = value;
        this.row_type = row_type;
    }
}

package nutritiondb.ben.db2.models;

import java.util.List;

/**
 * Created by benebsworth on 15/11/16.
 */
public class NutrientRow {
    public String name;
    public String units;
    public String value;
    public Integer row_type;
    public List<NutrientRow> invisibleChildren; //required for ExpandableListView
    public NutrientRow(){
        super();
    }

    public NutrientRow(String name, String units, String value, Integer row_type) {
        super();
        this.name = name;
        this.units = units;
        this.value = value;
        this.row_type = row_type;
    }

    public NutrientRow(String name, Integer row_type) {
        /*
        Header/Parent row to nested nutrient rows
         */
        super();
        this.name = name;
        this.units = units;
        this.value = value;
        this.row_type = row_type;
    }
    public NutrientRow(String name, String units, Nutrient nutrient, Integer row_type) {
        super();
        this.name = name;
        this.units = units;
        this.value = nutrient.getValue();
        this.row_type = row_type;
    }
    public NutrientRow(String name, Nutrient nutrient, Integer row_type) {
        super();
        this.name = name;
        this.units = nutrient.getUnits();
        this.value = nutrient.getValue();
        this.row_type = row_type;
    }
    public NutrientRow(Nutrient nutrient, Integer row_type) {
        super();
        this.name = nutrient.getName();
        this.units = nutrient.getUnits();
        this.value = nutrient.getValue();
        this.row_type = row_type;
    }
}

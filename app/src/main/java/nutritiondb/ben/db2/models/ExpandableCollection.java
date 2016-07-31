package nutritiondb.ben.db2.models;

/**
 * Created by benebsworth on 3/06/16.
 * Class is used to as a management data structure for the packing of information relating
 * to each row of the nutiritional information fact sheet.
 */

import java.util.ArrayList;
import java.util.HashMap;

//essential functions as the datastructure/data handler for the purpose of this exercise.
//Enables the clean packing of the data in the required format.
public class ExpandableCollection {

    public String name = "";
    public String units = "";
    public String value = "";
//    public int layoutName;


    public static HashMap<String, ArrayList<ExpandableCollection>> expandable_hashmap;
    public static ArrayList<ExpandableCollection> expandable_main_arr = null;
    public static ArrayList<String> key_value = null;


    public ExpandableCollection(String name, String units, String value) {

        this.name = name;
        this.units = units;
        this.value = value;
//        this.layoutName = layoutName;
    }
}


package nutritiondb.ben.db2.models;

/**
 * Created by benebsworth on 27/06/16.
 */
public class ListItem {
    //TODO: implement POJO for ListItem strucuture to be used in the presentation of search results
    //datastructure:
    /*
    * ListItem
    *   - name (i.e. Cheese)
    *   - group (i.e. Diary, milk product)
    * */
    private String NDB_no;
    private String name;

    public ListItem() {

    }
    public ListItem(String NDB_no, String name) {
        this.NDB_no = NDB_no;
        this.name = name;
    }

    public void setNDBno(String NDB_no) {
        this.NDB_no = NDB_no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNDBno() {
        return NDB_no;
    }
    public String getName() {
        return name;
    }
}

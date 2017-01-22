package nutritiondb.ben.db2.models;

/**
 * Created by benebsworth on 26/10/2016.
 */

public class Item {
    private String mUUID;
    private String mGroup;
    private String mName;
    private String mSource;



    public Item(String UUID, String name, String group, String source) {
        mUUID = UUID;
        mName = name;
        mGroup = group;
        mSource = source;
    }
    public Item () {}
    public String getUUID() {
        return mUUID;
    }
    public void setUUID(String UUID) {
        mUUID = UUID;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    public String getGroup() {
        return mGroup;
    }
    public void setGroup(String group) {
        mGroup = group;
    }

    public String getSource() {
        return mSource;
    }
    public void setSource(String source) {
        mSource = source;
    }
}

package nutritiondb.ben.db2.managers;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.FoodProfileActivity;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.Item;
import nutritiondb.ben.db2.models.Nutrient;
import nutritiondb.ben.db2.models.Portion;

/**
 * Created by benebsworth on 23/05/16.
 */
public class FireBaseController extends Application {

    protected FirebaseDatabase mFirebaseDatabase;
    protected DatabaseReference mDatabaseReference;
    private List<Item> mItemList;
    public HashMap<String, HashMap<String, String>> itemInfo;
    private static final String TAG = FireBaseController.class.getSimpleName();
    protected static final String ITEM_LIST_PATH = "/v2/items";


    public FireBaseController(Context mContext) {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        itemInfo = new HashMap<String, HashMap<String, String>>();
        mItemList = new ArrayList<>();



    }

    public Map<String, String> getItems(final Context mContext) {
        /** Retrieves the item list containing the mapping between searchable food names and their
         * unique ID's
         * */
        Log.i(TAG, "Getting items..");
        final long t0 = System.currentTimeMillis();
        final HashMap<String, String> itemList = new HashMap<String, String>();
        mItemList.clear();

            Query items = mDatabaseReference.child(ITEM_LIST_PATH);

            items.addValueEventListener(new ValueEventListener() {
                HashMap<String, Object> result;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method will be called once when initalised and again whenever the data
                    // is changed.
                    if (dataSnapshot != null) {

                        Map<String, String> results = (Map<String, String>) dataSnapshot.getValue();

                        if (results != null) {
                            for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                                Item item = itemSnapshot.getValue(Item.class);
                                System.out.println(item.toString());
                                item.setUUID(itemSnapshot.getKey());
                                mItemList.add(item);
                            }
                            Log.d(TAG, String.format("Got list in %dms", System.currentTimeMillis() - t0));
                            ((Application)mContext).mItemList = mItemList;
                            ((Application)mContext).saveList(mItemList);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // failed to read the value from the referenced data source
                    Log.w("getMitemList:", "Failed to read value.");
                }

            });
            return itemList;
        }


    public HashMap<String, HashMap<String, String>> getProfile(final String NDB_No,final String source, final Context mContext) {
        /** Method to pull the firebase DB profile information for the specified item.
        * Expected ref_dir will be the NDB_number, i.e 10001. From this there will a tree structure as
        * Follows:
        * NDB_number
        *   - name
        *       -- long description
        *       -- scientific
        *   - nutrients
        *       -- ABBR1 (i.e VITA for vitamin A)
        *           --- name
        *           --- units
        *           --- value
        *       -- ABBR2 (i.e PRO for protein)
        *           --- name
        *           --- units
        *           --- value
        *       -- etc.
        *   - portions
        *       -- 0
        *           --- amount
        *           --- weight (i.e 5g)
        *           --- units (i.e tbsp, cup, stick etc.)
        *       -- etc (there will be an arbitrary range of portion options per food item
        *
        *   - meta
        *       -- carb_factor
        *       -- fat_factor
        *       ...
        *       --reference_description
        * The goal is to walk this data structure and parse it into a HashMap for future use
        * throughout the app.
        *
        * */
        final long t0 = System.currentTimeMillis();
        String ref_dir = "v2/"+source+"/"+NDB_No;

        HashMap<String, HashMap<String, String>> profile = new HashMap<String, HashMap<String, String>>();
        Query items = mDatabaseReference.child(ref_dir);
        Log.i("DB reference: ", mDatabaseReference.toString());
        Log.i("path:", items.getRef().toString());
        items.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (source == "NUTTAB") {
                    String group = (String) dataSnapshot.child("group/group").getValue();
                    String name = (String) dataSnapshot.child("name/name").getValue();
                }
                else if (source == "USDA") {
                    String group = (String) dataSnapshot.child("group").getValue();
                    String name = (String) dataSnapshot.child("name/long").getValue();
                }
                String group = (String) dataSnapshot.child("group").getValue();
                String name = (String) dataSnapshot.child("name/long").getValue();
                Log.d(TAG, "getProfile:group:"+group);
                Log.d(TAG, "getProfile:name:"+name);
                FoodProfile profile = new FoodProfile(NDB_No, name, group);
                //access portions:
                for (DataSnapshot portionSnapshot : dataSnapshot.child("portions").getChildren()) {
                    Portion portion = portionSnapshot.getValue(Portion.class);
                    profile.getPortions().put(portionSnapshot.getKey(),portion);
                }
                //access nutrients:
                for (DataSnapshot nutrientSnapshot : dataSnapshot.child("nutrients").getChildren()) {
                    Nutrient nutrient = nutrientSnapshot.getValue(Nutrient.class);
                    profile.getNutrients().put(nutrientSnapshot.getKey(), nutrient);
                }
                Log.d(TAG, String.format("Got profile in %dms", System.currentTimeMillis() - t0));
                ((FoodProfileActivity)mContext).gotProfile(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return profile;

    }






}

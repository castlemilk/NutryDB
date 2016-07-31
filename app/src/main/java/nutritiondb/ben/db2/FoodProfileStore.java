package nutritiondb.ben.db2;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nutritiondb.ben.db2.managers.DBHandler;
import nutritiondb.ben.db2.models.FoodProfile;

/**
 * Created by benebsworth on 5/07/16.
 */
public class FoodProfileStore<T extends FoodProfile> {

    private static final String TAG = FoodProfileStore.class.getSimpleName();
    public String TABLE_NAME;
    private Gson gson;
    private DBHandler dbHandler;

    public FoodProfileStore(Context context,String table_name) {
        this.TABLE_NAME = table_name;
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
        dbHandler = new DBHandler(context,this.TABLE_NAME);



    }


    public List<T> load(Class<T> clazz) {
        Log.i(TAG, ":table:"+TABLE_NAME+":load");
        List<T> result = new ArrayList<T>();
        List<String> ids = dbHandler.getIds(); //return list to NDB numbers in DB
        if (ids != null) {
            Log.i(TAG, ":table:"+TABLE_NAME+":load:ids:"+ids.size()+":"+ids.toString());
            Type type = new TypeToken<T>(){}.getType();
            Log.i(TAG, ":table:"+TABLE_NAME+":load:type:"+type.toString());
            for (String id : ids) {
                Log.i(TAG, ":table:"+TABLE_NAME+":loading:id:"+id);
                String json_string = dbHandler.getItem(id);
                Log.i(TAG, ":table:"+TABLE_NAME+":loading:json_string:"+json_string);
                if (json_string != null) {
                    T item = gson.fromJson(json_string, clazz); //must feed the class structure
                    if (item != null) {
                        result.add(item);
                    }

                }


            }
            Log.i(TAG, ":table:"+TABLE_NAME+":loaded:items:"+result.size()+":done");
            return result;
        }
        else {
            Log.i(TAG, ":table:"+TABLE_NAME+":load:no ids found");
            return result;
        }
    }
    void save(List<T> lst) {
        for (T item : lst) {
            save(item);
        }
    }

    public void save(T item) {
        if (item == null) {
            Log.i(TAG, ":table:"+TABLE_NAME+":save:null item");
            Log.d(getClass().getName(), "Can't save null item");
            return;
        }
        else {
            try {
                Log.i(TAG, ":table:"+TABLE_NAME+":save:"+item.toString());
                dbHandler.addItem(item.NDB_no, gson.toJson(item));
                Log.i(TAG, ":table:"+TABLE_NAME+":saved:length:"+gson.toJson(item).length());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean delete(String NDB_no) {
        Log.i(TAG, ":table:"+TABLE_NAME+":delete:"+NDB_no);
        try {
            if (dbHandler.removeItem(NDB_no)) {
                Log.i(TAG, ":table:"+TABLE_NAME+":deleted:success");
                return true;
            }
            else {
                Log.i(TAG, ":table:"+TABLE_NAME+":deleted:failed");
                return false;
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getItemIds() {
        return dbHandler.getIds();
    }
}

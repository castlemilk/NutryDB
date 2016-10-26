package nutritiondb.ben.db2.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import nutritiondb.ben.db2.models.Item;

/**
 * Created by benebsworth on 19/06/16.
 */
public class DBHandler_ItemList extends SQLiteOpenHelper {

    private static final String TAG = DBHandler_ItemList.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "nutryDB";
    // ItemList table name
    private static final String TABLE_ITEMLIST = "ItemList";

    // ItemList Table Columns names
    private static final String KEY_ID = "UUID";
    private static final String KEY_NAME = "name";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_GROUP = "food_group";
    private GsonBuilder builder;
    private Gson gson;


    public DBHandler_ItemList(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_ITEMLIST + "("
                + KEY_ID + " TEXT PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_GROUP + " TEXT, " +
                KEY_SOURCE + " TEXT " + ")";
        db.execSQL(CREATE_ITEM_LIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ITEMLIST);
        //Create table again
        onCreate(db);

    }

    public void deleteTable(SQLiteDatabase db) {
        System.out.println("DELETING TABLE: "+TABLE_ITEMLIST);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ITEMLIST);
    }



    public void addItem(Item item) {
        SQLiteDatabase db  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.getUUID());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_SOURCE, item.getGroup());
        values.put(KEY_GROUP, item.getSource());
        db.insert(TABLE_ITEMLIST, null, values);

    }
    public Item getItem(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "";
        q += "SELECT * FROM "+TABLE_ITEMLIST;
        q += " WHERE "+KEY_NAME+"="+name;
        Cursor cursor = db.rawQuery(q, null);

        if (cursor != null) {
            cursor.moveToFirst();

            Item item = new Item(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return item;
        }
        else {
            return null;
        }
    }
    public List<Item> searchList(String searchTerm, int match_limit) {
        List<Item> result = new ArrayList<>();

        String q = "";
        q += "SELECT * FROM "+TABLE_ITEMLIST;
        q += " WHERE "+KEY_NAME + " LIKE '%"+searchTerm+"%'";
        q += " LIMIT 0,"+Integer.toString(match_limit);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            do {


                result.add(new Item(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return result;
    }
    public void saveList(List<Item> itemList) {
        AsyncTask<List<Item>,Void,Void> saveListTask;
        saveListTask = new AsyncTask<List<Item>, Void, Void>() {
            final long t0 = System.currentTimeMillis();
            @Override
            protected Void doInBackground(List<Item>... params) {
                for (Item item : params[0]) {
                    addItem(item);
                }
                Log.d(TAG, String.format("Saved list in DB in %dms", System.currentTimeMillis() - t0));
                return null;
            }

        };

        saveListTask.execute(itemList);


    }

    public List<Item> getItemList() {
        List<Item> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ITEMLIST, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    result.add(new Item(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                } while (cursor.moveToNext());
                cursor.close();
                return result;
            }
            else {
                return null;
            }
        } else {
            return null;
        }
    }
}

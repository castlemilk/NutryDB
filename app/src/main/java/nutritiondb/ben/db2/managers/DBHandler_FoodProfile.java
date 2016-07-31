package nutritiondb.ben.db2.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nutritiondb.ben.db2.models.FoodProfile;

/**
 * Created by benebsworth on 19/06/16.
 */
public class DBHandler_FoodProfile extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "nutryDB";
    // ItemList table name
    private static final String TABLE_FOODPROFILE = "food_profile";
    // ItemList Table Columns names
    private static final String KEY_ID = "ndb_no";
    private static final String KEY_JSON = "json_block";
    private GsonBuilder builder;
    private Gson gson;


    public DBHandler_FoodProfile(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        builder = new GsonBuilder();
        gson = builder.create();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        deleteTable(db);
        String CREATE_ITEM_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_FOODPROFILE + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_JSON +" TEXT"+")";
        db.execSQL(CREATE_ITEM_LIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_FOODPROFILE);
        //Create table again
        onCreate(db);

    }

    public void deleteTable(SQLiteDatabase db) {
        System.out.println("DELETING TABLE: "+ TABLE_FOODPROFILE);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_FOODPROFILE);
    }
    public boolean deleteProfile(String NDB_no) {
        System.out.println("Deleting item: "+ NDB_no);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FOODPROFILE,KEY_ID+"="+NDB_no, null) > 0;

    }

    public void addProfile(FoodProfile profile) {
        SQLiteDatabase db  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, profile.getNDBno());
        values.put(KEY_JSON, gson.toJson(profile));
        try {
            db.insert(TABLE_FOODPROFILE, null, values);
            db.close();
        }
        catch (SQLiteConstraintException e) {
            //attempted to add a duplicate
        }

    }
    public FoodProfile getProfile(String NDB_no) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FOODPROFILE, new String[] {KEY_ID, KEY_JSON},
                KEY_ID +"=?",
                new String[] {String.valueOf(NDB_no)},null,null,null,null);

        if (cursor != null) {
            cursor.moveToFirst();

             FoodProfile profile = gson.fromJson(cursor.getString(1),
                     FoodProfile.class);

            return profile;
        }
        else {
            return null;
        }
    }
}

package nutritiondb.ben.db2.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benebsworth on 5/07/16.
 */
public class DBHandler extends SQLiteOpenHelper{

    private static final String TAG = DBHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "nutryDB";
    private static final String KEY_ID = "NDB_no";
    private static final String KEY_JSON = "json";
    private String TABLE_NAME;
    public DBHandler(Context context, String table_name) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME = table_name;
        SQLiteDatabase db = getReadableDatabase();
        onCreate(db);



    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Running onCreate for table: "+TABLE_NAME);
        String CREATE_TABLE_NAME_TABLE = "CREATE TABLE IF NOT EXISTS "+  TABLE_NAME+ "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_JSON +" TEXT"+")";
        db.execSQL(CREATE_TABLE_NAME_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
    //TODO: create generic DB handler based off fed table name

    public List<String> getIds() {
        SQLiteDatabase db = getReadableDatabase();
        final long t0 = System.currentTimeMillis();
        List<String> result = new ArrayList<>();

        String GET_IDS = "SELECT "+KEY_ID+" FROM "+TABLE_NAME;
        db.rawQuery(GET_IDS, null);
        Cursor cursor = db.rawQuery(GET_IDS,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    result.add(cursor.getString(0));
                } while (cursor.moveToNext());
                cursor.close();
                Log.d(TAG, String.format("Got list of ids from DB in %dms", System.currentTimeMillis() - t0));
                return result;
            }
            else {
                Log.d(TAG, String.format("Failed to find anything from DB in %dms", System.currentTimeMillis() - t0));
                return result;
            }
        }
        else {
            Log.d(TAG, String.format("Failed to find anything from DB in %dms", System.currentTimeMillis() - t0));
            return result;
        }


    }

    public  String getItem(String ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ITEM = "SELECT "+KEY_JSON+" FROM "+TABLE_NAME+" WHERE "+KEY_ID+" = '"+ID+"'";
        Cursor cursor = db.rawQuery(GET_ITEM, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }


    }
    public void addItem(String id, String itemAsJson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_JSON, itemAsJson);
        db.insertOrThrow(TABLE_NAME, null, values);




    }
    public boolean removeItem(String id) {
        SQLiteDatabase db = getWritableDatabase();
        boolean deleted = db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { id }) > 0;
        if (!deleted) {
            Log.i(TAG, ":table:"+TABLE_NAME+":delete:"+id+"failed using db.delete, trying raw");
            String DELETE_ROW = "DELETE FROM "+TABLE_NAME+"WHERE "+KEY_ID+" ='"+id+"'";
            db.execSQL(DELETE_ROW);
            db.close();
            return true;
        }
        else {
            Log.i(TAG, ":table:"+TABLE_NAME+":delete:"+id+".delete succeeded");
            return deleted;
        }
    }


}

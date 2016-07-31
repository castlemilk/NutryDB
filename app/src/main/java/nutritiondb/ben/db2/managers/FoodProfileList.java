package nutritiondb.ben.db2.managers;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.FoodProfileStore;
import nutritiondb.ben.db2.models.FoodProfile;

/**
 * Created by benebsworth on 5/07/16.
 */
public class FoodProfileList extends AbstractList<FoodProfile> {
    //TODO: implement datastructure for handling the IO of foodprofiles
    private final String TAG = getClass().getSimpleName();
    private FoodProfileStore<FoodProfile> store;
    private List<FoodProfile> list;
    private Application app;
    private int maxSize;
    private List<FoodProfile> filteredList;
    private final DataSetObservable dataSetObservable;
    private String filter;
    private Handler handler;



    public FoodProfileList(Application app, FoodProfileStore<FoodProfile> store) {
        this(app,store,100);
    }

    FoodProfileList(Application app, FoodProfileStore<FoodProfile> store, int maxSize) {
        this.app = app;
        this.store = store;
        this.maxSize = maxSize;
        this.list = new ArrayList<FoodProfile>();
        this.filteredList = new ArrayList<FoodProfile>();
        this.dataSetObservable = new DataSetObservable();
        this.filter = "";
    }


    @Override
    public FoodProfile get(int location) {
        return this.list.get(location);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    public void notifyDataSetChanged() {
        this.filteredList.clear();
        if (filter == null || filter.length() == 0) {
            this.filteredList.addAll(this.list);
        }
        else {
            //TODO: apply a filter...
        }
    }

    public boolean contains(String NDB_no) {
        Log.i(TAG, ":table:"+this.store.TABLE_NAME+":contains:"+NDB_no);
        if (list.size() > 0) {
            for (FoodProfile item : list) {
                if (item.getNDBno().equals(NDB_no)) {
                    Log.i(TAG, ":table:"+this.store.TABLE_NAME+":contains:found");
                    return true;
                }
            }
            Log.i(TAG, ":table:"+this.store.TABLE_NAME+":contains:not found");
            return false;
        } else {
            Log.i(TAG, ":table:"+this.store.TABLE_NAME+":contains:list empty");
            return false;
        }
    }

    public void load() {
        Log.i(TAG, ":table:"+this.store.TABLE_NAME+":load:");
        this.list.addAll(this.store.load(FoodProfile.class));
        Log.i(TAG, ":table:"+this.store.TABLE_NAME+":loaded:"+this.list.size()+":items");
        notifyDataSetChanged();
    }

    private void doUpdateLastAccess(FoodProfile fp) {
        long t = System.currentTimeMillis();
        long dt = t - fp.lastAccess;
        if (dt < 2000) {
            return;
        }
        fp.lastAccess = t;
        store.save(fp);
    }
    void updateLastAccess(final FoodProfile fp) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doUpdateLastAccess(fp);
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    doUpdateLastAccess(fp);
                }
            });
        }
    }
    public boolean add(FoodProfile item) {
        Log.i(TAG, "table:"+this.store.TABLE_NAME+":add:"+item.getNDBno());
        int index = this.list.indexOf(item);
        boolean already_exists = store.getItemIds().contains(item.getNDBno());
        Log.i(TAG,"table:"+store.TABLE_NAME+":current list:"+store.getItemIds().toString());
        Log.i(TAG, "table:"+store.TABLE_NAME+"index:"+index);
        Log.i(TAG, "table:already_exists:"+already_exists);
        if (index > -1 || already_exists) {
            Log.i(TAG, "table:"+this.store.TABLE_NAME+":add:"+item.getNDBno()+":already exists");
            return false;
        }
        Log.i(TAG, "table:"+this.store.TABLE_NAME+":added:previous length:"+this.list.size());
        this.list.add(item);
        store.save(item);
        Log.i(TAG, "table:"+this.store.TABLE_NAME+":added:current length:"+this.list.size());
        Log.i(TAG, "table:"+this.store.TABLE_NAME+":added:"+item.getNDBno()+":done");

        if (this.list.size() > this.maxSize) {
            //TODO: add to top of stack via sort and add
        }
        notifyDataSetChanged();

        return true;
    }
    public boolean remove(String NDB_no) {
        Log.i(TAG, "table:"+this.store.TABLE_NAME+":remove:"+NDB_no);
        if (this.list != null) {
            for (FoodProfile item : list) {
                if (item.getNDBno().equals(NDB_no)) {
                    Log.i(TAG, "table:"+this.store.TABLE_NAME+":remove:"+NDB_no+":found:removing");
                    this.list.remove(item);
                    store.delete(NDB_no);
                    notifyDataSetChanged();
                    Log.i(TAG, "table:"+this.store.TABLE_NAME+":remove:"+NDB_no+":found:removed");
                    return true;
                }
            }
            Log.i(TAG, "table:"+this.store.TABLE_NAME+":remove:"+NDB_no+":not found");
            return false;
        }
        else {
            return false;
        }
    }

    public FoodProfile remove(int index) {

        FoodProfile foodProfile = this.filteredList.get(index);
        Log.i(TAG, "table:"+this.store.TABLE_NAME+":removing"+foodProfile.NDB_no);
        int realIndex = this.list.indexOf(foodProfile);
        if (realIndex > -1) {
            Log.i(TAG, "table:"+this.store.TABLE_NAME+":removing"+foodProfile.NDB_no+":found realIndex");
            return removeByIndex(realIndex);
        }
        return null;
    }

    public FoodProfile removeByIndex(int index) {
        FoodProfile foodProfile = this.list.remove(index);
        if (foodProfile != null) {
            boolean removed = store.delete(foodProfile.NDB_no);

            if (removed) {
                Log.i(TAG, "table:"+this.store.TABLE_NAME+":removing"+foodProfile.NDB_no+":success");
                notifyDataSetChanged();
            }
            else {
                Log.i(TAG, "table:"+this.store.TABLE_NAME+":removing"+foodProfile.NDB_no+":failed");
            }


        }
        return foodProfile;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.dataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.dataSetObservable.unregisterObserver(observer);
    }
}

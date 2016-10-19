    package nutritiondb.ben.db2;

    import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.shamanland.fonticon.FontIconTypefaceHolder;

import java.util.ArrayList;
import java.util.List;

import nutritiondb.ben.db2.managers.DBHandler_FoodProfile;
import nutritiondb.ben.db2.managers.DBHandler_ItemList;
import nutritiondb.ben.db2.managers.FireBaseController;
import nutritiondb.ben.db2.managers.FoodProfileList;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.ListItem;
import nutritiondb.ben.db2.views.adapters.ItemListAdapter;

/**
 * Created by benebsworth on 25/06/16.
 */
public class Application extends android.app.Application {
    private static final String PREF                    = "app";
    static final String PREF_UI_THEME                   = "UITheme";
    static final String PREF_UI_THEME_LIGHT             = "light";
    static final String PREF_UI_THEME_DARK              = "dark";
    public FoodProfileList                              bookmarks;
    public FoodProfileList                              history;
    private FoodProfileStore<FoodProfile> bookmarkStore;
    private FoodProfileStore<FoodProfile> historyStore;
    public List<ListItem>                               mItemList;
    public boolean listCached                           = false;
    private String lookupQuery                          = "";
    public ItemListAdapter lastResult;
    public DBHandler_ItemList itemListDB;
    public DBHandler_FoodProfile foodProfileDB;
    public FireBaseController fireBaseController;
    public ListItemFilter mFilter;
    public FoodProfile currentFoodProfile;

    private static final String TAG = Application.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        FontIconTypefaceHolder.init(getAssets(), "fontawesome-4.2.0.ttf");
        lastResult = new ItemListAdapter(this);
        itemListDB = new DBHandler_ItemList(this);
        foodProfileDB = new DBHandler_FoodProfile(this);
        fireBaseController = new FireBaseController();

        bookmarkStore = new FoodProfileStore<>(this,"bookmarks");
        historyStore = new FoodProfileStore<>(this,"history");

        bookmarks = new FoodProfileList(this, bookmarkStore);
        history =  new FoodProfileList(this, historyStore);


        bookmarks.load();
        history.load();
        Log.i(TAG,"initialised objects..");
    }

    SharedPreferences prefs() {
        return this.getSharedPreferences(PREF, Activity.MODE_PRIVATE);
    }
    String getPreferredTheme() {
        return prefs().getString(Application.PREF_UI_THEME,
                Application.PREF_UI_THEME_LIGHT);
    }

    public void installTheme(Activity activity) {
        String theme = getPreferredTheme();
        if (theme.equals(PREF_UI_THEME_DARK)) {
            activity.setTheme(android.R.style.Theme_Holo);
        }
        else {
            activity.setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        }
    }

    public void saveList(List<ListItem> itemList) {
        itemListDB.saveList(itemList);
    }

    public List<ListItem> find(String query, boolean cachedList) {
        //TODO: implement search or add refer to search module
        long t0 = System.currentTimeMillis();
        if (cachedList) {
            Log.i(TAG,"using cache....");
            return itemListDB.searchList(query, 10);
        }
        else {
            Log.i(TAG,"using temporary list....");
            if (mItemList != null) {
                mFilter = new ListItemFilter(query, mItemList, ListItemFilter.SearchMode.CONTAINS);
                return mFilter.filter();
            }
            else {
                return null;
            }
        }

    }

    public void addLookupListener(LookupListener listener){
        lookupListeners.add(listener);
    }
    public void removeLookupListener(LookupListener listener){
        lookupListeners.remove(listener);
    }

    private void setLookupResult(String query, List<ListItem> data) {
        this.lastResult.setData(data);
        lookupQuery = query;
        SharedPreferences.Editor edit = prefs().edit();
        edit.putString("query", query);
        edit.apply();
    }
    private AsyncTask<Void, Void, List<ListItem>> currentLookupTask;

    public void lookup(String query) {
        this.lookup(query, true, true);
    }

    private void lookup(final String query, boolean async, final boolean cached) {
        Log.i(TAG, "looking up: "+query);
        final long t0 = System.currentTimeMillis();
        //TODO: implement search
        if (currentLookupTask != null) {
            currentLookupTask.cancel(false);
            notifyLookupCanceled(query);
            currentLookupTask = null;
        }
        notifyLookupStarted(query);
        if (query == null || query.equals("")) {
            setLookupResult("", new ArrayList<ListItem>());
            notifyLookupFinished(query);
        }
        if (async) {
            currentLookupTask = new AsyncTask<Void, Void, List<ListItem>>() {
                @Override
                protected List<ListItem> doInBackground(Void... params) {
                    return find(query, listCached);
                }
                @Override
                protected void onPostExecute(List<ListItem> result) {
                    if (!isCancelled()) {
                        if (result != null) {
                            Log.i(TAG, "found: " + result.toString());
                            setLookupResult(query, result);
                            notifyLookupFinished(query);
                            currentLookupTask = null;
                            Log.d(TAG, String.format("Got list in %dms", System.currentTimeMillis() - t0));
                        } else
                        {
                            Log.i(TAG, "found Nothing");
                            notifyLookupFinished(query);
                            currentLookupTask = null;
                        }


                    }
                }
            };
            currentLookupTask.execute();
        }
        else {
            setLookupResult(query, find(query, cached));
            notifyLookupFinished(query);
        }


    }


    public String getLookupQuery() {
        return lookupQuery;
    }

    public List<ListItem> getitemList() {
        return getItemList(false);
    }
    public List<ListItem> getItemList(boolean async) {
        /**
         * Get item list from Firebase if the there is no current list cached.
         * TODO: this needs improving because one a list is cached there is no further check
         * TODO: to evaluate changes in the list and respond accordingly to syncronise.
         */
        List<ListItem> itemList = itemListDB.getItemList();
        if (itemList != null) {
            Log.i(TAG, "Using stored list");
            listCached = true;

        }
        else {
                Log.i(TAG, "list not found, pulling from server");
                fireBaseController.getItems(this);
            }

        return itemList;
    }

    public void addBookmark(FoodProfile foodProfile) {
        bookmarks.add(foodProfile);
    }

    public void removeBookmark(String NDB_no) {
        bookmarks.remove(NDB_no);
    }

    public Boolean isBookmarked(String NDB_no) {
        return bookmarks.contains(NDB_no);
    }


    private void notifyLookupStarted(String query) {
        for (LookupListener l : lookupListeners) {
            l.onLookupStarted(query);
        }
    }

    private void notifyLookupFinished(String query) {
        for (LookupListener l : lookupListeners) {
            l.onLookupFinished(query);
        }
    }

    private void notifyLookupCanceled (String query) {
        for (LookupListener l : lookupListeners) {
            l.onLookupCanceled(query);
        }
    }

    private List<LookupListener> lookupListeners = new ArrayList<LookupListener>();

}

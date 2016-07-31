package nutritiondb.ben.db2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nutritiondb.ben.db2.models.ListItem;

/**
 * Created by benebsworth on 30/06/16.
 */
public class ListItemFilter {

//    static final String CONTAINS                   = "CONTAINS";
//    static final String STARTS_WITH                = "STARTS_WITH";
    private static final String TAG = ListItemFilter.class.getSimpleName();
    private List<ListItem> mItemsToSearchIn;
    private List<ListItem> mItemsFound;
    private String mQuery;
    private ListItemFilter.SearchMode mSearchMode;
    ListItemFilter(String query, List<ListItem> itemsToSearchIn, ListItemFilter.SearchMode searchMode) {
        mItemsToSearchIn = itemsToSearchIn;
        mQuery = query;
        mSearchMode = searchMode;

    }

    protected List<ListItem> filter() {

        List<ListItem> results = new ArrayList<>();

        if (mQuery.isEmpty()) {
            return results;
        }
        if (mSearchMode==ListItemFilter.SearchMode.CONTAINS) {
            for (ListItem item : mItemsToSearchIn) {
                if (item.getName().toLowerCase().contains(mQuery.toLowerCase())) {
                    results.add(item);
                }
            }
        } else {
            for (ListItem item : mItemsToSearchIn) {
                if (item.getName().toLowerCase().startsWith(mQuery.toLowerCase())) {
                    results.add(item);
                }

            }
        }
            Log.i(TAG, "Filtered "+String.valueOf(mItemsToSearchIn.size())+" items");
            Log.i(TAG, "Found "+String.valueOf(results.size())+" matches");
            return results;
    }

    public enum SearchMode {

        STARTS_WITH, CONTAINS

    }
}

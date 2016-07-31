package nutritiondb.ben.db2.fragments;

import android.util.Log;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.managers.FoodProfileList;

/**
 * Created by benebsworth on 25/06/16.
 */
public class FavouriteFragment extends FoodProfileListFragment {
    private static final String TAG =FoodProfileListFragment.class.getSimpleName();
    @Override
    FoodProfileList getFoodProfileList() {
        Log.i(TAG,"getFoodProfileList:getting list");
        Application app = (Application) getActivity().getApplication();
        Log.i(TAG,"getFoodProfileList:found:"+app.bookmarks.size()+" items");
        return app.bookmarks;
    }

    @Override
    String getItemClickAction() {
        return "showFavourites";
    }

    @Override
    int getDeleteConfirmationItemCountResId() {
        return R.plurals.confirm_delete_bookmark_count;
    }

    @Override
    String getPreferencesNS() {
        return "bookmarks";
    }

    @Override
    int getEmptyIcon() {
        return R.xml.ic_empty_view_bookmark;
    }
    @Override
    String getEmptyText() {
        return getString(R.string.main_empty_bookmarks);
    }
}

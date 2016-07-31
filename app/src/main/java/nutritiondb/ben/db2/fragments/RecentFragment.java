package nutritiondb.ben.db2.fragments;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.managers.FoodProfileList;

/**
 * Created by benebsworth on 25/06/16.
 */
public class RecentFragment extends FoodProfileListFragment {

    @Override
    FoodProfileList getFoodProfileList() {
        Application app = (Application) getActivity().getApplication();
        return app.history;
    }

    @Override
    String getItemClickAction() {
        return "showHistory";
    }

    @Override
    int getEmptyIcon() {
        return R.xml.ic_empty_view_history;
    }

    @Override
    CharSequence getEmptyText() {
        return getString(R.string.main_empty_history);
    }

    @Override
    int getDeleteConfirmationItemCountResId() {
        return R.plurals.confirm_delete_history_count;
    }

    @Override
    String getPreferencesNS() {
        return "history";
    }
}

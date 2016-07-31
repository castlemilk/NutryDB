package nutritiondb.ben.db2.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import nutritiondb.ben.db2.views.adapters.SettingsListAdapter;

/**
 * Created by benebsworth on 26/06/16.
 */
public class SettingsFragment extends ListFragment {
    private final static String TAG = SettingsFragment.class.getSimpleName();
    private SettingsListAdapter listAdapter;
    private AlertDialog clearCacheConfirmationDialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listAdapter = new SettingsListAdapter(this);
        setListAdapter(listAdapter);
    }
}

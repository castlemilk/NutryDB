package nutritiondb.ben.db2.views.adapters;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by benebsworth on 26/06/16.
 */
public class SettingsListAdapter extends BaseAdapter implements SharedPreferences.OnSharedPreferenceChangeListener {

    final static int CSS_SELECT_REQUEST = 13;

    private final static String TAG = SettingsListAdapter.class.getSimpleName();
//    private final Activity context;
//    private final Application app;

    private Fragment                fragment;
    public SettingsListAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}

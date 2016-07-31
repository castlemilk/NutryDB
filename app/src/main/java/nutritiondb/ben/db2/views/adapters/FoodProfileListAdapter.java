package nutritiondb.ben.db2.views.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DateFormat;

import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.managers.FoodProfileList;
import nutritiondb.ben.db2.models.FoodProfile;

/**
 * Created by benebsworth on 12/07/16.
 */
public class FoodProfileListAdapter extends BaseAdapter {
    private final String TAG = getClass().getSimpleName();
    FoodProfileList list;
    DateFormat dateFormat;
    private DataSetObserver observer;
    private boolean selectionMode;

    public FoodProfileListAdapter(FoodProfileList list) {
        this.list = list;
        this.dateFormat = DateFormat.getDateInstance();
        this.observer = new DataSetObserver() {
            @Override
            public void onChanged() {
                Log.i(TAG, "onChanged:datachanged");
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                notifyDataSetInvalidated();
            }
        };
        this.list.registerDataSetObserver(observer);
    }

    //TODO: create adapter which is used to generate the list of bookmarked/history items
    @Override
    public int getCount() {
        synchronized (list) {
            return list == null ? 0 : list.size();
        }

    }

    @Override
    public Object getItem(int position) {
        synchronized (list) {
            return list.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return selectionMode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.i(TAG, ":getView:getting view for row");

        FoodProfile foodProfile = list.get(position);
//        Log.i(TAG, ":getView:item:"+foodProfile);
//        Log.i(TAG, ":getView:item:NDB_no:"+foodProfile.NDB_no);
        Log.i(TAG, ":getView:item:name:"+foodProfile.name);
        Log.i(TAG, ":getView:item:group:"+foodProfile.group);
        CharSequence timestamp = DateUtils.getRelativeTimeSpanString(foodProfile.createdAt);
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.blob_descriptor_list_item, parent,
                    false);
        }

        TextView titleView = (TextView) view
                .findViewById(R.id.blob_descriptor_key);
        titleView.setText(foodProfile.name);
        TextView sourceView = (TextView) view
                .findViewById(R.id.blob_descriptor_source);
        sourceView.setText(foodProfile.group);
        TextView timestampView = (TextView) view
                .findViewById(R.id.blob_descriptor_timestamp);
        timestampView.setText(timestamp);
        CheckBox cb = (CheckBox) view
                .findViewById(R.id.blob_descriptor_checkbox);
        cb.setVisibility(isSelectionMode() ? View.VISIBLE : View.GONE);


        return view;
    }
}

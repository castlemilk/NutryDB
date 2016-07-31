package nutritiondb.ben.db2.views.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.ListItem;

/**
 * Created by benebsworth on 27/06/16.
 */
public class ItemListAdapter extends BaseAdapter{
    //TODO: implement adapter for producing the filtered list of items from the lookup function
    //TODO: make POJO for item (i.e. <name, group, etc>
    private static final String TAG = ItemListAdapter.class.getSimpleName();
    Handler mainHandler;
    ExecutorService executor;
    ArrayList<ListItem> list;
    ArrayList<ListItem> itemsFound;

    private final int   chunkSize;
    private final int   loadMoreThreashold;
    int                 MAX_SIZE   = 10000;

    public ItemListAdapter(Application app) {
        this(app, 20,5);
    }

    public ItemListAdapter(Application app, int chunkSize, int loadMoreThreshold) {
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.executor = Executors.newSingleThreadExecutor();
        this.list = new ArrayList<>(chunkSize);
        this.chunkSize = chunkSize;
        this.loadMoreThreashold = loadMoreThreshold;

    }

    public void setData(List<ListItem> data) {
        //TODO: add data to adapter list
        synchronized (list) {
            list.clear();
            list.addAll(data);
            Log.i(TAG, "found items: ");
            for (ListItem item : list) {
                Log.i(TAG, item.getName());
            }
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        synchronized (list) {
            return list == null ? 0 : list.size();
        }
    }

    @Override
    public ListItem getItem(int position) {
        ListItem result;
        synchronized (list) {
            result = list.get(position);

        }
        return result;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = list.get(position);
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_description_list_item, parent, false);
        }

        TextView titleView  = (TextView) view.findViewById(R.id.blob_descriptor_key);
        titleView.setText(item.getName());
//        TextView subTitleView = (TextView) view.findViewById(R.id.blob_descriptor_source);
        //subTitleView.setText(\\TODO: set subtitle);
        //TODO: can implement more list presentation data here.
        return view;
    }

    //TODO: implement the capability to load more synchronously/asynchronously
    //TODO: implement list filter from this adapter, so it can be integrated into other features


//    public class ListItemFilter extends Filter {
//
//        static final String CONTAINS                   = "CONTAINS";
//        static final String STARTS_WITH                = "STARTS_WITH";
//        private List<ListItem> mItemsToSearchIn;
//        private List<ListItem> mItemsFound;
//        private String mQuery;
//        private String mSearchMode;
//        ListItemFilter(String query, List<ListItem> itemsToSearchIn, String searchMode) {
//            mItemsToSearchIn = itemsToSearchIn;
//            mQuery = query;
//            mSearchMode = searchMode;
//
//        }
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            Filter.FilterResults filterResults = new FilterResults();
//
//            List<ListItem> results = new ArrayList<>();
//
//            if (TextUtils.isEmpty(constraint)) {
//                filterResults.values = results;
//                filterResults.count = 0;
//                return filterResults;
//            }
//            if (mSearchMode==CONTAINS) {
//                for (ListItem item : mItemsToSearchIn) {
//                    if (item.getName().contains(constraint.toString().toLowerCase())) {
//                        results.add(item);
//                    }
//                }
//            } else {
//                for (ListItem item : mItemsToSearchIn) {
//                    if (item.getName().startsWith(constraint.toString().toLowerCase())) {
//                        results.add(item);
//                    }
//
//                }
//            }
//            filterResults.values = results;
//            filterResults.count = results.size();
//            return filterResults;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults filteredResults) {
//            if (!(filteredResults.values instanceof List<?>)) return;
//
//            List<ListItem> results = (List<ListItem>) filteredResults.values;
//            setData(results);
//
//
//        }
//    }




}



package nutritiondb.ben.db2.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import nutritiondb.ben.db2.FoodProfileActivity;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.managers.FoodProfileList;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.views.adapters.FoodProfileListAdapter;

/**
 * Created by benebsworth on 12/07/16.
 */
public abstract class FoodProfileListFragment extends BaseListFragment {
    private static final String TAG =FoodProfileListFragment.class.getSimpleName();
    private Drawable icFilter;
    private Drawable icClock;
    private Drawable icList;
    private Drawable icArrowUp;
    private Drawable icArrowDown;

    private FoodProfileListAdapter listAdapter;
    private AlertDialog deleteConfirmationDialog = null;
    private final static String PREF_SORT_ORDER = "sortOrder";
    private final static String PREF_SORT_DIRECTION = "sortDir";
    private MenuItem miFilter = null;
    private FoodProfileList foodProfileList;
//    private Intent intent;
    //TODO: implement fragment for history/bookarked item list

    public boolean isFilterExpanded() {
        return miFilter != null && miFilter.isActionViewExpanded();
    }

    public void collapseFilter() {
        if (miFilter != null) {
            miFilter.collapseActionView();
        }
    }
    abstract FoodProfileList getFoodProfileList();
    abstract String getItemClickAction();
    protected void setSelectionMode(boolean selectionMode) {
        listAdapter.setSelectionMode(selectionMode);
    }

    protected int getSelectionMenuId() {
        return R.menu.blob_descriptor_selection;
    }
    @Override
    int getEmptyIcon() {
        return 0;
    }

    @Override
    CharSequence getEmptyText() {
        return null;
    }


    abstract int getDeleteConfirmationItemCountResId();


    abstract String getPreferencesNS();

    private SharedPreferences prefs() {
        return getActivity().getSharedPreferences(getPreferencesNS(), Activity.MODE_PRIVATE);
    }

    protected boolean onSelectionActionItemClicked(final ActionMode mode, MenuItem item) {
        Log.i(TAG,"onSelectionActionItemClicked:Action item selected");
        ListView listView = getListView();
        switch (item.getItemId()) {
            case R.id.blob_descriptor_delete:
                Log.i(TAG,"onSelectionActionItemClicked:delete item(s) clicked");
                int count = listView.getCheckedItemCount();
                String countStr = getResources().getQuantityString(getDeleteConfirmationItemCountResId(), count, count);
                String message = getString(R.string.blob_descriptor_confirm_delete, countStr);
                deleteConfirmationDialog = new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSelectedItems();
                                mode.finish();
                                deleteConfirmationDialog = null;
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).create();
                deleteConfirmationDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        deleteConfirmationDialog = null;
                    }
                });
                deleteConfirmationDialog.show();
                return true;
            case R.id.blob_descriptor_select_all:
                int itemCount = listView.getCount();
                for (int i = itemCount - 1; i > -1; --i) {
                    listView.setItemChecked(i, true);
                }
                return true;
            default:
                return false;

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG,"onViewCreated:view created");
        super.onViewCreated(view, savedInstanceState);

        foodProfileList = getFoodProfileList();

        Log.i(TAG,"onViewCreated:"+foodProfileList.size()+" in list");
        //TODO: ADD FILTERING/ORDERING CAPABILITIY PREFEFRENCES ETC.
        listAdapter = new FoodProfileListAdapter(foodProfileList);
        final FragmentActivity activity = getActivity();
        //TODO: ADD GRAPHICS FOR DIFFERENT FUNCTIONS (I,E, FILTER ETC)


        final ListView listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i(TAG,"onViewCreated:onItemClick:clicked");
                Intent intent = new Intent(activity, FoodProfileActivity.class);
                FoodProfile foodProfile = foodProfileList.get(position);
                intent.setAction(getItemClickAction());
                intent.putExtra("position", position);
                intent.putExtra("profile", foodProfile);
                Log.i(TAG,"onViewCreated:onItemClick:clicked:foodprofie:"+foodProfile);
                Log.i(TAG,"onViewCreated:onItemClick:clicked:foodprofie:DBN_no:"+foodProfile.NDB_no);
                Log.i(TAG,"onViewCreated:onItemClick:clicked:foodprofie:name:"+foodProfile.name);
                startActivity(intent);
            }
        });
        setListAdapter(listAdapter);
    }

    protected void deleteSelectedItems() {
        Log.i(TAG,"deleteSelectedItems:deleting");
        SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
        Log.i(TAG,"deleteSelectedItems:delete:checked items:"+checkedItems.size());
        for (int i = checkedItems.size() - 1; i > -1; --i) {
            int position = checkedItems.keyAt(i);
            boolean checked = checkedItems.get(position);
            if (checked) {
                Log.i(TAG, "deleteSelectedItems:arraylocation:before"+getFoodProfileList());
                getFoodProfileList().remove(position);

                Log.i(TAG, "deleteSelectedItems:arraylocation:after"+getFoodProfileList());
            }
        }
        getFoodProfileList().notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.blob_descriptor_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        Log.i(TAG,"onPrepareOptionsMenu:preparing menu");
        listAdapter.notifyDataSetChanged();

//        FoodProfileList list = getFoodProfileList();
//        if (listAdapter != null) {
//            FoodProfileListAdapter listAdapter = new FoodProfileListAdapter(list);
//            setListAdapter(listAdapter);
//        }

//        miFilter = menu.findItem(R.id.action_filter);
//        miFilter.setIcon(icFilter);
//
//        View filterActionView = miFilter.getActionView();
//        SearchView searchView = (SearchView) filterActionView
//                .findViewById(R.id.fldFilter);
//        searchView.setQueryHint(miFilter.getTitle());
//        searchView.setQuery(list.getFilter(), true);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                BlobDescriptorList list = getDescriptorList();
//                if (!newText.equals(list.getFilter())) {
//                    getDescriptorList().setFilter(newText);
//                }
//                return true;
//            }
//        });
//        setSortOrder(menu.findItem(R.id.action_sort_order), list.getSortOrder());
//        setAscending(menu.findItem(R.id.action_sort_asc), list.isAscending());
//
        super.onPrepareOptionsMenu(menu);
//
    }

    @Override
    public void onPause() {
        super.onPause();
        if (deleteConfirmationDialog != null) {
            deleteConfirmationDialog.dismiss();
        }
    }
}

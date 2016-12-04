package nutritiondb.ben.db2.fragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.shamanland.fonticon.FontIconDrawable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.Portion;
import nutritiondb.ben.db2.views.adapters.DetailedDataRecyclerAdapter;

/**
 * Created by benebsworth on 3/07/16.
 */
public class DetailedFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private final String TAG = getClass().getSimpleName();
    private MenuItem mBookmark;
//    private MenuItem mFullscreen;
    private Drawable icBookmark;
    private Drawable icBookmarkO;
//    private Drawable icFullscreen;
    private String NDB_no = "";
    private FoodProfile foodProfile;
    private Spinner servingSelector;
    private RecyclerView recyclerView;
    private DetailedDataRecyclerAdapter mAdapter;
    private View detailedTabView;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        icBookmark = FontIconDrawable.inflate(activity, R.xml.ic_actionbar_bookmark);
        icBookmarkO = FontIconDrawable.inflate(activity, R.xml.ic_actionbar_bookmark_o);
//        icFullscreen = FontIconDrawable.inflate(activity, R.xml.ic_actionbar_fullscreen);
        setHasOptionsMenu(true);
    }


    private void displayBookmarked(boolean value) {
        if (mBookmark == null) {
            return;
        }
        if (value) {
            mBookmark.setChecked(true);
            mBookmark.setIcon(icBookmark);
        } else {
            mBookmark.setChecked(false);
            mBookmark.setIcon(icBookmarkO);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.article, menu);
        mBookmark = menu.findItem(R.id.action_bookmark_article);
//        mFullscreen = menu.findItem(R.id.action_fullscreen);
//        if (Build.VERSION.SDK_INT < 19) {
//            mFullscreen.setVisible(false);
//            mFullscreen.setEnabled(false);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG,"onOptionsItemSelected");
        int itemId = item.getItemId();
        Log.i(TAG,"onOptionsItemSelected:"+item.toString());
//        if (itemId == R.id.action_find_in_page) {
//            view.showFindDialog(null, true);
//            return true;
//        }
        if (itemId == R.id.action_bookmark_article) {
            Application app = (Application)getActivity().getApplication();
            if (this.NDB_no != null) {
                if (item.isChecked()) {
                    app.removeBookmark(this.NDB_no);
                    displayBookmarked(false);
                } else {
                    app.addBookmark(this.foodProfile);
                    displayBookmarked(true);
                }
            }
            return true;
        }
//        if (itemId == R.id.action_fullscreen) {
//            ((FoodProfileActivity)getActivity()).toggleFullScreen();
//            return true;
//        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (this.NDB_no == null) {
            mBookmark.setVisible(false);
        }
        else {
            Application app = (Application) getActivity().getApplication();
            try {
                boolean bookmarked = app.isBookmarked(this.NDB_no);
                Log.d(getTag(), String.format("is %s bookmarked? %s", this.NDB_no, bookmarked));
                displayBookmarked(bookmarked);

            } catch (Exception e) {
                mBookmark.setVisible(false);
                Log.d(getTag(), "Invalid item for bookmarking: " + this.NDB_no, e);
            }
        }

//        mFullscreen.setIcon(icFullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView:creatingview+initialising");
        Bundle args = getArguments();
        this.foodProfile  = (FoodProfile) args.getSerializable("profile");
        this.NDB_no = args.isEmpty() ? null : args.getString("id");
        if (NDB_no == null) {
            Log.i(TAG,"onCreateView:NDB_no:null");
            if (this.foodProfile == null) {
                Log.i(TAG,"onCreateView:foodProfile:null");
            }
            else {
                Log.i(TAG,"onCreateView:foodProfile:"+foodProfile.getNutrients().toString());
            }
            View layout = inflater.inflate(R.layout.empty_view, container, false);
            return layout;
        }
        Log.i(TAG,"onCreateView:NDB_no:"+this.NDB_no);
        if (this.foodProfile == null) {
            Log.i(TAG,"onCreateView:foodProfile:null");
        }
        else {
            Log.i(TAG,"onCreateView:foodProfile:"+foodProfile.getNutrients().toString());
        }
        detailedTabView = inflater.inflate(R.layout.detailed_fragment_view, container, false);


        recyclerView = (RecyclerView) detailedTabView.findViewById(R.id.detailed_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        build_table2((Portion) getSpinner().getSelectedItem());
        initialise_portions_detailed();
//        mainExpList = (ExpandableListView) detailedTabView.findViewById(R.id.energy_expandablelist_view);
//        mainExpList.setGroupIndicator(null);
//recyclerView = (RecyclerView) summaryTabView.findViewById(R.id.factSheetListView);

//        build_table(detailedTabView, null);
//
//        mainExpList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//
//                if (ExpandableCollection.key_value!=null) {
//                    if (ExpandableCollection.key_value.size()>groupPosition) {
//                        String key=ExpandableCollection.key_value.get(groupPosition);
//
//                        if (ExpandableCollection.expandable_hashmap.size()>0) {
//                            ExpandableCollection obj_exp = ExpandableCollection.expandable_hashmap
//                                    .get(key).get(childPosition);
//
//
//                        }}}
//                return false;}
//        });
        //TODO: generate portions + table for summary card
        Log.i(TAG,"onCreateView:done");

        return detailedTabView;



    }

    public void build_table2(Portion portion) {

        mAdapter = new DetailedDataRecyclerAdapter(foodProfile.getScaledProfile(portion));
        recyclerView.setAdapter(mAdapter);

    }


    public Spinner getSpinner() {
        MaterialViewPager mviewPager = (MaterialViewPager) getActivity().findViewById(R.id.materialViewPager);
        View rootView = mviewPager.getRootView();
        return (Spinner) rootView.findViewById(R.id.servingSelector);
    }
    public void initialise_portions_detailed() {

        MaterialViewPager mviewPager = (MaterialViewPager) getActivity().findViewById(R.id.materialViewPager);
        View rootView = mviewPager.getRootView();
        servingSelector = (Spinner) rootView.findViewById(R.id.servingSelector);
        servingSelector.setOnItemSelectedListener(this);

    }
    public void build_portions() {
        Log.i(TAG,"build_portions:started build");
        HashMap<String, Portion> portions = foodProfile.getPortions();

        MaterialViewPager mviewPager = (MaterialViewPager) getActivity().findViewById(R.id.materialViewPager);
        View rootView = mviewPager.getRootView();
        servingSelector = (Spinner) rootView.findViewById(R.id.servingSelector);
        ArrayList<Portion> servings = new ArrayList<>();
        servings.add(new Portion("100g", "100", "1"));
//        servings.add(new Portion("cup","250ml","1"));
//        servings.add(new Portion("Ounce","28","1"));

        for(Map.Entry<String, Portion> portion : portions.entrySet()) {
            System.out.println(portion.getKey()+" : "+portion.getValue().getPortion().toString());
            servings.add(portion.getValue());
        }
        servingSelector.setOnItemSelectedListener(this);
        ArrayAdapter<Portion> servingAdapter = new ArrayAdapter<Portion>(getContext(), android.R.layout.simple_spinner_item, servings);
        servingSelector.setAdapter(servingAdapter);
        Log.i(TAG,"build_portions:completed build:added "+servings.size()+" items");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Portion portion =  (Portion) parent.getSelectedItem();
        System.out.println("selected: "+portion.toString());
        System.out.println("passing: "+portion.getPortion().toString());
        build_table2(portion);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

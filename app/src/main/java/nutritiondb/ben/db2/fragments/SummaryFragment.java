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
import android.widget.ListView;
import android.widget.Spinner;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.shamanland.fonticon.FontIconDrawable;

import java.util.HashMap;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.Nutrient;
import nutritiondb.ben.db2.models.NutritionalData;
import nutritiondb.ben.db2.models.Portion;
import nutritiondb.ben.db2.views.adapters.SummaryDataRecyclerAdapter;

/**
 * Created by benebsworth on 3/07/16.
 */
public class SummaryFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private final String TAG = getClass().getSimpleName();
    private MenuItem  mBookmark;
//    private MenuItem mFullscreen;
    private Drawable icBookmark;
    private Drawable icBookmarkO;
//    private Drawable icFullscreen;
    private String NDB_no = "";
    private FoodProfile foodProfile;
    View summaryTabView;
    private RecyclerView recyclerView;
    private SummaryDataRecyclerAdapter mAdapter;
    private Spinner servingSelector;
//    private List<NutritionalData> nutData = new ArrayList<>();
    ListView listView1;

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
        int itemId = item.getItemId();
//        if (itemId == R.id.action_find_in_page) {
//            view.showFindDialog(null, true);
//            return true;
//        }
        if (itemId == R.id.action_bookmark_article) {
            Application app = (Application) getActivity().getApplication();
            if (this.NDB_no != null) {
                if (item.isChecked()) {
                    app.removeBookmark(this.NDB_no);
                    app.bookmarks.notifyDataSetChanged();
                    displayBookmarked(false);
                } else {
                    app.addBookmark(this.foodProfile);
                    app.bookmarks.notifyDataSetChanged();
                    displayBookmarked(true);
                }
            }
            return true;
        }
//        if (itemId == R.id.action_fullscreen) {
//            Log.i(TAG,"onOptionsItemSelected:fullscreen clicked");
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
            //TODO: generate empty fragments layout
        }
        Log.i(TAG,"onCreateView:NDB_no:"+this.NDB_no);
        if (this.foodProfile == null) {
            Log.i(TAG,"onCreateView:foodProfile:null");
        }
        else {
            Log.i(TAG,"onCreateView:foodProfile:"+foodProfile.getNutrients().toString());
        }
        summaryTabView = inflater.inflate(R.layout.summary_fragment_view, container, false);
        //TODO: generate portions + table for summary card

        recyclerView = (RecyclerView) summaryTabView.findViewById(R.id.factSheetListView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        build_table((Portion) getSpinner().getSelectedItem());
//        MaterialViewPager mviewPager =
        initialise_portions();
        Log.i(TAG,"onCreateView:done");
        return summaryTabView;



    }
    public Spinner getSpinner() {
        MaterialViewPager mviewPager = (MaterialViewPager) getActivity().findViewById(R.id.materialViewPager);
        View rootView = mviewPager.getRootView();
        return (Spinner) rootView.findViewById(R.id.servingSelector);
    }


    public void build_table(Portion portion) {
        Log.i(TAG,"build_table:started build");
        System.out.print("portion:" +portion.toString());
        HashMap<String, Nutrient> nutrients = foodProfile.getNutrients(portion);
//
        NutritionalData nutDatanew[]  = new NutritionalData[]
                {
                        new NutritionalData("", "Units", "", R.layout.list_item_row_header),
                        new NutritionalData("Energy", "kCal", nutrients.get("ENERC_KCAL").getValue(), R.layout.list_item_row_header),
                        new NutritionalData("", "kJ", nutrients.get("ENERC_KJ").getValue(), R.layout.list_item_row),
                        new NutritionalData("Carbohydrates Total", "g", nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getValue() : "~", R.layout.list_item_row_header),
                        new NutritionalData("Sugars", "g", nutrients.containsKey("SUGAR") ? nutrients.get("SUGAR").getValue() : "~", R.layout.list_item_row_sub),
                        new NutritionalData("Fibre", "g", nutrients.containsKey("FIBTG") ? nutrients.get("FIBTG").getValue() : "~", R.layout.list_item_row_sub),
                        new NutritionalData("Protein", "g", nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getValue() : "~", R.layout.list_item_row),
                        new NutritionalData("Fat", "g", nutrients.containsKey("FAT") ? nutrients.get("FAT").getValue() : "~", R.layout.list_item_row),
                        new NutritionalData("Sodium", "mg", nutrients.containsKey("NA") ? nutrients.get("NA").getValue() : "~", R.layout.list_item_row),
                        new NutritionalData("Potassium", "mg", nutrients.containsKey("K") ? nutrients.get("K").getValue() : "~", R.layout.list_item_row),
                        new NutritionalData("Vitamin C", "mg", nutrients.containsKey("VITC") ? nutrients.get("VITC").getValue() : "~", R.layout.list_item_row),
                        new NutritionalData("Vitamin D", "mg", nutrients.containsKey("VITD") ? nutrients.get("VITD").getValue() : "~", R.layout.list_item_row),


                };
        mAdapter = new SummaryDataRecyclerAdapter(nutDatanew);
//        recyclerView = (RecyclerView) summaryTabView.findViewById(R.id.factSheetListView);
        recyclerView.setAdapter(mAdapter);
        Log.i(TAG,"build_table:build completed:added "+nutDatanew.length+" items");



    }

    public void initialise_portions() {
        MaterialViewPager mviewPager = (MaterialViewPager) getActivity().findViewById(R.id.materialViewPager);
        View rootView = mviewPager.getRootView();
        servingSelector = (Spinner) rootView.findViewById(R.id.servingSelector);
        servingSelector.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Portion portion =  (Portion) parent.getSelectedItem();
        System.out.println("selected: "+portion.toString());
        System.out.println("passing: "+portion.getPortion().toString());
        build_table(portion);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //TODO: copy over summary fragment
}

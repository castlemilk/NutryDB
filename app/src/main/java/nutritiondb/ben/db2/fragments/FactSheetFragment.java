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
import android.widget.Spinner;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.shamanland.fonticon.FontIconDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.FoodProfileActivity;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.Nutrient;
import nutritiondb.ben.db2.models.Portion;
import nutritiondb.ben.db2.views.adapters.ExpandableListAdapter;
import nutritiondb.ben.db2.views.adapters.SummaryDataRecyclerAdapter;

/** Fragment response for generating the nutrition fact sheet display. Will route usage of
 *  different adapters based on summary/detailed tabs. It is initialised in
 *  ProfilePagerAdapter
 *  TODO: Improve error/exception handling to display a graceful error fragment
 *  TODO: Enable routing based on specific nutritional fact selection.
 *  TODO: Save expansion state
 */
public class FactSheetFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    public RecyclerView factList;
    private final int SUMMARY_TAB = 0;
    private final int DETAILED_TAB = 1;
    private MenuItem mBookmark;
    private Drawable icBookmark;
    private Drawable icBookmarkO;
    private String NDB_no = "";

    public static FactSheetFragment newInstance(String name, FoodProfile foodProfile) {

        FactSheetFragment factSheetFragment = new FactSheetFragment();
        Bundle args  = new Bundle();
        args.putString("name", name);
        args.putSerializable("foodprofile", foodProfile);

        return factSheetFragment;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        icBookmark = FontIconDrawable.inflate(activity, R.xml.ic_actionbar_bookmark);
        icBookmarkO = FontIconDrawable.inflate(activity, R.xml.ic_actionbar_bookmark_o);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.article, menu);
        mBookmark = menu.findItem(R.id.action_bookmark_article);
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (this.NDB_no == null) {
            mBookmark.setVisible(false);
        }
        else {
            Application app = (Application) getActivity().getApplication();
            try {
                boolean bookmarked = app.isBookmarked(getFoodProfile().NDB_no);
                Log.d(getTag(), String.format("is %s bookmarked? %s", this.NDB_no, bookmarked));
                displayBookmarked(bookmarked);

            } catch (Exception e) {
                mBookmark.setVisible(false);
                Log.d(getTag(), "Invalid item for bookmarking: " + this.NDB_no, e);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_bookmark_article) {
            Application app = (Application) getActivity().getApplication();
            if (this.NDB_no != null) {
                if (item.isChecked()) {
                    app.removeBookmark(getFoodProfile().NDB_no);
                    app.bookmarks.notifyDataSetChanged();
                    displayBookmarked(false);
                } else {
                    app.addBookmark(getFoodProfile());
                    app.bookmarks.notifyDataSetChanged();
                    displayBookmarked(true);
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    public String getFragmentName() {
        return getArguments().getString("name");
    }
    public FoodProfile getFoodProfile() {
            if (getArguments() != null) {
                return (FoodProfile) getArguments().getSerializable("profile");
            }
            else {
                return ((FoodProfileActivity) getActivity()).mfoodProfile;
            }
    }
    public int getPosition() {
        return getArguments().getInt("position");
    }
    public Spinner getSpinner() {
        MaterialViewPager mviewPager = (MaterialViewPager) getActivity().findViewById(R.id.materialViewPager);
        View rootView = mviewPager.getRootView();
        return (Spinner) rootView.findViewById(R.id.servingSelector);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView:creating fragment view");
        if (container == null) {
            return null;
        }
        if (getFoodProfile() == null) {
            View layout = inflater.inflate(R.layout.empty_view, container, false);
            return layout;
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        switch (getPosition()) {
            case SUMMARY_TAB:
                View summaryLayout = inflater.inflate(R.layout.summary_fragment_view, container, false);
                factList = (RecyclerView) summaryLayout.findViewById(R.id.factSheetListView);

                factList.setLayoutManager(mLayoutManager);
                factList.setHasFixedSize(true);
                factList.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                build_summary_table(getFoodProfile(), (Portion) getSpinner().getSelectedItem());
                return factList;
            case DETAILED_TAB:
                View detailedLayout = inflater.inflate(R.layout.detailed_fragment_view, container, false);
                factList = (RecyclerView) detailedLayout.findViewById(R.id.detailed_recyclerview);
                factList.setLayoutManager(mLayoutManager);
                factList.setHasFixedSize(true);
                factList.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                build_detailed_table(getFoodProfile(), (Portion) getSpinner().getSelectedItem());
                return factList;
            default:
                View defaultLayout = inflater.inflate(R.layout.summary_fragment_view, container, false);
                factList = (RecyclerView) defaultLayout.findViewById(R.id.factSheetListView);

                factList.setLayoutManager(mLayoutManager);
                factList.setHasFixedSize(true);
                factList.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                build_summary_table(getFoodProfile(), (Portion) getSpinner().getSelectedItem());
                return factList;



        }


    }

    public void build_summary_table(FoodProfile foodProfile, Portion portion) {
        Log.i(TAG,"build_table:started build");
        System.out.print("portion:" +portion.toString());
        SummaryDataRecyclerAdapter mAdapter = new SummaryDataRecyclerAdapter(foodProfile);
        factList.setAdapter(mAdapter);
        Log.i(TAG,"build_table:build completed:added "+mAdapter.getItemCount()+" items");

    }
    public void build_detailed_table(FoodProfile foodProfile, Portion portion) {
        Log.i(TAG, "build_table:started build");
        System.out.print("portion:" + portion.toString());
        HashMap<String, Nutrient> nutrients = foodProfile.getNutrients(portion);

        factList.setAdapter(new ExpandableListAdapter(data));
    }




}

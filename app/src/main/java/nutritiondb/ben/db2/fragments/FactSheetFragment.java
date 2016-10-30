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
import nutritiondb.ben.db2.models.NutritionalData;
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
        HashMap<String, Nutrient> nutrients = foodProfile.getNutrients(portion);
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
        SummaryDataRecyclerAdapter mAdapter = new SummaryDataRecyclerAdapter(nutDatanew);

        factList.setAdapter(mAdapter);
        Log.i(TAG,"build_table:build completed:added "+nutDatanew.length+" items");

    }
    public void build_detailed_table(FoodProfile foodProfile, Portion portion) {
        Log.i(TAG, "build_table:started build");
        System.out.print("portion:" + portion.toString());
        HashMap<String, Nutrient> nutrients = foodProfile.getNutrients(portion);
        List<ExpandableListAdapter.Item> data = new ArrayList<>();
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " ", "Units", " "));
        //Energy
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Energy"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " ", "kCal",
                nutrients.get("ENERC_KCAL").getValue()));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " ", "kJ",
                nutrients.get("ENERC_KJ").getValue()));

        //Fat
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Fats & Fatty Acids"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Total Fat",
                nutrients.containsKey("FAT") ? nutrients.get("FAT").getUnits() : "g",
                nutrients.containsKey("FAT") ? nutrients.get("FAT").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Saturated Fat",
                nutrients.containsKey("FATSAT") ? nutrients.get("FATSAT").getUnits() : "g",
                nutrients.containsKey("FATSAT") ? nutrients.get("FATSAT").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Monounsaturated Fat",
                nutrients.containsKey("FAMS") ? nutrients.get("FAMS").getUnits() : "g",
                nutrients.containsKey("FAMS") ? nutrients.get("FAMS").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "  Polyunsaturated Fat",
                nutrients.containsKey("FAPU") ? nutrients.get("FAPU").getUnits() : "g",
                nutrients.containsKey("FAPU") ? nutrients.get("FAPU").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Transaturated Fat",
                nutrients.containsKey("FATRN") ? nutrients.get("FATRN").getUnits() : "g",
                nutrients.containsKey("FATRN") ? nutrients.get("FATRN").getValue() : "~"));
        Double EPA_value = nutrients.containsKey("F20D5") ? nutrients.get("F20D5").getValueD() : 0.00;
        Double DHA_value = nutrients.containsKey("F22D6") ? nutrients.get("F22D6").getValueD() : 0.00;
        Double ALA_value = nutrients.containsKey("F18D3CN3") ? nutrients.get("F18D3CN3").getValueD() : 0.00;
        Double total_omega = EPA_value + ALA_value + DHA_value;
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Total Omega-3 Fatty Acid",
                "mg",
                String.valueOf(total_omega)));
        //Carbohydrates
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Carbohydrates"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Total Carbohydrates",
                nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getUnits() : "g",
                nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "  Dietry Fibre",
                nutrients.containsKey("FIBTG") ? nutrients.get("FIBTG").getUnits() : "g",
                nutrients.containsKey("FIBTG") ? nutrients.get("FIBTG").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "  Sugars",
                nutrients.containsKey("SUGAR") ? nutrients.get("SUGAR").getUnits() : "g",
                nutrients.containsKey("SUGAR") ? nutrients.get("SUGAR").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "      Glucose",
                nutrients.containsKey("GLUS") ? nutrients.get("GLUS").getUnits() : "mg",
                nutrients.containsKey("GLUS") ? nutrients.get("GLUS").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "      Sucrose",
                nutrients.containsKey("SUCS") ? nutrients.get("SUCS").getUnits() : "mg",
                nutrients.containsKey("SUCS") ? nutrients.get("SUCS").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "      Maltose",
                nutrients.containsKey("MALS") ? nutrients.get("MALS").getUnits() : "mg",
                nutrients.containsKey("MALS") ? nutrients.get("MALS").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "      Lactose",
                nutrients.containsKey("LACS") ? nutrients.get("LACS").getUnits() : "mg",
                nutrients.containsKey("LACS") ? nutrients.get("LACS").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "      Galactose",
                nutrients.containsKey("GALS") ? nutrients.get("GALS").getUnits() : "mg",
                nutrients.containsKey("GALS") ? nutrients.get("GALS").getValue() : "~"));
        //Protein
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Protein"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Protein",
                nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getUnits() : "g",
                nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Tryptophan",
                nutrients.containsKey("TRP_G") ? nutrients.get("TRP_G").getUnits() : "mg",
                nutrients.containsKey("TRP_G") ? nutrients.get("TRP_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Threonine",
                nutrients.containsKey("THR_G") ? nutrients.get("THR_G").getUnits() : "mg",
                nutrients.containsKey("THR_G") ? nutrients.get("THR_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Isoleucine",
                nutrients.containsKey("ILE_G") ? nutrients.get("ILE_G").getUnits() : "mg",
                nutrients.containsKey("ILE_G") ? nutrients.get("ILE_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Leucine",
                nutrients.containsKey("LEU_G") ? nutrients.get("LEU_G").getUnits() : "mg",
                nutrients.containsKey("LEU_G") ? nutrients.get("LEU_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Lysine",
                nutrients.containsKey("LYS_G") ? nutrients.get("LYS_G").getUnits() : "mg",
                nutrients.containsKey("LYS_G") ? nutrients.get("LYS_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Methionine",
                nutrients.containsKey("MET_G") ? nutrients.get("MET_G").getUnits() : "mg",
                nutrients.containsKey("MET_G") ? nutrients.get("MET_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Cystine",
                nutrients.containsKey("CYS_G") ? nutrients.get("CYS_G").getUnits() : "mg",
                nutrients.containsKey("CYS_G") ? nutrients.get("CYS_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Phenylalanine",
                nutrients.containsKey("PHE_G") ? nutrients.get("PHE_G").getUnits() : "mg",
                nutrients.containsKey("PHE_G") ? nutrients.get("PHE_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Tyrosine",
                nutrients.containsKey("TYR_G") ? nutrients.get("TYR_G").getUnits() : "mg",
                nutrients.containsKey("TYR_G") ? nutrients.get("TYR_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Valine",
                nutrients.containsKey("VAL_G") ? nutrients.get("VAL_G").getUnits() : "mg",
                nutrients.containsKey("VAL_G") ? nutrients.get("VAL_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Arginine",
                nutrients.containsKey("ARG_G") ? nutrients.get("ARG_G").getUnits() : "mg",
                nutrients.containsKey("ARG_G") ? nutrients.get("ARG_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Histidine",
                nutrients.containsKey("HISTN_G") ? nutrients.get("HISTN_G").getUnits() : "mg",
                nutrients.containsKey("HISTN_G") ? nutrients.get("HISTN_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Alanine",
                nutrients.containsKey("ALA_G") ? nutrients.get("ALA_G").getUnits() : "mg",
                nutrients.containsKey("ALA_G") ? nutrients.get("ALA_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Aspartic",
                nutrients.containsKey("ASP_G") ? nutrients.get("ASP_G").getUnits() : "mg",
                nutrients.containsKey("ASP_G") ? nutrients.get("ASP_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Glutamic",
                nutrients.containsKey("GLU_G") ? nutrients.get("GLU_G").getUnits() : "mg",
                nutrients.containsKey("GLU_G") ? nutrients.get("GLU_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Proline",
                nutrients.containsKey("PRO_G") ? nutrients.get("PRO_G").getUnits() : "mg",
                nutrients.containsKey("PRO_G") ? nutrients.get("PRO_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Serine",
                nutrients.containsKey("SER_G") ? nutrients.get("SER_G").getUnits() : "mg",
                nutrients.containsKey("SER_G") ? nutrients.get("SER_G").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Hyroxyprofile",
                nutrients.containsKey("HYP") ? nutrients.get("HYP").getUnits() : "mg",
                nutrients.containsKey("HYP") ? nutrients.get("HYP").getValue() : "~"));
        //Minerals
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Minerals"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Calcium",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnits() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Iron",
                nutrients.containsKey("FE") ? nutrients.get("FE").getUnits() : "mg",
                nutrients.containsKey("FE") ? nutrients.get("FE").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Magnesium",
                nutrients.containsKey("MG") ? nutrients.get("MG").getUnits() : "mg",
                nutrients.containsKey("MG") ? nutrients.get("MG").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Phosphorus",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnits() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Potassium",
                nutrients.containsKey("K") ? nutrients.get("K").getUnits() : "mg",
                nutrients.containsKey("K") ? nutrients.get("K").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Sodium",
                nutrients.containsKey("NA") ? nutrients.get("NA").getUnits() : "mg",
                nutrients.containsKey("NA") ? nutrients.get("NA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Zinc",
                nutrients.containsKey("ZN") ? nutrients.get("ZN").getUnits() : "mg",
                nutrients.containsKey("ZN") ? nutrients.get("ZN").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Copper",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnits() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Manganese",
                nutrients.containsKey("CU") ? nutrients.get("CU").getUnits() : "mg",
                nutrients.containsKey("CU") ? nutrients.get("CU").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Selenium",
                nutrients.containsKey("SE") ? nutrients.get("SE").getUnits() : "mg",
                nutrients.containsKey("SE") ? nutrients.get("SE").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Flouride",
                nutrients.containsKey("FLD") ? nutrients.get("FLD").getUnits() : "mg",
                nutrients.containsKey("FLD") ? nutrients.get("FLD").getValue() : "~"));
        //Vitamins
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Vitamins"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Vitamin C",
                nutrients.containsKey("VITC") ? nutrients.get("VITC").getUnits() : "mg",
                nutrients.containsKey("VITC") ? nutrients.get("VITC").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Vitamin A",
                nutrients.containsKey("VITA_IU") ? nutrients.get("VITA_IU").getUnits() : "IU",
                nutrients.containsKey("VITA_IU") ? nutrients.get("VITA_IU").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "    Retinol",
                nutrients.containsKey("RETOL") ? nutrients.get("RETOL").getUnits() : "mcg",
                nutrients.containsKey("RETOL") ? nutrients.get("RETOL").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "    Retinol Activity Equivalent (RAE)",
                nutrients.containsKey("VITA_RAE") ? nutrients.get("VITA_RAE").getUnits() : "mcg",
                nutrients.containsKey("VITA_RAE") ? nutrients.get("VITA_RAE").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "    Alpha Carotene",
                nutrients.containsKey("CARTA") ? nutrients.get("CARTA").getUnits() : "mcg",
                nutrients.containsKey("CARTA") ? nutrients.get("CARTA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "    Beta Carotene",
                nutrients.containsKey("CARTB") ? nutrients.get("CARTB").getUnits() : "mg",
                nutrients.containsKey("CARTB") ? nutrients.get("CARTB").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "    Beta Cryptoxathin",
                nutrients.containsKey("CRYPX") ? nutrients.get("CRYPX").getUnits() : "mg",
                nutrients.containsKey("CRYPX") ? nutrients.get("CRYPX").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "    Lycopene",
                nutrients.containsKey("LYCPN") ? nutrients.get("LYCPN").getUnits() : "mg",
                nutrients.containsKey("LYCPN") ? nutrients.get("LYCPN").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "    Lutein + Zeaxanthin",
                nutrients.containsKey("LUT+ZEA") ? nutrients.get("LUT+ZEA").getUnits() : "mg",
                nutrients.containsKey("LUT+ZEA") ? nutrients.get("LUT+ZEA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Vitamin D",
                nutrients.containsKey("VITD") ? nutrients.get("VITD").getUnits() : "mg",
                nutrients.containsKey("VITD") ? nutrients.get("VITD").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Vitamin E (Alpha-Tocopherol)",
                nutrients.containsKey("TOCPHA") ? nutrients.get("TOCPHA").getUnits() : "mg",
                nutrients.containsKey("TOCPHA") ? nutrients.get("TOCPHA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Beta Trocopherol",
                nutrients.containsKey("TOCPHB") ? nutrients.get("TOCPHB").getUnits() : "mg",
                nutrients.containsKey("TOCPHB") ? nutrients.get("TOCPHB").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "   Delta Tocopherol",
                nutrients.containsKey("TOCPHD") ? nutrients.get("TOCPHD").getUnits() : "mg",
                nutrients.containsKey("TOCPHD") ? nutrients.get("TOCPHD").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Vitamin K",
                nutrients.containsKey("VITK") ? nutrients.get("VITK").getUnits() : "mcg",
                nutrients.containsKey("VITK") ? nutrients.get("VITK").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Thiamin",
                nutrients.containsKey("THIA") ? nutrients.get("THIA").getUnits() : "mg",
                nutrients.containsKey("THIA") ? nutrients.get("THIA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Riboflavin",
                nutrients.containsKey("RIBF") ? nutrients.get("RIBF").getUnits() : "mg",
                nutrients.containsKey("RIBF") ? nutrients.get("RIBF").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Niacin",
                nutrients.containsKey("NIA") ? nutrients.get("NIA").getUnits() : "mg",
                nutrients.containsKey("NIA") ? nutrients.get("NIA").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Vitamin B6",
                nutrients.containsKey("SE") ? nutrients.get("SE").getUnits() : "mg",
                nutrients.containsKey("SE") ? nutrients.get("SE").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Folate",
                nutrients.containsKey("FOL") ? nutrients.get("FOL").getUnits() : "mcg",
                nutrients.containsKey("FOL") ? nutrients.get("FOL").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Food Folate",
                nutrients.containsKey("FOLFD") ? nutrients.get("FOLFD").getUnits() : "mcg",
                nutrients.containsKey("FOLFD") ? nutrients.get("FOLFD").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Folic Acid",
                nutrients.containsKey("FOLAC") ? nutrients.get("FOLAC").getUnits() : "mcg",
                nutrients.containsKey("FOLAC") ? nutrients.get("FOLAC").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Dietry Folate Equivalents",
                nutrients.containsKey("FOLDFE") ? nutrients.get("FOLDFE").getUnits() : "mcg",
                nutrients.containsKey("FOLDFE") ? nutrients.get("FOLDFE").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Niacin",
                nutrients.containsKey("VITAB6A") ? nutrients.get("VITAB6A").getUnits() : "mg",
                nutrients.containsKey("VITAB6A") ? nutrients.get("VITAB6A").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Pantothenic Acid",
                nutrients.containsKey("PANTAC") ? nutrients.get("PANTAC").getUnits() : "mg",
                nutrients.containsKey("PANTAC") ? nutrients.get("PANTAC").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Choline",
                nutrients.containsKey("CHOLN") ? nutrients.get("CHOLN").getUnits() : "mg",
                nutrients.containsKey("CHOLN") ? nutrients.get("CHOLN").getValue() : "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Betaine",
                nutrients.containsKey("BETN") ? nutrients.get("BETN").getUnits() : "mg",
                nutrients.containsKey("BETN") ? nutrients.get("BETN").getValue() : "~"));
        factList.setAdapter(new ExpandableListAdapter(data));
    }




}

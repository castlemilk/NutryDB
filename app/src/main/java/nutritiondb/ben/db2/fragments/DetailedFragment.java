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
import java.util.List;
import java.util.Map;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.Nutrient;
import nutritiondb.ben.db2.models.Portion;
import nutritiondb.ben.db2.views.adapters.ExpandableListAdapter;

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
    private ExpandableListAdapter mAdapter;
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



        final HashMap<String, Nutrient> nutrients = foodProfile.getNutrients(portion);
        System.out.print("energy:"+nutrients.get("ENERC_KCAL").getValue());
        List<ExpandableListAdapter.Item> data = new ArrayList<>();
        //Energy
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Energy"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " ", "kCal",
                nutrients.get("ENERC_KCAL").getValue()));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, " ", "kJ",
                nutrients.get("ENERC_KJ").getValue()));

        //Fat
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Fats & Fatty Acids"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Total Fat",
                nutrients.containsKey("FAT") ? nutrients.get("FAT").getUnit() : "g",
                nutrients.containsKey("FAT") ? nutrients.get("FAT").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Saturated Fat",
                nutrients.containsKey("FATSAT") ? nutrients.get("FATSAT").getUnit() : "g",
                nutrients.containsKey("FATSAT") ? nutrients.get("FATSAT").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Monounsaturated Fat",
                nutrients.containsKey("FAMS") ? nutrients.get("FAMS").getUnit() : "g",
                nutrients.containsKey("FAMS") ? nutrients.get("FAMS").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "  Polyunsaturated Fat",
                nutrients.containsKey("FAPU") ? nutrients.get("FAPU").getUnit() : "g",
                nutrients.containsKey("FAPU") ? nutrients.get("FAPU").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Transaturated Fat",
                nutrients.containsKey("FATRN") ? nutrients.get("FATRN").getUnit() : "g",
                nutrients.containsKey("FATRN") ? nutrients.get("FATRN").getValue(): "~"));
        Double EPA_value = nutrients.containsKey("F20D5") ? nutrients.get("F20D5").getValueD() : 0.00;
        Double DHA_value = nutrients.containsKey("F22D6") ? nutrients.get("F22D6").getValueD() : 0.00;
        Double ALA_value = nutrients.containsKey("F18D3CN3") ? nutrients.get("F18D3CN3").getValueD() : 0.00;
        Double total_omega = EPA_value+ALA_value+DHA_value;
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Total Omega-3 Fatty Acid",
                "mg",
                String.valueOf(total_omega)));
        //Carbohydrates
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Carbohydrates"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Total Carbohydrates",
                nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getUnit() : "g",
                nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "  Dietry Fibre",
                nutrients.containsKey("FIBTG") ? nutrients.get("FIBTG").getUnit() : "g",
                nutrients.containsKey("FIBTG") ? nutrients.get("FIBTG").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "  Sugars",
                nutrients.containsKey("SUGAR") ? nutrients.get("SUGAR").getUnit() : "g",
                nutrients.containsKey("SUGAR") ? nutrients.get("SUGAR").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "      Glucose",
                nutrients.containsKey("GLUS") ? nutrients.get("GLUS").getUnit() : "mg",
                nutrients.containsKey("GLUS") ? nutrients.get("GLUS").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "      Sucrose",
                nutrients.containsKey("SUCS") ? nutrients.get("SUCS").getUnit() : "mg",
                nutrients.containsKey("SUCS") ? nutrients.get("SUCS").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "      Maltose",
                nutrients.containsKey("MALS") ? nutrients.get("MALS").getUnit() : "mg",
                nutrients.containsKey("MALS") ? nutrients.get("MALS").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "      Lactose",
                nutrients.containsKey("LACS") ? nutrients.get("LACS").getUnit() : "mg",
                nutrients.containsKey("LACS") ? nutrients.get("LACS").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "      Galactose",
                nutrients.containsKey("GALS") ? nutrients.get("GALS").getUnit() : "mg",
                nutrients.containsKey("GALS") ? nutrients.get("GALS").getValue(): "~"));
        //Protein
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Protein"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Protein",
                nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getUnit() : "g",
                nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Tryptophan",
                nutrients.containsKey("TRP_G") ? nutrients.get("TRP_G").getUnit() : "mg",
                nutrients.containsKey("TRP_G") ? nutrients.get("TRP_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Threonine",
                nutrients.containsKey("THR_G") ? nutrients.get("THR_G").getUnit() : "mg",
                nutrients.containsKey("THR_G") ? nutrients.get("THR_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Isoleucine",
                nutrients.containsKey("ILE_G") ? nutrients.get("ILE_G").getUnit() : "mg",
                nutrients.containsKey("ILE_G") ? nutrients.get("ILE_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Leucine",
                nutrients.containsKey("LEU_G") ? nutrients.get("LEU_G").getUnit() : "mg",
                nutrients.containsKey("LEU_G") ? nutrients.get("LEU_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Lysine",
                nutrients.containsKey("LYS_G") ? nutrients.get("LYS_G").getUnit() : "mg",
                nutrients.containsKey("LYS_G") ? nutrients.get("LYS_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Methionine",
                nutrients.containsKey("MET_G") ? nutrients.get("MET_G").getUnit() : "mg",
                nutrients.containsKey("MET_G") ? nutrients.get("MET_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Cystine",
                nutrients.containsKey("CYS_G") ? nutrients.get("CYS_G").getUnit() : "mg",
                nutrients.containsKey("CYS_G") ? nutrients.get("CYS_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Phenylalanine",
                nutrients.containsKey("PHE_G") ? nutrients.get("PHE_G").getUnit() : "mg",
                nutrients.containsKey("PHE_G") ? nutrients.get("PHE_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Tyrosine",
                nutrients.containsKey("TYR_G") ? nutrients.get("TYR_G").getUnit() : "mg",
                nutrients.containsKey("TYR_G") ? nutrients.get("TYR_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Valine",
                nutrients.containsKey("VAL_G") ? nutrients.get("VAL_G").getUnit() : "mg",
                nutrients.containsKey("VAL_G") ? nutrients.get("VAL_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Arginine",
                nutrients.containsKey("ARG_G") ? nutrients.get("ARG_G").getUnit() : "mg",
                nutrients.containsKey("ARG_G") ? nutrients.get("ARG_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Histidine",
                nutrients.containsKey("HISTN_G") ? nutrients.get("HISTN_G").getUnit() : "mg",
                nutrients.containsKey("HISTN_G") ? nutrients.get("HISTN_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Alanine",
                nutrients.containsKey("ALA_G") ? nutrients.get("ALA_G").getUnit() : "mg",
                nutrients.containsKey("ALA_G") ? nutrients.get("ALA_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Aspartic",
                nutrients.containsKey("ASP_G") ? nutrients.get("ASP_G").getUnit() : "mg",
                nutrients.containsKey("ASP_G") ? nutrients.get("ASP_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Glutamic",
                nutrients.containsKey("GLU_G") ? nutrients.get("GLU_G").getUnit() : "mg",
                nutrients.containsKey("GLU_G") ? nutrients.get("GLU_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Proline",
                nutrients.containsKey("PRO_G") ? nutrients.get("PRO_G").getUnit() : "mg",
                nutrients.containsKey("PRO_G") ? nutrients.get("PRO_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Serine",
                nutrients.containsKey("SER_G") ? nutrients.get("SER_G").getUnit() : "mg",
                nutrients.containsKey("SER_G") ? nutrients.get("SER_G").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Hyroxyprofile",
                nutrients.containsKey("HYP") ? nutrients.get("HYP").getUnit() : "mg",
                nutrients.containsKey("HYP") ? nutrients.get("HYP").getValue(): "~"));
        //Minerals
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Minerals"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Calcium",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnit() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Iron",
                nutrients.containsKey("FE") ? nutrients.get("FE").getUnit() : "mg",
                nutrients.containsKey("FE") ? nutrients.get("FE").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Magnesium",
                nutrients.containsKey("MG") ? nutrients.get("MG").getUnit() : "mg",
                nutrients.containsKey("MG") ? nutrients.get("MG").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Phosphorus",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnit() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Potassium",
                nutrients.containsKey("K") ? nutrients.get("K").getUnit() : "mg",
                nutrients.containsKey("K") ? nutrients.get("K").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Sodium",
                nutrients.containsKey("NA") ? nutrients.get("NA").getUnit() : "mg",
                nutrients.containsKey("NA") ? nutrients.get("NA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Zinc",
                nutrients.containsKey("ZN") ? nutrients.get("ZN").getUnit() : "mg",
                nutrients.containsKey("ZN") ? nutrients.get("ZN").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Copper",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnit() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Manganese",
                nutrients.containsKey("CU") ? nutrients.get("CU").getUnit() : "mg",
                nutrients.containsKey("CU") ? nutrients.get("CU").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Selenium",
                nutrients.containsKey("SE") ? nutrients.get("SE").getUnit() : "mg",
                nutrients.containsKey("SE") ? nutrients.get("SE").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Flouride",
                nutrients.containsKey("FLD") ? nutrients.get("FLD").getUnit() : "mg",
                nutrients.containsKey("FLD") ? nutrients.get("FLD").getValue(): "~"));
        //Vitamins
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Vitamins"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Vitamin C",
                nutrients.containsKey("VITC") ? nutrients.get("VITC").getUnit() : "mg",
                nutrients.containsKey("VITC") ? nutrients.get("VITC").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Vitamin A",
                nutrients.containsKey("VITA_IU") ? nutrients.get("VITA_IU").getUnit() : "IU",
                nutrients.containsKey("VITA_IU") ? nutrients.get("VITA_IU").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "    Retinol",
                nutrients.containsKey("RETOL") ? nutrients.get("RETOL").getUnit() : "mcg",
                nutrients.containsKey("RETOL") ? nutrients.get("RETOL").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "    Retinol Activity Equivalent (RAE)",
                nutrients.containsKey("VITA_RAE") ? nutrients.get("VITA_RAE").getUnit() : "mcg",
                nutrients.containsKey("VITA_RAE") ? nutrients.get("VITA_RAE").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "    Alpha Carotene",
                nutrients.containsKey("CARTA") ? nutrients.get("CARTA").getUnit() : "mcg",
                nutrients.containsKey("CARTA") ? nutrients.get("CARTA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "    Beta Carotene",
                nutrients.containsKey("CARTB") ? nutrients.get("CARTB").getUnit() : "mg",
                nutrients.containsKey("CARTB") ? nutrients.get("CARTB").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "    Beta Cryptoxathin",
                nutrients.containsKey("CRYPX") ? nutrients.get("CRYPX").getUnit() : "mg",
                nutrients.containsKey("CRYPX") ? nutrients.get("CRYPX").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "    Lycopene",
                nutrients.containsKey("LYCPN") ? nutrients.get("LYCPN").getUnit() : "mg",
                nutrients.containsKey("LYCPN") ? nutrients.get("LYCPN").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "    Lutein + Zeaxanthin",
                nutrients.containsKey("LUT+ZEA") ? nutrients.get("LUT+ZEA").getUnit() : "mg",
                nutrients.containsKey("LUT+ZEA") ? nutrients.get("LUT+ZEA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Vitamin D",
                nutrients.containsKey("VITD") ? nutrients.get("VITD").getUnit() : "mg",
                nutrients.containsKey("VITD") ? nutrients.get("VITD").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Vitamin E (Alpha-Tocopherol)",
                nutrients.containsKey("TOCPHA") ? nutrients.get("TOCPHA").getUnit() : "mg",
                nutrients.containsKey("TOCPHA") ? nutrients.get("TOCPHA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Beta Trocopherol",
                nutrients.containsKey("TOCPHB") ? nutrients.get("TOCPHB").getUnit() : "mg",
                nutrients.containsKey("TOCPHB") ? nutrients.get("TOCPHB").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "   Delta Tocopherol",
                nutrients.containsKey("TOCPHD") ? nutrients.get("TOCPHD").getUnit() : "mg",
                nutrients.containsKey("TOCPHD") ? nutrients.get("TOCPHD").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Vitamin K",
                nutrients.containsKey("VITK") ? nutrients.get("VITK").getUnit() : "mcg",
                nutrients.containsKey("VITK") ? nutrients.get("VITK").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Thiamin",
                nutrients.containsKey("THIA") ? nutrients.get("THIA").getUnit() : "mg",
                nutrients.containsKey("THIA") ? nutrients.get("THIA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Riboflavin",
                nutrients.containsKey("RIBF") ? nutrients.get("RIBF").getUnit() : "mg",
                nutrients.containsKey("RIBF") ? nutrients.get("RIBF").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Niacin",
                nutrients.containsKey("NIA") ? nutrients.get("NIA").getUnit() : "mg",
                nutrients.containsKey("NIA") ? nutrients.get("NIA").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Vitamin B6",
                nutrients.containsKey("SE") ? nutrients.get("SE").getUnit() : "mg",
                nutrients.containsKey("SE") ? nutrients.get("SE").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Folate",
                nutrients.containsKey("FOL") ? nutrients.get("FOL").getUnit() : "mcg",
                nutrients.containsKey("FOL") ? nutrients.get("FOL").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Food Folate",
                nutrients.containsKey("FOLFD") ? nutrients.get("FOLFD").getUnit() : "mcg",
                nutrients.containsKey("FOLFD") ? nutrients.get("FOLFD").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Folic Acid",
                nutrients.containsKey("FOLAC") ? nutrients.get("FOLAC").getUnit() : "mcg",
                nutrients.containsKey("FOLAC") ? nutrients.get("FOLAC").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Dietry Folate Equivalents",
                nutrients.containsKey("FOLDFE") ? nutrients.get("FOLDFE").getUnit() : "mcg",
                nutrients.containsKey("FOLDFE") ? nutrients.get("FOLDFE").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Niacin",
                nutrients.containsKey("VITAB6A") ? nutrients.get("VITAB6A").getUnit() : "mg",
                nutrients.containsKey("VITAB6A") ? nutrients.get("VITAB6A").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Pantothenic Acid",
                nutrients.containsKey("PANTAC") ? nutrients.get("PANTAC").getUnit() : "mg",
                nutrients.containsKey("PANTAC") ? nutrients.get("PANTAC").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Choline",
                nutrients.containsKey("CHOLN") ? nutrients.get("CHOLN").getUnit() : "mg",
                nutrients.containsKey("CHOLN") ? nutrients.get("CHOLN").getValue(): "~"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,  "Betaine",
                nutrients.containsKey("BETN") ? nutrients.get("BETN").getUnit() : "mg",
                nutrients.containsKey("BETN") ? nutrients.get("BETN").getValue(): "~"));

        mAdapter = new ExpandableListAdapter(data);
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
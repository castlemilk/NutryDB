package nutritiondb.ben.db2.views.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.NutrientRow;

/**
 * Created by benebsworth on 25/07/16.
 */
public class DetailedDataRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    public static final int CHILD_CHILD = 2;

    private List<ExpandableListAdapter.Item> data;

    public DetailedDataRecyclerAdapter(FoodProfile foodProfile) {
        List<NutrientRow> data = new ArrayList<>();
        data.add(new NutrientRow(" ", "Units", " ", ExpandableListAdapter.CHILD));
        //Energy
        data.add(new NutrientRow("Energy", ExpandableListAdapter.HEADER));
        data.add(new NutrientRow(" ", foodProfile.getNutrient("ENERC_KCAL"),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow(" ", foodProfile.getNutrient("ENERC_KJ"),
                ExpandableListAdapter.CHILD));
        //Fat
        data.add(new NutrientRow("Fats & Fatty Acids", ExpandableListAdapter.HEADER));
        data.add(new NutrientRow("Total Fat", foodProfile.getNutrient("FAT"),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Saturated Fat", foodProfile.getNutrient("FATSAT"),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Monounsaturated Fat", foodProfile.getNutrient("FAMS"),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("  Polyunsaturated Fat", foodProfile.getNutrient("FAPU"),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Transaturated Fat", foodProfile.getNutrient("FATRN"),
                ExpandableListAdapter.CHILD));
        //TODO add method to carry out this function or implement the aggregation of values in DB
        Double EPA_value = foodProfile.getNutrient("F20D5").getValueD();
        Double DHA_value = foodProfile.getNutrient("F22D6").getValueD();
        Double ALA_value = foodProfile.getNutrient("F18D3CN3").getValueD();
        Double total_omega = EPA_value + ALA_value + DHA_value;
        data.add(new NutrientRow("Total Omega-3 Fatty Acid", "mg", String.valueOf(total_omega),
                ExpandableListAdapter.CHILD));
        //Carbohydrates
        data.add(new NutrientRow(ExpandableListAdapter.HEADER, "Carbohydrates"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Total Carbohydrates",
                nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getUnits() : "g",
                nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "  Dietry Fibre",
                nutrients.containsKey("FIBTG") ? nutrients.get("FIBTG").getUnits() : "g",
                nutrients.containsKey("FIBTG") ? nutrients.get("FIBTG").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "  Sugars",
                nutrients.containsKey("SUGAR") ? nutrients.get("SUGAR").getUnits() : "g",
                nutrients.containsKey("SUGAR") ? nutrients.get("SUGAR").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "      Glucose",
                nutrients.containsKey("GLUS") ? nutrients.get("GLUS").getUnits() : "mg",
                nutrients.containsKey("GLUS") ? nutrients.get("GLUS").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "      Sucrose",
                nutrients.containsKey("SUCS") ? nutrients.get("SUCS").getUnits() : "mg",
                nutrients.containsKey("SUCS") ? nutrients.get("SUCS").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "      Maltose",
                nutrients.containsKey("MALS") ? nutrients.get("MALS").getUnits() : "mg",
                nutrients.containsKey("MALS") ? nutrients.get("MALS").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "      Lactose",
                nutrients.containsKey("LACS") ? nutrients.get("LACS").getUnits() : "mg",
                nutrients.containsKey("LACS") ? nutrients.get("LACS").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "      Galactose",
                nutrients.containsKey("GALS") ? nutrients.get("GALS").getUnits() : "mg",
                nutrients.containsKey("GALS") ? nutrients.get("GALS").getValue() : "~"));
        //Protein
        data.add(new NutrientRow(ExpandableListAdapter.HEADER, "Protein"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Protein",
                nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getUnits() : "g",
                nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Tryptophan",
                nutrients.containsKey("TRP_G") ? nutrients.get("TRP_G").getUnits() : "mg",
                nutrients.containsKey("TRP_G") ? nutrients.get("TRP_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Threonine",
                nutrients.containsKey("THR_G") ? nutrients.get("THR_G").getUnits() : "mg",
                nutrients.containsKey("THR_G") ? nutrients.get("THR_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Isoleucine",
                nutrients.containsKey("ILE_G") ? nutrients.get("ILE_G").getUnits() : "mg",
                nutrients.containsKey("ILE_G") ? nutrients.get("ILE_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Leucine",
                nutrients.containsKey("LEU_G") ? nutrients.get("LEU_G").getUnits() : "mg",
                nutrients.containsKey("LEU_G") ? nutrients.get("LEU_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Lysine",
                nutrients.containsKey("LYS_G") ? nutrients.get("LYS_G").getUnits() : "mg",
                nutrients.containsKey("LYS_G") ? nutrients.get("LYS_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Methionine",
                nutrients.containsKey("MET_G") ? nutrients.get("MET_G").getUnits() : "mg",
                nutrients.containsKey("MET_G") ? nutrients.get("MET_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Cystine",
                nutrients.containsKey("CYS_G") ? nutrients.get("CYS_G").getUnits() : "mg",
                nutrients.containsKey("CYS_G") ? nutrients.get("CYS_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Phenylalanine",
                nutrients.containsKey("PHE_G") ? nutrients.get("PHE_G").getUnits() : "mg",
                nutrients.containsKey("PHE_G") ? nutrients.get("PHE_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Tyrosine",
                nutrients.containsKey("TYR_G") ? nutrients.get("TYR_G").getUnits() : "mg",
                nutrients.containsKey("TYR_G") ? nutrients.get("TYR_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Valine",
                nutrients.containsKey("VAL_G") ? nutrients.get("VAL_G").getUnits() : "mg",
                nutrients.containsKey("VAL_G") ? nutrients.get("VAL_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Arginine",
                nutrients.containsKey("ARG_G") ? nutrients.get("ARG_G").getUnits() : "mg",
                nutrients.containsKey("ARG_G") ? nutrients.get("ARG_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Histidine",
                nutrients.containsKey("HISTN_G") ? nutrients.get("HISTN_G").getUnits() : "mg",
                nutrients.containsKey("HISTN_G") ? nutrients.get("HISTN_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Alanine",
                nutrients.containsKey("ALA_G") ? nutrients.get("ALA_G").getUnits() : "mg",
                nutrients.containsKey("ALA_G") ? nutrients.get("ALA_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Aspartic",
                nutrients.containsKey("ASP_G") ? nutrients.get("ASP_G").getUnits() : "mg",
                nutrients.containsKey("ASP_G") ? nutrients.get("ASP_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Glutamic",
                nutrients.containsKey("GLU_G") ? nutrients.get("GLU_G").getUnits() : "mg",
                nutrients.containsKey("GLU_G") ? nutrients.get("GLU_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Proline",
                nutrients.containsKey("PRO_G") ? nutrients.get("PRO_G").getUnits() : "mg",
                nutrients.containsKey("PRO_G") ? nutrients.get("PRO_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Serine",
                nutrients.containsKey("SER_G") ? nutrients.get("SER_G").getUnits() : "mg",
                nutrients.containsKey("SER_G") ? nutrients.get("SER_G").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Hyroxyprofile",
                nutrients.containsKey("HYP") ? nutrients.get("HYP").getUnits() : "mg",
                nutrients.containsKey("HYP") ? nutrients.get("HYP").getValue() : "~"));
        //Minerals
        data.add(new NutrientRow(ExpandableListAdapter.HEADER, "Minerals"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Calcium",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnits() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Iron",
                nutrients.containsKey("FE") ? nutrients.get("FE").getUnits() : "mg",
                nutrients.containsKey("FE") ? nutrients.get("FE").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Magnesium",
                nutrients.containsKey("MG") ? nutrients.get("MG").getUnits() : "mg",
                nutrients.containsKey("MG") ? nutrients.get("MG").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Phosphorus",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnits() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Potassium",
                nutrients.containsKey("K") ? nutrients.get("K").getUnits() : "mg",
                nutrients.containsKey("K") ? nutrients.get("K").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Sodium",
                nutrients.containsKey("NA") ? nutrients.get("NA").getUnits() : "mg",
                nutrients.containsKey("NA") ? nutrients.get("NA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Zinc",
                nutrients.containsKey("ZN") ? nutrients.get("ZN").getUnits() : "mg",
                nutrients.containsKey("ZN") ? nutrients.get("ZN").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Copper",
                nutrients.containsKey("CA") ? nutrients.get("CA").getUnits() : "mg",
                nutrients.containsKey("CA") ? nutrients.get("CA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Manganese",
                nutrients.containsKey("CU") ? nutrients.get("CU").getUnits() : "mg",
                nutrients.containsKey("CU") ? nutrients.get("CU").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Selenium",
                nutrients.containsKey("SE") ? nutrients.get("SE").getUnits() : "mg",
                nutrients.containsKey("SE") ? nutrients.get("SE").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Flouride",
                nutrients.containsKey("FLD") ? nutrients.get("FLD").getUnits() : "mg",
                nutrients.containsKey("FLD") ? nutrients.get("FLD").getValue() : "~"));
        //Vitamins
        data.add(new NutrientRow(ExpandableListAdapter.HEADER, "Vitamins"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Vitamin C",
                nutrients.containsKey("VITC") ? nutrients.get("VITC").getUnits() : "mg",
                nutrients.containsKey("VITC") ? nutrients.get("VITC").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Vitamin A",
                nutrients.containsKey("VITA_IU") ? nutrients.get("VITA_IU").getUnits() : "IU",
                nutrients.containsKey("VITA_IU") ? nutrients.get("VITA_IU").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "    Retinol",
                nutrients.containsKey("RETOL") ? nutrients.get("RETOL").getUnits() : "mcg",
                nutrients.containsKey("RETOL") ? nutrients.get("RETOL").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "    Retinol Activity Equivalent (RAE)",
                nutrients.containsKey("VITA_RAE") ? nutrients.get("VITA_RAE").getUnits() : "mcg",
                nutrients.containsKey("VITA_RAE") ? nutrients.get("VITA_RAE").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "    Alpha Carotene",
                nutrients.containsKey("CARTA") ? nutrients.get("CARTA").getUnits() : "mcg",
                nutrients.containsKey("CARTA") ? nutrients.get("CARTA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "    Beta Carotene",
                nutrients.containsKey("CARTB") ? nutrients.get("CARTB").getUnits() : "mg",
                nutrients.containsKey("CARTB") ? nutrients.get("CARTB").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "    Beta Cryptoxathin",
                nutrients.containsKey("CRYPX") ? nutrients.get("CRYPX").getUnits() : "mg",
                nutrients.containsKey("CRYPX") ? nutrients.get("CRYPX").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "    Lycopene",
                nutrients.containsKey("LYCPN") ? nutrients.get("LYCPN").getUnits() : "mg",
                nutrients.containsKey("LYCPN") ? nutrients.get("LYCPN").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "    Lutein + Zeaxanthin",
                nutrients.containsKey("LUT+ZEA") ? nutrients.get("LUT+ZEA").getUnits() : "mg",
                nutrients.containsKey("LUT+ZEA") ? nutrients.get("LUT+ZEA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Vitamin D",
                nutrients.containsKey("VITD") ? nutrients.get("VITD").getUnits() : "mg",
                nutrients.containsKey("VITD") ? nutrients.get("VITD").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Vitamin E (Alpha-Tocopherol)",
                nutrients.containsKey("TOCPHA") ? nutrients.get("TOCPHA").getUnits() : "mg",
                nutrients.containsKey("TOCPHA") ? nutrients.get("TOCPHA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Beta Trocopherol",
                nutrients.containsKey("TOCPHB") ? nutrients.get("TOCPHB").getUnits() : "mg",
                nutrients.containsKey("TOCPHB") ? nutrients.get("TOCPHB").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "   Delta Tocopherol",
                nutrients.containsKey("TOCPHD") ? nutrients.get("TOCPHD").getUnits() : "mg",
                nutrients.containsKey("TOCPHD") ? nutrients.get("TOCPHD").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Vitamin K",
                nutrients.containsKey("VITK") ? nutrients.get("VITK").getUnits() : "mcg",
                nutrients.containsKey("VITK") ? nutrients.get("VITK").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Thiamin",
                nutrients.containsKey("THIA") ? nutrients.get("THIA").getUnits() : "mg",
                nutrients.containsKey("THIA") ? nutrients.get("THIA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Riboflavin",
                nutrients.containsKey("RIBF") ? nutrients.get("RIBF").getUnits() : "mg",
                nutrients.containsKey("RIBF") ? nutrients.get("RIBF").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Niacin",
                nutrients.containsKey("NIA") ? nutrients.get("NIA").getUnits() : "mg",
                nutrients.containsKey("NIA") ? nutrients.get("NIA").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Vitamin B6",
                nutrients.containsKey("SE") ? nutrients.get("SE").getUnits() : "mg",
                nutrients.containsKey("SE") ? nutrients.get("SE").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Folate",
                nutrients.containsKey("FOL") ? nutrients.get("FOL").getUnits() : "mcg",
                nutrients.containsKey("FOL") ? nutrients.get("FOL").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Food Folate",
                nutrients.containsKey("FOLFD") ? nutrients.get("FOLFD").getUnits() : "mcg",
                nutrients.containsKey("FOLFD") ? nutrients.get("FOLFD").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Folic Acid",
                nutrients.containsKey("FOLAC") ? nutrients.get("FOLAC").getUnits() : "mcg",
                nutrients.containsKey("FOLAC") ? nutrients.get("FOLAC").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Dietry Folate Equivalents",
                nutrients.containsKey("FOLDFE") ? nutrients.get("FOLDFE").getUnits() : "mcg",
                nutrients.containsKey("FOLDFE") ? nutrients.get("FOLDFE").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Niacin",
                nutrients.containsKey("VITAB6A") ? nutrients.get("VITAB6A").getUnits() : "mg",
                nutrients.containsKey("VITAB6A") ? nutrients.get("VITAB6A").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Pantothenic Acid",
                nutrients.containsKey("PANTAC") ? nutrients.get("PANTAC").getUnits() : "mg",
                nutrients.containsKey("PANTAC") ? nutrients.get("PANTAC").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Choline",
                nutrients.containsKey("CHOLN") ? nutrients.get("CHOLN").getUnits() : "mg",
                nutrients.containsKey("CHOLN") ? nutrients.get("CHOLN").getValue() : "~"));
        data.add(new NutrientRow(ExpandableListAdapter.CHILD, "Betaine",
                nutrients.containsKey("BETN") ? nutrients.get("BETN").getUnits() : "mg",
                nutrients.containsKey("BETN") ? nutrients.get("BETN").getValue() : "~"));

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case HEADER:
                view = inflater.inflate(R.layout.expended_header, parent, false);
                ExpandableListAdapter.ListHeaderViewHolder header = new ExpandableListAdapter.ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = inflater.inflate(R.layout.detailed_child_row_layout_parent, parent, false);
                ExpandableListAdapter.ListChildViewHolder child = new ExpandableListAdapter.ListChildViewHolder(view);
                return child;
            case CHILD_CHILD:
                //TODO:
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ExpandableListAdapter.Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ExpandableListAdapter.ListHeaderViewHolder itemController = (ExpandableListAdapter.ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.name.setText(item.name);
                if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.ic_remove_black_24dp);
                } else {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.ic_add_black_24dp);
                }
                itemController.parent_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<ExpandableListAdapter.Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.ic_add_black_24dp);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (ExpandableListAdapter.Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.ic_remove_black_24dp);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                final ExpandableListAdapter.ListChildViewHolder childController = (ExpandableListAdapter.ListChildViewHolder) holder;
                childController.name.setText(data.get(position).name);
                childController.unit.setText(data.get(position).unit);
                childController.value.setText(data.get(position).value);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView btn_expand_toggle;
        public CardView parent_card;

        public ExpandableListAdapter.Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_header);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.iv_exp);
            parent_card = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView unit;
        public TextView value;

        public ExpandableListAdapter.Item refferalItem;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.detailedChildRowNameTextView);
            unit = (TextView) itemView.findViewById(R.id.detailedChildRowUnitsTextView);
            value = (TextView) itemView.findViewById(R.id.detailedChildRowValueTextView);

        }
    }


    public static class Item {
        public int type;
        public String name;
        public String unit;
        public String value;
        public List<ExpandableListAdapter.Item> invisibleChildren;

        public Item(int type, String name) {
            //header input
            this.type = type;
            this.name = name;
        }

        public Item(String name, String unit, String value, Integer type) {
            //child input
            this.type = type;
            this.name = name;
            this.unit = unit;
            this.value = value;
        }
    }
}



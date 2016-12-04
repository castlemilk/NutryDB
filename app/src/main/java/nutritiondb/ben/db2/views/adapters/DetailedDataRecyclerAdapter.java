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
    List<NutrientRow> data = new ArrayList<>();

    public DetailedDataRecyclerAdapter(FoodProfile foodProfile) {

        data.add(new NutrientRow(" ", "Units", " ", ExpandableListAdapter.CHILD));
        //Energy
        data.add(new NutrientRow("Energy", ExpandableListAdapter.HEADER));
        data.add(new NutrientRow(" ", foodProfile.getEnergyKCal(),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow(" ", foodProfile.getEnergyKJ(),
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
        data.add(new NutrientRow("Carbohydrates",ExpandableListAdapter.HEADER));
        data.add(new NutrientRow("Total Carbohydrates", foodProfile.getNutrient("CHOCDF"),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("  Dietry Fibre", foodProfile.getNutrient("FIBTG"),
                ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("  Sugars",
                foodProfile.getNutrient("SUGAR"),ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("      Glucose",
                foodProfile.getNutrient("GLUS"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("      Sucrose",
                foodProfile.getNutrient("SUCS"),ExpandableListAdapter.CHILD ));
        data.add(new NutrientRow("      Maltose",
                foodProfile.getNutrient("MALS"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("      Lactose",
                foodProfile.getNutrient("LACS"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("      Galactose",
                foodProfile.getNutrient("GALS"), ExpandableListAdapter.CHILD));
        //Protein
        data.add(new NutrientRow("Protein", ExpandableListAdapter.HEADER));
        data.add(new NutrientRow("Protein",
                foodProfile.getNutrient("PROCNT"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Tryptophan",
                foodProfile.getNutrient("TRP_G"),ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Threonine",
                foodProfile.getNutrient("THR_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Isoleucine",
                foodProfile.getNutrient("ILE_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Leucine",
                foodProfile.getNutrient("LEU_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Lysine",
                foodProfile.getNutrient("LYS_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Methionine",
                foodProfile.getNutrient("MET_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Cystine",
                foodProfile.getNutrient("CYS_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Phenylalanine",
                foodProfile.getNutrient("PHE_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Tyrosine",
                foodProfile.getNutrient("TYR_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Valine",
                foodProfile.getNutrient("VAL_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Arginine",
                foodProfile.getNutrient("ARG_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Histidine",
                foodProfile.getNutrient("HISTN_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Alanine",
                foodProfile.getNutrient("ALA_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Aspartic",
                foodProfile.getNutrient("ASP_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Glutamic",
                foodProfile.getNutrient("GLU_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Proline",
                foodProfile.getNutrient("PRO_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Serine",
                foodProfile.getNutrient("SER_G"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Hyroxyprofile",
                foodProfile.getNutrient("HYP"), ExpandableListAdapter.CHILD));
        //Minerals
        data.add(new NutrientRow("Minerals", ExpandableListAdapter.HEADER));
        data.add(new NutrientRow("Calcium",
                foodProfile.getNutrient("CA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Iron",
                foodProfile.getNutrient("FE"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Magnesium",
                foodProfile.getNutrient("MG"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Phosphorus",
                foodProfile.getNutrient("CA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Potassium",
                foodProfile.getNutrient("K"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Sodium",
                foodProfile.getNutrient("NA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Zinc",
                foodProfile.getNutrient("ZN"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Copper",
                foodProfile.getNutrient("CA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Manganese",
                foodProfile.getNutrient("CU"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Selenium",
                foodProfile.getNutrient("SE"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Flouride",
                foodProfile.getNutrient("FLD"), ExpandableListAdapter.CHILD));
        //Vitamins
        data.add(new NutrientRow("Vitamins", ExpandableListAdapter.HEADER));
        data.add(new NutrientRow("Vitamin C",
                foodProfile.getNutrient("VITC"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Vitamin A",
                foodProfile.getNutrient("VITA_IU"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("    Retinol",
                foodProfile.getNutrient("RETOL"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("    Retinol Activity Equivalent (RAE)",
                foodProfile.getNutrient("VITA_RAE"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("    Alpha Carotene",
                foodProfile.getNutrient("CARTA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("    Beta Carotene",
                foodProfile.getNutrient("CARTB"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("    Beta Cryptoxathin",
                foodProfile.getNutrient("CRYPX"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("    Lycopene",
                foodProfile.getNutrient("LYCPN"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("    Lutein + Zeaxanthin",
                foodProfile.getNutrient("LUT+ZEA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Vitamin D",
                foodProfile.getNutrient("VITD"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Vitamin E (Alpha-Tocopherol)",
                foodProfile.getNutrient("TOCPHA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Beta Trocopherol",
                foodProfile.getNutrient("TOCPHB"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("   Delta Tocopherol",
                foodProfile.getNutrient("TOCPHD"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Vitamin K",
                foodProfile.getNutrient("VITK"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Thiamin",
                foodProfile.getNutrient("THIA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Riboflavin",
                foodProfile.getNutrient("RIBF"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Niacin",
                foodProfile.getNutrient("NIA"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Vitamin B6",
                foodProfile.getNutrient("SE"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Folate",
                foodProfile.getNutrient("FOL"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Food Folate",
                foodProfile.getNutrient("FOLFD"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Folic Acid",
                foodProfile.getNutrient("FOLAC"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Dietry Folate Equivalents",
                foodProfile.getNutrient("FOLDFE"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Niacin",
                foodProfile.getNutrient("VITAB6A"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Pantothenic Acid",
                foodProfile.getNutrient("PANTAC"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Choline",
                foodProfile.getNutrient("CHOLN"), ExpandableListAdapter.CHILD));
        data.add(new NutrientRow("Betaine",
                foodProfile.getNutrient("BETN"), ExpandableListAdapter.CHILD));

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
        final NutrientRow item = data.get(position);
        switch (item.row_type) {
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
                            item.invisibleChildren = new ArrayList<NutrientRow>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).row_type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.ic_add_black_24dp);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (NutrientRow i : item.invisibleChildren) {
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
                childController.unit.setText(data.get(position).units);
                childController.value.setText(data.get(position).value);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).row_type;

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



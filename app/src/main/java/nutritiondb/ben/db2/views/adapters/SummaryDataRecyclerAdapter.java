package nutritiondb.ben.db2.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.NutrientRow;

/**
 * Created by benebsworth on 22/07/16.
 * Adapter for generating the summary view card. Does static labelling for the nutrient row names.
 * This can make use of the default AbbreviationMapping class to lookup field names if needed.
 */
public class SummaryDataRecyclerAdapter extends RecyclerView.Adapter<SummaryDataRecyclerAdapter.ViewHolder> {
    private NutrientRow[] summaryNutrients;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nutrient, unit, value;
        public ViewHolder(View v) {
            super(v);
            nutrient = (TextView) v.findViewById(R.id.summaryListNameTextView);
            unit = (TextView) v.findViewById(R.id.summaryListUnitsTextView);
            value = (TextView) v.findViewById(R.id.summaryListValueTextView);
        }
    }

    public SummaryDataRecyclerAdapter(FoodProfile foodProfile) {

        summaryNutrients  = new NutrientRow[]
                {
                        new NutrientRow("", "Units", "", R.layout.list_item_row_header),
                        new NutrientRow("Energy", foodProfile.getNutrient("ENERC"), R.layout.list_item_row_header),
                        new NutrientRow("", foodProfile.getNutrient("ENERC_KJ"), R.layout.list_item_row),
                        new NutrientRow("Carbohydrates Total", foodProfile.getNutrient("CHOCDF"), R.layout.list_item_row_header),
                        new NutrientRow("Sugars", "g", foodProfile.getNutrient("SUGAR"), R.layout.list_item_row_sub),
                        new NutrientRow("Fibre", "g", foodProfile.getNutrient("FIBTG"), R.layout.list_item_row_sub),
                        new NutrientRow("Protein", "g", foodProfile.getNutrient("PROCNT") , R.layout.list_item_row),
                        new NutrientRow("Fat", "g", foodProfile.getNutrient("FAT"), R.layout.list_item_row),
                        new NutrientRow("Sodium", "mg", foodProfile.getNutrient("NA"), R.layout.list_item_row),
                        new NutrientRow("Potassium", "mg", foodProfile.getNutrient("K"), R.layout.list_item_row),
                        new NutrientRow("Vitamin C", "mg", foodProfile.getNutrient("VITC"), R.layout.list_item_row),
                        new NutrientRow("Vitamin D", "mg", foodProfile.getNutrient("VITD"), R.layout.list_item_row),


                };
    }

    @Override
    public SummaryDataRecyclerAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public int getItemViewType(int position) {
        switch (summaryNutrients[position].row_type) {
            case R.layout.list_item_row_header:
                return R.layout.list_item_row_header;
            case R.layout.list_item_row:
                return R.layout.list_item_row;
            case R.layout.list_item_row_sub:
                return R.layout.list_item_row_sub;
        }
        return summaryNutrients[position].row_type;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NutrientRow mNutrientRow = summaryNutrients[position];
        holder.nutrient.setText(mNutrientRow.name);
        holder.unit.setText(mNutrientRow.units);
        holder.value.setText(mNutrientRow.value);

    }

    @Override
    public int getItemCount() {
        return summaryNutrients.length;
    }
}



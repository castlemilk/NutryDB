package nutritiondb.ben.db2.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.NutritionalData;

/**
 * Created by benebsworth on 22/07/16.
 */
public class SummaryDataRecyclerAdapter extends RecyclerView.Adapter<SummaryDataRecyclerAdapter.ViewHolder> {
    NutritionalData[] mDataset;
//
//    static final int TYPE_HEADER = 0;
//    static final int TYPE_STANDARD = 1;
//    static final int TYPE_SUB  =2;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nutrient, unit, value;
        public ViewHolder(View v) {
            super(v);
            nutrient = (TextView) v.findViewById(R.id.summaryListNameTextView);
            unit = (TextView) v.findViewById(R.id.summaryListUnitsTextView);
            value = (TextView) v.findViewById(R.id.summaryListValueTextView);
        }
    }

    public SummaryDataRecyclerAdapter(NutritionalData[] data) {
        mDataset = data;
    }

    @Override
    public SummaryDataRecyclerAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public int getItemViewType(int position) {
        switch (mDataset[position].row_type) {
            case R.layout.list_item_row_header:
                return R.layout.list_item_row_header;
            case R.layout.list_item_row:
                return R.layout.list_item_row;
            case R.layout.list_item_row_sub:
                return R.layout.list_item_row_sub;
        }
        return mDataset[position].row_type;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NutritionalData nutdata = mDataset[position];
        holder.nutrient.setText(nutdata.name);
        holder.unit.setText(nutdata.units);
        holder.value.setText(nutdata.value);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

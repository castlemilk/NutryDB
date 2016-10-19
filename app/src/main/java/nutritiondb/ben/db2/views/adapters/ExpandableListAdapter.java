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

/**
 * Created by benebsworth on 25/07/16.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    public static final int CHILD_CHILD = 2;

    private List<Item> data;
    public ExpandableListAdapter(List<Item> data) {
        this.data = data;
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
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = inflater.inflate(R.layout.detailed_child_row_layout_parent, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
            case CHILD_CHILD:
                //TODO:
        }
        return null;
        }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
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
                            item.invisibleChildren = new ArrayList<Item>();
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
                            for (Item i : item.invisibleChildren) {
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
                final ListChildViewHolder childController = (ListChildViewHolder) holder;
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

        public Item refferalItem;

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

        public Item refferalItem;

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
        public List<Item> invisibleChildren;

        public Item(int type, String name) {
            //header input
            this.type = type;
            this.name = name;
        }

        public Item(int type, String name, String unit, String value) {
            //child input
            this.type = type;
            this.name = name;
            this.unit = unit;
            this.value = value;
        }
    }
}

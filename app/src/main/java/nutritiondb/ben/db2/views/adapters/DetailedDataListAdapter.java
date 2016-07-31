package nutritiondb.ben.db2.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import nutritiondb.ben.db2.R;
import nutritiondb.ben.db2.models.ExpandableCollection;

/**
 * Created by benebsworth on 2/06/16.
 */

//Takes our data and blackbox handles the interactions with the expandable list view.
public class DetailedDataListAdapter extends BaseExpandableListAdapter {


    // pre-define varialbles. not sure if this is technically required or a colloqial design pattern
    // Note that the pre-defined variables will conform the the initial formatting present in the
    // packer class ExpandableCollection

    Context ctx;
    Activity act;
    ArrayList<String> headerCollectionArr;
    HashMap<String, ArrayList<ExpandableCollection>> holderHash;

    // initialisation function where the dependant variables are passed. We initialise with the
    // following variables:
    //  - Context (can be thought of as the relative view/activity)
    //  - Activity, the relevent activity. Links to ExpandabileListActivity
    //  - data packs for populating the expandableListView.
    public DetailedDataListAdapter(Context ctx, Activity act,
                                   ArrayList<String> headerCollectionArr,
                                   HashMap<String, ArrayList<ExpandableCollection>> holderHash) {
        this.ctx = ctx;
        this.headerCollectionArr = headerCollectionArr;
        this.holderHash = holderHash;
        this.act=act;
    }

    // This will get the child object from using the group position which can be thought of as the
    // parent tab. It then will access the child position.
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        String st=headerCollectionArr.get(groupPosition);

        Object obj=null;
        if (holderHash.size()>0) {
            obj	=holderHash.get(st).get(childPosititon);
        }

        return obj;
    }
    // Returns the 'id' of the child. but this seems to just be mapped to the position of the child.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // on getChildView, the child item will be greated and formatted according to the defined layout.
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // note the key here is how the expandable collection has been packed. need to implement a
        // similar datastructure to enable the same functionality.
        ExpandableCollection exp_obj =  (ExpandableCollection) getChild(groupPosition, childPosition);

        ChildViewHolder viewHolder=new ChildViewHolder();
        if (convertView == null) {
            // What does the inflater do?
            LayoutInflater infalInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.detailed_child_row_layout_parent, null);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.detailedChildRowNameTextView);

            viewHolder.units = (TextView) convertView
                    .findViewById(R.id.detailedChildRowUnitsTextView);

            viewHolder.value = (TextView) convertView
                    .findViewById(R.id.detailedChildRowValueTextView);

            convertView.setTag(viewHolder);


        }else
        {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }


        viewHolder.name.setText(exp_obj.name);
        viewHolder.units.setText(exp_obj.units);
        viewHolder.value.setText(exp_obj.value);

        return convertView;
    }
    // Returns the number of children nested in a group position
    @Override
    public int getChildrenCount(int groupPosition) {
        String st=headerCollectionArr.get(groupPosition);
        int i = 0;
        if (holderHash.size()>0) {
            ArrayList<ExpandableCollection> ll=holderHash.get(st);
            if (ll!=null) {
                i=ll.size();
            }else{

                i=0;
            }

        }
        return i;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerCollectionArr.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return headerCollectionArr.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // on getGroupView the parent card will be defined..
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);

        // Not that we use the overarching context here to initialise the inflater.
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expended_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tv_header);
        ImageView image = (ImageView) convertView.findViewById(R.id.iv_exp);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);



        int imageResourceId = isExpanded ? R.drawable.ic_remove_black_24dp : R.drawable.ic_add_black_24dp;
        image.setImageResource(imageResourceId);

        image.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public class ChildViewHolder {

        TextView name;
        TextView units;
        TextView value;


    }



}




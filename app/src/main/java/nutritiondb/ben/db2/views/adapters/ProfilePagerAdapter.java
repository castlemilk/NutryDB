package nutritiondb.ben.db2.views.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.io.Serializable;

import nutritiondb.ben.db2.Application;
import nutritiondb.ben.db2.fragments.FactSheetFragment;
import nutritiondb.ben.db2.models.FoodProfile;

/** ProfilePagerAdapter
 *  Adapter for managing the summary + detailed tabs.
 *
 */

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG =ProfilePagerAdapter.class.getSimpleName();
    private Application app;
    private FoodProfile foodProfile;
    private Fragment[]  fragments;
    public FactSheetFragment summaryTab;
    public FactSheetFragment detailedTab;
    public ProfilePagerAdapter(Application app, FragmentManager fm) {
        super(fm);
        this.app = app;
        this.foodProfile = app.currentFoodProfile;
        summaryTab = FactSheetFragment.newInstance("summary", foodProfile);
        detailedTab = FactSheetFragment.newInstance("detailed", foodProfile);
        fragments = new Fragment[] {summaryTab, detailedTab};
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments[position];
        Log.i(TAG,"ProfilePagerAdapter:getItem:fragment:position"+position);
        foodProfile = app.currentFoodProfile;
        if (foodProfile!= null) {
            Bundle args = new Bundle();
            args.putInt("position", position);
            args.putString("id", foodProfile.getNDBno());
            args.putSerializable("profile", (Serializable) foodProfile);
            fragment.setArguments(args);
        }
        Log.i(TAG,"ProfilePagerAdapter:getItem:done");
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position % 2) {
            case 0:
                return "Summary";
            case 1:
                return "Detailed";
        }
        return "";
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}

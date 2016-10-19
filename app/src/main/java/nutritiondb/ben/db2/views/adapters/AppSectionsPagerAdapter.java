package nutritiondb.ben.db2.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nutritiondb.ben.db2.fragments.FavouriteFragment;
import nutritiondb.ben.db2.fragments.LookupFragment;
import nutritiondb.ben.db2.fragments.RecentFragment;

/**
 * Created by benebsworth on 25/06/16.
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
    private Fragment[]      fragments;
    public LookupFragment tabLookup;
    FavouriteFragment tabFavourites;
    RecentFragment tabRecent;
//    SettingsFragment tabSettings;



    public AppSectionsPagerAdapter(FragmentManager fm) {
        super(fm);

        tabLookup = new LookupFragment();
        tabFavourites = new FavouriteFragment();
        tabRecent = new RecentFragment();
//        tabSettings = new SettingsFragment();
        fragments = new Fragment[] {tabLookup,tabFavourites,tabRecent};//, tabSettings};



    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }


}
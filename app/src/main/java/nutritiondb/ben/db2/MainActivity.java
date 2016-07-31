package nutritiondb.ben.db2;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shamanland.fonticon.FontIconDrawable;

import nutritiondb.ben.db2.fragments.BaseListFragment;
import nutritiondb.ben.db2.views.adapters.AppSectionsPagerAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private AppSectionsPagerAdapter appSectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Application app = (Application) getApplication();
        app.installTheme(this);
        setContentView(R.layout.activity_main);

        appSectionsPagerAdapter = new AppSectionsPagerAdapter(
                getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(appSectionsPagerAdapter.getCount());
        viewPager.setAdapter(appSectionsPagerAdapter);

        final String[] subtitles = new String[]{
                getString(R.string.subtitle_lookup),
                getString(R.string.subtitle_bookmark),
                getString(R.string.subtitle_history),
                getString(R.string.subtitle_settings),
        };

        viewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                        actionBar.setSubtitle(subtitles[position]);
                    }
                });

        Drawable[] tabIcons = new Drawable[4];
        tabIcons[0] = FontIconDrawable.inflate(this, R.xml.ic_tab_search);
        tabIcons[1] = FontIconDrawable.inflate(this, R.xml.ic_tab_bookmark);
        tabIcons[2] = FontIconDrawable.inflate(this, R.xml.ic_tab_history);
        tabIcons[3] = FontIconDrawable.inflate(this, R.xml.ic_tab_settings);

        for (int i = 0; i < appSectionsPagerAdapter.getCount(); i++) {
            ActionBar.Tab tab = actionBar.newTab();
            tab.setTabListener(this);
            tab.setIcon(tabIcons[i]);
            actionBar.addTab(tab);
        }

        app.getitemList();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int currentSection = savedInstanceState.getInt("currentSection");
        viewPager.setCurrentItem(currentSection);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSection", viewPager.getCurrentItem());
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        Log.i(TAG, "onTabSelected:"+tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        Fragment frag = appSectionsPagerAdapter.getItem(tab.getPosition());
        if (frag instanceof BaseListFragment) {
            ((BaseListFragment)frag).finishActionMode();
        }
        if (tab.getPosition() == 0) {
            View v = this.getCurrentFocus();
            if (v != null){
                InputMethodManager mgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onBackPressed() {
        int currentItem = viewPager.getCurrentItem();
        Fragment frag = appSectionsPagerAdapter.getItem(currentItem);
        Log.d(TAG, "current tab: " + currentItem);
        super.onBackPressed();
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = cm.getPrimaryClip();
        if (clipData == null) {
            return;
        }
        int count = clipData.getItemCount();
        for (int i = 0; i < count; i++) {
            ClipData.Item item = clipData.getItemAt(i);
            CharSequence text = item.getText();
            if (text != null && text.length() > 0) {
                if (Patterns.WEB_URL.matcher(text).find()) {
                    Log.d(TAG, "Text contains web url, not pasting: " + text);
                    return;
                }
                viewPager.setCurrentItem(0);
                cm.setPrimaryClip(ClipData.newPlainText(null, ""));
                appSectionsPagerAdapter.tabLookup.setQuery(text.toString());
                break;
            }
        }
    }








}



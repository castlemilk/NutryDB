package nutritiondb.ben.db2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nutritiondb.ben.db2.models.FoodProfile;
import nutritiondb.ben.db2.models.Portion;
import nutritiondb.ben.db2.views.adapters.ProfilePagerAdapter;

/**
 * Created by benebsworth on 3/07/16.
 */
public class FoodProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    static final String PREF = "FoodProfile";
    public Intent intent;
    public int position;
    private Spinner servingSelector;
    private static final String TAG = FoodProfileActivity.class.getSimpleName();
    private ProfilePagerAdapter profilePagerAdapter;
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private String NDB_no;
    public Application app;
    public FoodProfile mfoodProfile;

    boolean onDestroyCalled = false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (Application) getApplication();
        setContentView(R.layout.activity_food_profile_loading);
        intent = getIntent();
        position = intent.getIntExtra("position", 0);

        final String action = intent.getAction();
        Log.i(TAG, "action: " + action);

        if (action == null) {
            NDB_no = app.lastResult.getItem(position).getNDBno();
            Log.i(TAG, "position: "+ position);
            app.fireBaseController.getProfile(NDB_no, this);
        }
        else {
            mfoodProfile = (FoodProfile) getIntent().getExtras().getSerializable("profile");

            app.currentFoodProfile =mfoodProfile;
            Log.i(TAG, "position: "+ position);
            AsyncTask<Void, Void, ProfilePagerAdapter> createAdapterTask = new AsyncTask<Void, Void, ProfilePagerAdapter>() {
                public Exception exception;

                @Override
                protected ProfilePagerAdapter doInBackground(Void... params) {
                    ProfilePagerAdapter result = null;
                    try {
                        if (action.equals("showFavourites")) {
                            result = createFromBookmarks(app);
                        } else if (action.equals("showHistory")) {
                            result = createFromHistory(app);
                        }
                    }

                catch (Exception e) {
                    this.exception = e;
                }
                return result;
            }
                @Override
                protected void onPostExecute(ProfilePagerAdapter adapter) {
                    if (isFinishing() || onDestroyCalled) {
                        return;
                    }
                    if (this.exception != null) {
                        Toast.makeText(FoodProfileActivity.this,
                                this.exception.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    profilePagerAdapter = adapter;
                    if (profilePagerAdapter == null) {
                        int messageId = R.string.article_collection_invalid_link;
                        Toast.makeText(FoodProfileActivity.this, messageId,
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    Log.i(TAG, "postexecute:setting pager view");
                    setContentView(R.layout.activity_food_profile);
                    app.history.add(mfoodProfile);
                    mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
                    View view = mViewPager.getRootView();
                    servingSelector = (Spinner) view.findViewById(R.id.servingSelector);
                    ArrayList<Portion> servings = new ArrayList<>();
                    servings.add(new Portion("100g", "100", "1"));
                    servingSelector.setOnItemSelectedListener(FoodProfileActivity.this);
                    ArrayAdapter<Portion> servingAdapter = new ArrayAdapter<Portion>(view.getContext(), R.layout.spinner_list_item, servings);
                    servingSelector.setAdapter(servingAdapter);
                    mViewPager.getViewPager().setAdapter(profilePagerAdapter);
                    mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
                        @Override
                        public HeaderDesign getHeaderDesign(int page) {
                            switch (page) {
                                case 0:
                                    return HeaderDesign.fromColorResAndUrl(
                                            R.color.green,
                                            "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                                case 1:
                                    return HeaderDesign.fromColorResAndUrl(
                                            R.color.blue,
                                            "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                                case 2:
                                    return HeaderDesign.fromColorResAndUrl(
                                            R.color.cyan,
                                            "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                                case 3:
                                    return HeaderDesign.fromColorResAndUrl(
                                            R.color.red,
                                            "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                            }
                            return null;
                        }
                    });
//
                    toolbar = mViewPager.getToolbar();
                    if (toolbar != null) {
                        setSupportActionBar(toolbar);
                    }
                    mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
                    mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

                    TextView title = (TextView) view.findViewById(R.id.header_name);
                    title.setText(mfoodProfile.name);
                    TextView group = (TextView) view.findViewById(R.id.header_group);
                    group.setText(mfoodProfile.group);
                    TextView source  = (TextView) view.findViewById(R.id.header_source);
                    source.setText("USDA");
                    update_portions(servingAdapter);

                }
            };

            createAdapterTask.execute();
        }
            }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ProfilePagerAdapter createFromLastResult(final Application app) {

        return new ProfilePagerAdapter(app, getSupportFragmentManager());
    }

    public ProfilePagerAdapter createFromBookmarks(final Application app) {
        mfoodProfile = app.bookmarks.get(position);
        app.currentFoodProfile = mfoodProfile;
        Log.i(TAG, "createFromBookmarks:NDB:"+mfoodProfile.NDB_no );
        Log.i(TAG, "createFromBookmarks:name:"+mfoodProfile.name);
        return new ProfilePagerAdapter(app, getSupportFragmentManager());
    }
    public ProfilePagerAdapter createFromHistory(final Application app) {
        mfoodProfile = app.history.get(position);
        app.currentFoodProfile = mfoodProfile;
        Log.i(TAG, "createFromHistory:NDB:"+mfoodProfile.NDB_no );
        Log.i(TAG, "createFromHistory:name:"+mfoodProfile.name);
        return new ProfilePagerAdapter(app, getSupportFragmentManager());
    }


    public void gotProfile(FoodProfile foodProfile) {

        Log.i(TAG, ":gotProfile:" + foodProfile.getNutrients().values().size() + ":nutrients:");
        app.currentFoodProfile = foodProfile;
        mfoodProfile = foodProfile;
        profilePagerAdapter = createFromLastResult(app);
        if (profilePagerAdapter == null) {
                int messageId = R.string.article_collection_invalid_link;
            Toast.makeText(FoodProfileActivity.this, messageId,
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_food_profile);
        app.history.add(foodProfile);
        Log.i(TAG, "postexecute:setting pager view");
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        View view = mViewPager.getRootView();
        servingSelector = (Spinner) view.findViewById(R.id.servingSelector);
        ArrayList<Portion> servings = new ArrayList<>();
        servings.add(new Portion("100g", "100", "1"));
        servingSelector.setOnItemSelectedListener(this);
        ArrayAdapter<Portion> servingAdapter = new ArrayAdapter<Portion>(view.getContext(), R.layout.spinner_list_item, servings);
        servingSelector.setAdapter(servingAdapter);

        mViewPager.getViewPager().setAdapter(profilePagerAdapter);
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }
                return null;
            }
        });

        toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        //Set title fields
        TextView title = (TextView) view.findViewById(R.id.header_name);
        title.setText(foodProfile.name);
        TextView group = (TextView) view.findViewById(R.id.header_group);
        group.setText(foodProfile.group);
        TextView source  = (TextView) view.findViewById(R.id.header_source);
        source.setText("USDA");

        update_portions(servingAdapter);


    }

    public void update_portions(ArrayAdapter<Portion> adapter) {
        Log.i(TAG,"build_portions:started build");
        if (mfoodProfile != null) {
            HashMap<String, Portion> portions = mfoodProfile.getPortions();


            for (Map.Entry<String, Portion> portion : portions.entrySet()) {
                System.out.println(portion.getKey() + " : " + portion.getValue().getPortion().toString());
                adapter.add(portion.getValue());
            }
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemSelected:update:table:");
        if (profilePagerAdapter.summaryTab.factList != null && profilePagerAdapter.detailedTab.factList != null) {
            profilePagerAdapter.summaryTab.build_summary_table(mfoodProfile, (Portion) parent.getSelectedItem());
            profilePagerAdapter.detailedTab.build_detailed_table(mfoodProfile, (Portion) parent.getSelectedItem());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

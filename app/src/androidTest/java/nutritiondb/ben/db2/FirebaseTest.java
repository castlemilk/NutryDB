package nutritiondb.ben.db2;

//import android.app.Application;

import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class FirebaseTest extends ApplicationTestCase<Application> {
//    private static nutritiondb.ben.db2.Application application;
    private DatabaseReference mItemReference;
    private FirebaseDatabase mFirebaseDatabase;
    public FirebaseTest() {
        super(Application.class);
    }


//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        if (application == null) {
//            application = getApplication();
//        }
//        if (application == null) {
//            application = (Application) getContext().getApplicationContext();
//            assertNotNull(application);
//            long start = System.currentTimeMillis();
//
//        }
//    }

    @Test
    public void testReference(){

//        FirebaseApp.initializeApp(mContext, FirebaseOptions.fromResource(mContext));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemReference = mFirebaseDatabase.getReference();
//        System.out.println(mItemReference.toString());
        System.out.println(mFirebaseDatabase.toString());
        Log.d("output: ", mFirebaseDatabase.toString());
        Log.d("mItemReference: ", mItemReference.toString());

    }
    @Test
    public void testGetList() {

    }
    @Test
    public void testGetUSDAItem() {

    }
    @Test
    public void testGetNUTTABItem() {

    }
}
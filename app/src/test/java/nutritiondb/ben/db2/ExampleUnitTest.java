package nutritiondb.ben.db2;

import android.content.Context;
import android.test.mock.MockContext;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void validate_google_services() throws Exception {
        Context mContext;
        mContext = new MockContext();

        DatabaseReference mItemReference;
        FirebaseDatabase mFirebaseDatabase;
        FirebaseApp.initializeApp(mContext, FirebaseOptions.fromResource(mContext));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mItemReference = mFirebaseDatabase.getReference();
//        System.out.println(mItemReference.toString());
        System.out.println(mFirebaseDatabase.toString());

    }
}
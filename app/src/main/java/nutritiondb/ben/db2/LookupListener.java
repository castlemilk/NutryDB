package nutritiondb.ben.db2;

/**
 * Created by benebsworth on 25/06/16.
 */
public interface LookupListener {
    void onLookupStarted(String query);
    void onLookupFinished(String query);
    void onLookupCanceled(String query);
}
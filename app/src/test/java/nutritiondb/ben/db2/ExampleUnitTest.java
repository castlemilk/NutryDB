package nutritiondb.ben.db2;

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
        Application myapp = Application.getInstance();
        // cant seem to be done using unit tests, must be instrumental tests.

    }
}
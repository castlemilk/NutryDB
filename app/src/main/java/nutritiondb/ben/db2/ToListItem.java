package nutritiondb.ben.db2;

import nutritiondb.ben.db2.models.ListItem;

/**
 * Created by benebsworth on 3/07/16.
 */
public interface ToListItem {

    ListItem convert(Object item);
}

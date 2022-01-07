
/***
 * this class is the base for all the features that ill create like commands and etc
 */
package space.nickyblackburn.features;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.Locale.Category;

import space.nickyblackburn.settings.Settings;

public abstract class Feature {

    private final LinkedHashMap<String, Settings> settings =new LinkedHashMap<>();
    // gets the name of the feature
    public abstract String getName();

    // gets the decription of the feature
    public abstract String getDescription();

    // Returns trhe catagory 
    public Category getCategory()
    {
        return null;
    }

    public abstract String getPrimaryAction();

    public void doPrimaryAction()
    {
        
    }

    
}

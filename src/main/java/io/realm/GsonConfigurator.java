package io.realm;

import com.google.gson.GsonBuilder;

public class GsonConfigurator {

    public static GsonBuilder configure(GsonBuilder gsonBuilder) {
        return configure(gsonBuilder, false);
    }

    public static GsonBuilder configure(GsonBuilder gsonBuilder, boolean excludeIgnoredFields) {

        gsonBuilder.registerTypeAdapterFactory(new RealmTypeAdapterFactory());
        gsonBuilder.setExclusionStrategies(new RealmExclusionStrategy(excludeIgnoredFields));

        return gsonBuilder;
    }
    
    
}

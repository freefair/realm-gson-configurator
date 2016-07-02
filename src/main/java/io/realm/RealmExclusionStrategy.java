package io.realm;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import io.realm.annotations.Ignore;
import io.realm.internal.RealmObjectProxy;

class RealmExclusionStrategy implements ExclusionStrategy {

    private final boolean excludeIgnoredFields;

    RealmExclusionStrategy(boolean excludeIgnoredFields) {
        this.excludeIgnoredFields = excludeIgnoredFields;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (f.getDeclaringClass().equals(RealmObject.class)) {
            return true;
        }
        if (f.getDeclaringClass().equals(RealmObjectProxy.class)) {
            return true;
        }
        if (f.getDeclaringClass().getSimpleName().contains("RealmProxy") && RealmObjectProxy.class.isAssignableFrom(f.getDeclaringClass())) {
            return true;
        }
        if (excludeIgnoredFields && f.getAnnotation(Ignore.class) != null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}

package io.realm;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

class RealmTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if (RealmModel.class.isAssignableFrom(type.getRawType())) {
            TypeAdapter<RealmModel> delegateAdapter = (TypeAdapter<RealmModel>) gson.getDelegateAdapter(this, type);

            return (TypeAdapter<T>) new RealmTypeAdapter(delegateAdapter, gson);
        }

        return null;
    }

}

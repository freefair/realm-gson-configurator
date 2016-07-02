package io.realm;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;

import io.realm.internal.ColumnInfo;
import io.realm.internal.RealmObjectProxy;

class RealmTypeAdapter extends TypeAdapter<RealmModel> {

    final TypeAdapter<RealmModel> delegate;
    final Gson gson;

    RealmTypeAdapter(TypeAdapter<RealmModel> delegate, Gson gson) {
        //noinspection unchecked
        this.delegate = delegate;
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, RealmModel realmObject) throws IOException {
        JsonElement jsonElement = delegate.toJsonTree(realmObject);

        if (jsonElement.isJsonObject() && realmObject instanceof RealmObjectProxy) {
            overwriteWithRealmValues(jsonElement, (RealmObjectProxy) realmObject);
        }

        out.jsonValue(jsonElement.toString());
    }

    private void overwriteWithRealmValues(JsonElement jsonElement, RealmObjectProxy realmObject) {
        Class<? extends RealmObjectProxy> proxyClass = realmObject.getClass();

        RealmSchema realmSchema = realmObject.realmGet$proxyState().getRealm$realm().getSchema();
        ColumnInfo columnInfo = realmSchema.getColumnInfo(proxyClass);

        for (String fieldName : columnInfo.getIndicesMap().keySet()) {
            try {
                Method fieldGetter = proxyClass.getDeclaredMethod("realmGet$" + fieldName);
                Object fieldValue = fieldGetter.invoke(realmObject);
                JsonElement jsonValue = gson.toJsonTree(fieldValue);
                jsonElement.getAsJsonObject().add(fieldName, jsonValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RealmModel read(JsonReader in) throws IOException {
        return delegate.read(in);
    }
}

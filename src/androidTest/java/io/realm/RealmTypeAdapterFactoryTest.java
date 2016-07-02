package io.realm;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.test.Dog;
import io.realm.test.TestRealmModule;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RealmTypeAdapterFactoryTest {

    Gson gson;
    private Realm realm;
    private Dog managedDog;
    private Dog unmanagedDog;

    @Before
    public void setUp() throws Exception {
        gson = GsonConfigurator.configure(new GsonBuilder()).create();

        RealmConfiguration build = new RealmConfiguration.Builder(InstrumentationRegistry.getContext())
                .modules(new TestRealmModule()).build();

        Realm.deleteRealm(build);
        realm = Realm.getInstance(build);

        realm.beginTransaction();
        managedDog = realm.createObject(Dog.class);
        realm.commitTransaction();
        unmanagedDog = new Dog();
    }

    @Test
    public void testUnmanaged() {
        unmanagedDog.setAge(5);
        unmanagedDog.setName("Bella");
        unmanagedDog.setIgnoredString("Foo");

        String json = gson.toJson(unmanagedDog);

        assertThat(json, containsString("5"));
        assertThat(json, containsString("Bella"));
        assertThat(json, containsString("Foo"));
    }

    @Test
    public void testManaged() {
        realm.beginTransaction();
        managedDog.setAge(5);
        managedDog.setName("Bella");
        managedDog.setIgnoredString("Foo");
        realm.commitTransaction();

        String json = gson.toJson(managedDog);

        assertThat(json, containsString("5"));
        assertThat(json, containsString("Bella"));
        assertThat(json, containsString("Foo"));
    }

    @After
    public void tearDown() throws Exception {
        realm.close();
    }
}
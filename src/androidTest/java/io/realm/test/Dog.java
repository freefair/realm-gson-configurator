package io.realm.test;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class Dog extends RealmObject {

    @Ignore private String ignoredString;
    private int age;
    private String name;

    RealmList<Dog> children;
    Dog mate;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIgnoredString() {
        return ignoredString;
    }

    public void setIgnoredString(String ignoredString) {
        this.ignoredString = ignoredString;
    }
}

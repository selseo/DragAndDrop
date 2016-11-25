package com.example.katesudal.draganddrop;

import io.realm.RealmObject;

/**
 * Created by katesuda.l on 25/11/2559.
 */

public class Human  extends RealmObject {
    String name;

    public Human() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Human(String name) {
        this.name = name;
    }
}

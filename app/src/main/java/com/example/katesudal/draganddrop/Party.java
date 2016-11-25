package com.example.katesudal.draganddrop;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by katesuda.l on 25/11/2559.
 */

public class Party extends RealmObject {
    String date;
    RealmList<Section> sectionList;
    public Party() {
    }

    public RealmList<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(RealmList<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}


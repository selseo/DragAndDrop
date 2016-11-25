package com.example.katesudal.draganddrop;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by katesuda.l on 25/11/2559.
 */

public class Section extends RealmObject {
    String sectionName;
    RealmList<Human> memberList;

    public Section() {
    }

    public Section(String pui) {
        this.sectionName = pui;
    }

    public List<Human> getMemberList() {
        return memberList;
    }

    public void setMemberList(RealmList<Human> memberList) {
        this.memberList = memberList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}

package com.hiyoyo.omiso.nitijouseikatu;

import io.realm.RealmObject;

public class Tag extends RealmObject {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
}

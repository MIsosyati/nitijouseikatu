package com.hiyoyo.omiso.nitijouseikatu;

import java.util.Date;

import io.realm.RealmObject;

public class Plan extends RealmObject {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public String getFlec() {
        return flec;
    }

    public void setFlec(String flec) {
        this.flec = flec;
    }

    String name;
    String date;
    String tag;
    String notif;
    String flec;
    Date starttime;
    Date endtime;

}

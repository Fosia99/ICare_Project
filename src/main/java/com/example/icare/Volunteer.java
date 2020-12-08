package com.example.icare;
/**  A Java class for volunteer that is used in the Volunteer Adapter Method to pass variables*/
public class Volunteer {

    public String title, content, location, date, time, uid,contact;

    public Volunteer() {
    }

    public Volunteer(String title, String content, String location, String date, String time, String uid, String contact) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.date = date;
        this.time = time;
        this.uid = uid;
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
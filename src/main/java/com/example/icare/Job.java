package com.example.icare;
/**  A Java class for jobs that is used in the JobListings Adapter Method to pass variables*/
public class Job {
    public String title, content, requirements,location, date, time, uid,contact;



    public Job() {
    }

    public Job(String title, String content, String requirements, String location,
               String date, String time, String uid, String contact) {
        this.title = title;
        this.content = content;
        this.requirements = requirements;
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

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
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

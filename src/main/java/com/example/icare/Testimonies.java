package com.example.icare;
/**  A Java class for testimonies  that is used in the Testimonies Adapter Method to pass variables*/
public class Testimonies {
    public String image , name , content,uid;
    public Testimonies(){

    }
    public Testimonies(String image, String name, String content, String uid) {
        this.image = image;
        this.name = name;
        this.content = content;
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

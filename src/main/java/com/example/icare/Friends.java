package com.example.icare;
/**  A Java class for friends that is used in the Friends Adapter Method to pass variables*/
public class Friends {


    public String date,status;

    public Friends(){

    }

    public Friends(String date, String status) {
        this.date = date;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

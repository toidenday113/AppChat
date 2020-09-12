package com.example.appchat.Notifications;

public class DataGroup {
    private  String idGroup;
    private int icon;
    private String body;
    private String title;
    private String sented;

    public DataGroup(String idGroup, int icon, String body, String title, String sented) {
        this.idGroup = idGroup;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
    }

    public DataGroup() {
    }

    public String getUser() {
        return idGroup;
    }

    public void setUser(String idGroup) {
        this.idGroup = idGroup;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}

package com.ironxpert.admin.models;

import java.io.Serializable;

public class Agent implements Serializable {
    private String uid;
    private String name;
    private String phone;
    private String email;
    private String photo;

    public Agent() {}

    public Agent(String uid, String name, String phone, String email, String photo) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

package com.ironxpert.admin.models;

import java.io.Serializable;

public class Help implements Serializable {
    private String by;
    private String email;
    private String help_id;
    private String message;
    private boolean read;
    private long time;
    private String type;

    public Help() {}

    public Help(String by, String email, String help_id, String message, boolean read, long time, String type) {
        this.by = by;
        this.email = email;
        this.help_id = help_id;
        this.message = message;
        this.read = read;
        this.time = time;
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHelp_id() {
        return help_id;
    }

    public void setHelp_id(String help_id) {
        this.help_id = help_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}

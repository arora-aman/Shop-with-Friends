package com.aman_arora.firebase.swf.model;

import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class User {
    String name, email;
    HashMap<String, Object> timeStampJoined;

    public User() {
    }

    public User(String name, String email, HashMap<String, Object> timeStampJoined) {
        this.name = name;
        this.email = email;
        this.timeStampJoined = timeStampJoined;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimeStampJoined() {
        return timeStampJoined;
    }

    @Exclude
    public long getTimeStampLong(){
        return (long) timeStampJoined.get(Constants.TIMESTAMP_OBJECT_KEY);
    }
}

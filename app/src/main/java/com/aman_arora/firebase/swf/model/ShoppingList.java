package com.aman_arora.firebase.swf.model;


import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class ShoppingList {
    private String owner, listName;
    private HashMap<String, Object> timeStamp;
    private HashMap<String, Object> timeStampLastUpdated;
    private HashMap<String, User> shoppingUsers;

    public ShoppingList() {
    }

    public ShoppingList(String owner, String listName, HashMap<String, Object> dateCreated) {
        this.owner = owner;
        this.listName = listName;
        this.timeStamp = dateCreated;
        HashMap<String, Object> dateModifiedObj = new HashMap<>();
        dateModifiedObj.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);
        timeStampLastUpdated = dateModifiedObj;
        this.shoppingUsers = new HashMap<>();
    }

    public String getOwner() {
        return owner;
    }

    public String getListName() {
        return listName;
    }

    public HashMap<String, Object> getTimeStamp() {
       return timeStamp;
    }

    public HashMap<String, Object> getTimeStampLastUpdated() {
        return timeStampLastUpdated;
    }

    @Exclude
    public long getTimeStampLastUpdatedLong() {
        return (long)timeStampLastUpdated.get(Constants.TIMESTAMP_OBJECT_KEY);
    }

    @Exclude
    public long getTimeStampLong() {
        return (long)timeStamp.get(Constants.TIMESTAMP_OBJECT_KEY);
    }

    public void setTimestampLastChangedToNow() {
        HashMap<String, Object> timestampNowObject = new HashMap<String, Object>();
        timestampNowObject.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);
        this.timeStampLastUpdated = timestampNowObject;
    }

    public HashMap<String, User> getShoppingUsers() {
        return shoppingUsers;
    }
}

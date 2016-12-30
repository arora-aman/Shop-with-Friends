package com.aman_arora.firebase.swf.utils;

import android.content.Context;

import com.aman_arora.firebase.swf.model.User;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.HashMap;


public class Utils {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context mContext = null;

    public Utils(Context con) {
        mContext = con;
    }

    public static String encodeEmail(String email) {
        if (email != null) return email.replace('.', ',');
        return null;
    }

    public static String decodeEmail(String encodeEmail) {
        if (encodeEmail != null) return encodeEmail.replace(',', '.');
        return null;
    }

    public static boolean checkIfOwner(String listOwner, String currentUserEmail) {
        return (listOwner != null &&
                listOwner.equals(currentUserEmail));
    }

    public static HashMap<String, Object> createUpdatePackage(HashMap<String, Object> update,
                                                              String owner, String listID, String key, Object value,
                                                              HashMap<String, User> sharedUsers) {


        update.put(Constants.FIREBASE_USER_LISTS_LOCATION + "/" + owner + "/" + listID + "/" + key, value);

        if (sharedUsers != null) {
            for (User user : sharedUsers.values()) {
                if (user == null) break;
                update.put(Constants.FIREBASE_USER_LISTS_LOCATION + "/" + user.getEmail() +
                        "/" + listID + "/" + key, value);
            }
        }

        return update;

    }

    public static HashMap<String, Object> createTimeStampUpdatePackage(HashMap<String, Object> update,
                                                                       String owner, String listID,
                                                                       HashMap<String, User> sharedWith) {

        HashMap<String, Object> timeStamp = new HashMap<>();
        timeStamp.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);
        createUpdatePackage(update, owner, listID, Constants.FIREBASE_PROPERTY_TIMESTAMP_UPDATED,
                timeStamp, sharedWith);
        return update;

    }
}

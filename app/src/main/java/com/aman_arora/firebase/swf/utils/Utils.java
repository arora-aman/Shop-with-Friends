package com.aman_arora.firebase.swf.utils;

import android.content.Context;

import com.aman_arora.firebase.swf.model.ShoppingList;

import java.text.SimpleDateFormat;


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

    public static boolean checkIfOwner(ShoppingList shoppingList, String currentUserEmail) {
        return (shoppingList.getOwner() != null &&
                shoppingList.getOwner().equals(currentUserEmail));
    }

}

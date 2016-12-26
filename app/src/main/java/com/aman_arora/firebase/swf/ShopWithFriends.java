package com.aman_arora.firebase.swf;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;


/**
 * Includes one-time initialization of Firebase related code
 */
public class ShopWithFriends extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setLogLevel( Logger.Level.DEBUG);
    }

}
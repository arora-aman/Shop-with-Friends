package com.aman_arora.firebase.swf;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;


/**
 * Includes one-time initialization of Firebase related code
 */
public class ShopWithFriends extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setNetworkState();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setLogLevel( Logger.Level.DEBUG);
    }


    private void setNetworkState() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isInternetAvailable = networkInfo != null && networkInfo.isConnected();

        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.PREFERENCE_LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_NETWORK_INFO, isInternetAvailable);
        editor.apply();
    }


}
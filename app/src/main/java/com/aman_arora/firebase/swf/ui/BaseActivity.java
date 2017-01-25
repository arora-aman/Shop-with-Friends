package com.aman_arora.firebase.swf.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public abstract class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    protected String mEncodedEmail;
    protected DatabaseReference firebaseRef;
    private final String TAG = BaseActivity.class.getSimpleName();
    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.GOOGLE_OAUTH_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.PREFERENCE_LOGIN_FILE, MODE_PRIVATE);
        mEncodedEmail = sharedPreferences.getString(Constants.PREFERENCE_ENCODED_EMAIL, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initializeBackground(LinearLayout linearLayout) {


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen_land);
        } else {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    protected void writeEmailToSharedPreferences(String encodedEmail, String provider){
        Log.d(this.getLocalClassName(), "writeEmailToSharedPreferences:" + encodedEmail);
        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.PREFERENCE_LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREFERENCE_ENCODED_EMAIL, encodedEmail);
        editor.putString(Constants.PREFERENCE_PROVIDER, provider);
        editor.apply();
        editor.commit();
    }

    protected Dialog verifyEmail(final FirebaseUser user, final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomTheme_Dialog);
        builder.setTitle(getString(R.string.user_email_not_verified));
        builder.setMessage(getString(R.string.resend_verification_email));
        builder.setNegativeButton(getString(R.string.negative_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.positive_button_yes), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) Log.d(TAG, getString(R.string.email_sent));
                    }
                });
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        return  builder.create();
    }

    protected void onAuthChanged(FirebaseUser user, final Context context){
        if(!getNetworkState()){
            return;
        }
        Dialog verificationDialog = verifyEmail(user, context);
        if(!user.isEmailVerified())verificationDialog.show();
        else{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Constants.FIREBASE_USERS_URL)
                    .child(Utils.encodeEmail(user.getEmail()));
            HashMap<String, Object> verified = new HashMap<>();
            verified.put(Constants.FIREBASE_USER_VERIFIED_LOCATION, Boolean.TRUE);
            databaseReference.updateChildren(verified, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d(TAG, "onComplete: " + (databaseError == null));
                }
            });
        }

    }

    protected void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected boolean getNetworkState(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFERENCE_LOGIN_FILE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.KEY_NETWORK_INFO, false);
    }

}

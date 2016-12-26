package com.aman_arora.firebase.swf.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.aman_arora.firebase.swf.utils.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.aman_arora.firebase.swf.R;


public abstract class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    protected String mEncodedEmail;

    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.PREFERENCE_LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREFERENCE_ENCODED_EMAIL, encodedEmail);
        editor.putString(Constants.PREFERENCE_PROVIDER, provider);
        editor.apply();
        editor.commit();
    }

}

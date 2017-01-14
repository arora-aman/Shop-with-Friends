package com.aman_arora.firebase.swf.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.ui.BaseActivity;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextEmailInput, mEditTextPasswordInput;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = LoginActivity.class.getSimpleName();
    private TextView mForgotPassword;

    //    private boolean mGoogleIntentInProgress;
    /* Request code used to invoke sign in user interactions for Google+ */
//    public static final int RC_GOOGLE_LOGIN = 1;
    /* A Google account object that is populated if the user signs in with Google */
    GoogleSignInAccount mGoogleAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth.getInstance().signOut();


        initializeScreen();

        mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    signInPassword();
                }
                return true;
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onAuthChanged(user, LoginActivity.this);
                } else showErrorToast("LoginNope some bs happened!/ Signed out!");
            }
        };

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = mEditTextEmailInput.getText().toString();
                if (!isEmailValid(emailAddress)) return;
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, R.string.reset_password_email_sent,
                                            Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(LoginActivity.this, R.string.error_profile_update_request_not_completed,
                                            Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private boolean isEmailValid(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            mEditTextEmailInput.setError(getResources().getString(R.string.invalid_email_address));
        else return true;

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onSignInPressed(View view) {
        if (validateDetails()) signInPassword();
    }

    public boolean validateDetails() {
        boolean validEmail = false, validPassword = false;
        if (mEditTextEmailInput.getText().toString().length() <= 0)
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
        else validEmail = true;
        if (mEditTextPasswordInput.getText().length() <= 0)
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
        else validPassword = true;

        return validEmail && validPassword;
    }

    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }


    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);
        LinearLayout linearLayoutLoginActivity = (LinearLayout) findViewById(R.id.linear_layout_login_activity);
        initializeBackground(linearLayoutLoginActivity);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
        mForgotPassword = (TextView) findViewById(R.id.tv_forgot_pass);
//        setupGoogleSignIn();
    }

//
//    private void setupGoogleSignIn() {
//        SignInButton signInButton = (SignInButton) findViewById(R.id.login_with_google);
//        signInButton.setSize(SignInButton.SIZE_WIDE);
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSignInGooglePressed(v);
//            }
//        });
//    }
//
//    public void onSignInGooglePressed(View view) {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
//        mAuthProgressDialog.show();
//    }

    public void signInPassword() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(mEditTextEmailInput.getText().toString().toLowerCase(), mEditTextPasswordInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            writeEmailToSharedPreferences(Utils.encodeEmail(mEditTextEmailInput.getText().toString().toLowerCase()), Constants.PROVIDER_EMAIL_PASSWORD);
                        } else {
                            showErrorToast(getString(R.string.login_error));
                            Log.d(TAG, "onComplete: Error:" + task.getException().getMessage());
                        }
                        mAuthProgressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) mAuth.removeAuthStateListener(mAuthListener);
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        mAuthProgressDialog.dismiss();
        showErrorToast(result.toString());
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_GOOGLE_LOGIN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) {
//                Log.d(TAG, "onActivityResult: Got something back, let's see what? :o");
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//            } else {
//                if (result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
//                    showErrorToast("The sign in was cancelled. Make sure you're connected to the internet and try again.");
//                } else {
//                    Log.d(TAG, "onActivityResult: " + result.getStatus());
//                    showErrorToast("Error handling the sign in: " + result.getStatus().getStatusMessage());
//                }
//                mAuthProgressDialog.dismiss();
//            }
//        }
//
//    }

//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getId(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        mAuthProgressDialog.dismiss();
//                    }
//                });
//
//
//    }


}
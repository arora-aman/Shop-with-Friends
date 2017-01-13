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

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.ui.BaseActivity;
import com.aman_arora.firebase.swf.ui.MainActivity;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

//TODO: setup verification on account creation via email/password and make it more secure 0.3.17, L3 -> 27/37
public class CreateAccountActivity extends BaseActivity {
    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextUsernameCreate, mEditTextEmailCreate, mEditTextPasswordCreate;
    private static final String TAG = CreateAccountActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initializeScreen();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onAuthChanged(user, CreateAccountActivity.this);
                } else showErrorToast("Register some bs happened!/ Signed out!");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) mAuth.removeAuthStateListener(mAuthListener);
    }


    public void initializeScreen() {
        mEditTextUsernameCreate = (EditText) findViewById(R.id.edit_text_username_create);
        mEditTextEmailCreate = (EditText) findViewById(R.id.edit_text_email_create);
        mEditTextPasswordCreate = (EditText) findViewById(R.id.edit_text_password_create);
        LinearLayout linearLayoutCreateAccountActivity = (LinearLayout) findViewById(R.id.linear_layout_create_account_activity);
        initializeBackground(linearLayoutCreateAccountActivity);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);

        mEditTextPasswordCreate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_DONE) onCreateAccountPressed(null);
                return true;
            }
        });
    }


    public void onSignInPressed(View view) {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void onCreateAccountPressed(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (createUserHelper()) {
            mAuthProgressDialog.show();
            mAuth.createUserWithEmailAndPassword(mEditTextEmailCreate.getText().toString().toLowerCase(), mEditTextPasswordCreate.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "onComplete: " + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                showErrorToast(task.getException().getLocalizedMessage());
                                Log.d(TAG, "onComplete: Error:" + task.getException().getMessage());
                            } else {
                                onRegistration(task.getResult());
                            }
                            mAuthProgressDialog.dismiss();
                        }
                    });
        }
    }


    private boolean createUserHelper() {
        boolean userNameValid = isUserNameValid(mEditTextUsernameCreate.getText().toString());
        boolean passwordValid = isPasswordValid(mEditTextPasswordCreate.getText().toString());
        boolean emailValid = isEmailValid(mEditTextEmailCreate.getText().toString());
        return userNameValid && passwordValid && emailValid;
    }


    private boolean isEmailValid(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            mEditTextEmailCreate.setError(getResources().getString(R.string.invalid_email_address));
        else return true;

        return false;
    }

    private boolean isPasswordValid(String password) {
        if (!(password != null && password.length() >= 6))
            mEditTextPasswordCreate.setError(getResources().getString(R.string.error_invalid_password_not_valid));
        else return true;

        return false;
    }

    private boolean isUserNameValid(String userName) {
        if (userName != null && userName.length() > 0) return true;
        mEditTextUsernameCreate.setError(getString(R.string.error_cannot_be_empty));
        return false;
    }

    private void onRegistration(final AuthResult authResult) {
        Log.d(TAG, "onRegistration: Registering");
        final String encodedEmail = Utils.encodeEmail(mEditTextEmailCreate.getText().toString().toLowerCase().toLowerCase());
        HashMap<String, Object> timeStamp = new HashMap<String, Object>();
        timeStamp.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);
        User user = new User(mEditTextUsernameCreate.getText().toString(),
                encodedEmail, timeStamp);

        FirebaseUser currentUser = authResult.getUser();
        String uid = currentUser.getUid();
        Log.d(TAG, "onRegistration: " + uid);
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(mEditTextUsernameCreate.getText().toString())
                .build();

        currentUser.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "update_user_setName: " + task.isSuccessful());
                    }
                });


        HashMap<String, Object> registeredUser = new HashMap<>();
        registeredUser.put(Constants.USER_LOCATION + '/' + encodedEmail, user);
        registeredUser.put(Constants.FIREBASE_UID_MAPPINGS_LOCATION + '/' + uid, encodedEmail);
        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL)
                .updateChildren(registeredUser, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null){
                            writeEmailToSharedPreferences(encodedEmail, Constants.PROVIDER_EMAIL_PASSWORD);
                            FirebaseUser newUser = authResult.getUser();
                            newUser.sendEmailVerification();
                            startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                            finish();
                        }
                        else   Log.d(TAG, "onComplete: " + databaseError.getMessage());
                    }
                });

    }

}
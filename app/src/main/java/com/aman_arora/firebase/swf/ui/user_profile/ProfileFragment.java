package com.aman_arora.firebase.swf.ui.user_profile;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private EditText mNameET, mPasswordET, mConfirmPassET;
    private LinearLayout mCompleteValidationLayout;
    private Button mSendVerificationEmail, mFinishRegistration, mUpdateProfile, mChangePassword;
    private TextView mEmailTV, mUserVerifiedTV;
    private boolean isUserVerified;
    private FirebaseUser currentUser;

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        profileFragment.setArguments(args);
        return profileFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null, false);
        initialiseScreen(view);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(getActivity(), getString(R.string.user_not_found), Toast.LENGTH_LONG)
                            .show();
                    getActivity().finish();
                    return;
                }
                currentUser = user;
                mEmailTV.setText(user.getEmail());
                mNameET.setText(user.getDisplayName());
                getActivity().setTitle(user.getDisplayName() + "'s Lists");
                isUserVerified = user.isEmailVerified();
                Log.d(TAG, "onAuthStateChanged: " + user.getDisplayName());
                Log.d(TAG, "onAuthStateChanged: " + isUserVerified);
                if (isUserVerified) {
                    mSendVerificationEmail.setVisibility(View.GONE);
                    mUserVerifiedTV.setText(getString(R.string.profile_user_verified_true));
                    mCompleteValidationLayout.setVisibility(View.GONE);
                } else {
                    mSendVerificationEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "sendEmailVerification " + task.isSuccessful());
                                        }
                                    });
                        }
                    });

                    mFinishRegistration.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, Object> userObject = new HashMap<String, Object>();
                            userObject.put(Constants.KEY_CURRENT_USER, user);
                            ReAuthenticateDialogFragment fragment = ReAuthenticateDialogFragment.newInstance(userObject);
                            fragment.show(getFragmentManager(), "re_authFragment");
                        }
                    });
                }
            }
        };

        mPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 0) {
                    mConfirmPassET.setVisibility(View.VISIBLE);
                    mChangePassword.setVisibility(View.VISIBLE);
                } else {
                    mConfirmPassET.setVisibility(View.GONE);
                    mChangePassword.setVisibility(View.GONE);
                }
            }
        });

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        mUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClicked();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    public void initialiseScreen(View view) {

        mNameET = (EditText) view.findViewById(R.id.profile_edit_text_name);
        mEmailTV = (TextView) view.findViewById(R.id.profile_text_view_email);
        mPasswordET = (EditText) view.findViewById(R.id.profile_edit_text_password);
        mConfirmPassET = (EditText) view.findViewById(R.id.profile_edit_text_confirm_password);
        mCompleteValidationLayout = (LinearLayout) view.findViewById(R.id.profile_finish_validation_layout);
        mSendVerificationEmail = (Button) view.findViewById(R.id.profile_sendVerificationEmail);
        mFinishRegistration = (Button) view.findViewById(R.id.profile_reauthenticateUser);
        mUpdateProfile = (Button) view.findViewById(R.id.profile_update_profile);
        mChangePassword = (Button) view.findViewById(R.id.profile_changePassword);
        mUserVerifiedTV = (TextView) view.findViewById(R.id.profile_userVerifiedValue);
        isUserVerified = false;
        setMenuVisibility(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) mAuth.removeAuthStateListener(mAuthStateListener);
    }

    public void changePassword() {
        String newPassword = mPasswordET.getText().toString();
        if (newPassword.length() > 0) {
            String confirmPass = mConfirmPassET.getText().toString();
            if (newPassword.length() < 6) {
                Toast.makeText(getActivity(),
                        getString(R.string.error_invalid_password_not_valid), Toast.LENGTH_LONG).show();
            } else if (!newPassword.equals(confirmPass)) {
                Toast.makeText(getActivity(), R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> userObject = new HashMap<String, Object>();
                userObject.put(Constants.KEY_CURRENT_USER, currentUser);
                ChangePasswordDialogFragment fragment =
                        ChangePasswordDialogFragment.newInstance(userObject, currentUser.getEmail(), newPassword);
                fragment.show(getFragmentManager(), "re_authFragment");
            }
        }

    }

    public void onUpdateClicked() {
        String newName = mNameET.getText().toString();
        String newPassword = mPasswordET.getText().toString();
        Log.d(TAG, "onUpdateClicked: " + newName);
        if (newPassword.length() > 0) {
            Toast.makeText(getActivity(), R.string.error_update_password_first, Toast.LENGTH_SHORT)
                    .show();
        } else if (newName.length() <= 0) {
            mNameET.setError(getString(R.string.error_cannot_be_empty));
        } else if (newName.equals(currentUser.getDisplayName())) {
            Toast.makeText(getActivity(), R.string.error_no_update, Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> userObject = new HashMap<String, Object>();
            userObject.put(Constants.KEY_CURRENT_USER, currentUser);
            ChangeNameDialogFragment changeNameDialog = ChangeNameDialogFragment.newInstance(userObject,
                    currentUser.getEmail(), newName);
            changeNameDialog.show(getFragmentManager(), "re_authFrag");


        }
    }

}

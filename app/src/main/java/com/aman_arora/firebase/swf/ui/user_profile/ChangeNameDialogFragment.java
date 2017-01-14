package com.aman_arora.firebase.swf.ui.user_profile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChangeNameDialogFragment extends android.support.v4.app.DialogFragment {

    private static final String TAG = ReAuthenticateDialogFragment.class.getSimpleName();
    private EditText mEditTextEmailInput, mEditTextPasswordInput;
    private String mUserEmail, mUserName;
    private FirebaseUser userToValidate;

    public static ChangeNameDialogFragment newInstance(HashMap<String, Object> currentUser,
                                                       String userEmail, String newName) {
        ChangeNameDialogFragment fragment = new ChangeNameDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_CURRENT_USER, currentUser);
        args.putString(Constants.KEY_EMAIL, userEmail);
        args.putString(Constants.KEY_NAME, newName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments().getSerializable(Constants.KEY_CURRENT_USER) != null;
        userToValidate = (FirebaseUser) ((HashMap<String, Object>) getArguments().getSerializable(Constants.KEY_CURRENT_USER))
                .get(Constants.KEY_CURRENT_USER);
        mUserEmail = getArguments().getString(Constants.KEY_EMAIL);
        mUserName = getArguments().getString(Constants.KEY_NAME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_re_authenticate, null);
        mEditTextEmailInput = (EditText) view.findViewById(R.id.re_auth_edit_text_email);
        mEditTextPasswordInput = (EditText) view.findViewById(R.id.re_auth_edit_text_password);
        if (mUserEmail != null) mEditTextEmailInput.setText(mUserEmail);
        view.findViewById(R.id.mustVerifyEmail).setVisibility(View.GONE);
        Button mValidateUser = (Button) view.findViewById(R.id.re_auth_validate);

        mValidateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDetails()) onValidateClick();
            }
        });
        builder.setView(view);
        return builder.create();
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

    public void onValidateClick() {
        AuthCredential credential = EmailAuthProvider
                .getCredential(mEditTextEmailInput.getText().toString().trim(),
                        mEditTextPasswordInput.getText().toString());

        userToValidate.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), R.string.error_validating_user, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mUserName)
                                    .build();
                            userToValidate.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
//                                                Toast.makeText(getActivity(), R.string.profile_updated, Toast.LENGTH_SHORT)
//                                                        .show();
                                                Log.d(TAG, "onComplete: " + 123 + userToValidate.getDisplayName());

                                                HashMap<String, Object> nameUpdate = new HashMap<String, Object>();
                                                String userNodeUpdateKey = Constants.USER_LOCATION + '/' +
                                                        Utils.encodeEmail(userToValidate.getEmail()) + '/' + Constants.PROPERTY_USER_NAME;
                                                nameUpdate.put(userNodeUpdateKey, mUserName);
                                                updateNames(nameUpdate);
                                            } else {
                                                //TODO: if reauth error call re-auth;
                                                Toast.makeText(getActivity(), R.string.error_profile_update_request_not_completed,
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }
                                    });
                        }
                        dismiss();
                    }
                });
    }

    private void updateNames(final HashMap<String, Object> update){
        String friendOfUrl = Constants.FIREBASE_URL + '/' + Constants.FIREBASE_FRIEND_OF_LOCATION +
                '/' + Utils.encodeEmail(mUserEmail);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(friendOfUrl);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    update.put(Constants.FIREBASE_USER_FRIENDS_LOCATION + '/' +  snapshot.getKey()
                            + '/' + Utils.encodeEmail(mUserEmail)
                            + '/' + Constants.PROPERTY_USER_NAME, mUserName);
                }
                FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(Constants.FIREBASE_URL)
                        .updateChildren(update);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(getActivity().getClass().getSimpleName(),
                        getActivity().getString(R.string.log_error_the_read_failed) +
                                databaseError.getMessage());
            }
        });


    }
}

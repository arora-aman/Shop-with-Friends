package com.aman_arora.firebase.swf.ui.user_profile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class ReAuthenticateDialogFragment extends android.support.v4.app.DialogFragment{

    private static final String TAG = ReAuthenticateDialogFragment.class.getSimpleName();
    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    private FirebaseUser userToValidate;

    public static ReAuthenticateDialogFragment newInstance(HashMap<String, Object> currentUser){
        ReAuthenticateDialogFragment fragment = new ReAuthenticateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_CURRENT_USER, currentUser);
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments().getSerializable(Constants.KEY_CURRENT_USER) != null;
        userToValidate = (FirebaseUser) ((HashMap<String, Object>)getArguments().getSerializable(Constants.KEY_CURRENT_USER))
                .get(Constants.KEY_CURRENT_USER);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_re_authenticate, null);
        mEditTextEmailInput = (EditText) view.findViewById(R.id.re_auth_edit_text_email);
        mEditTextPasswordInput = (EditText) view.findViewById(R.id.re_auth_edit_text_password);
        Button mValidateUser = (Button) view.findViewById(R.id.re_auth_validate);

        mValidateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateDetails())onValidateClick();
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public boolean validateDetails(){
        boolean validEmail = false, validPassword = false;
        if(mEditTextEmailInput.getText().toString().length() <= 0)
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
        else validEmail = true;
        if(mEditTextPasswordInput.getText().length() <= 0)
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
        else validPassword = true;

        return  validEmail && validPassword;
    }

    public void onValidateClick(){
        AuthCredential credential = EmailAuthProvider
                .getCredential(mEditTextEmailInput.getText().toString().trim(),
                        mEditTextPasswordInput.getText().toString());

        userToValidate.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getActivity(), R.string.error_validating_user, Toast.LENGTH_SHORT)
                                    .show();
                        }
                        dismiss();
                    }
                });
    }
}

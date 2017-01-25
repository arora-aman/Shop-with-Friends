package com.aman_arora.firebase.swf.ui.ActiveListsDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;

import java.util.HashMap;

public abstract class EditListDialogFragment extends DialogFragment {

    private EditText mEditText;
    private int mResource;
    private String mCurrentName;

    protected static Bundle newInstanceHelper(String currentName, int resource, ShoppingList shoppingList,
                                              String encodedEmail, HashMap<String, User> sharedWithList) {
        Bundle params = new Bundle();
        params.putInt(Constants.KEY_LAYOUT_RESOURCE, resource);
        params.putString(Constants.KEY_CURRENT_NAME, currentName);
        params.putString(Constants.KEY_SHOPPING_LIST_OWNER, shoppingList.getOwner());
        params.putString(Constants.PREFERENCE_ENCODED_EMAIL, encodedEmail);
        params.putSerializable(Constants.KEY_SHARED_WITH_USERS, sharedWithList);
        return params;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResource = getArguments().getInt(Constants.KEY_LAYOUT_RESOURCE);
        mCurrentName = getArguments().getString(Constants.KEY_CURRENT_NAME);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        super.onActivityCreated(savedInstanceState);
    }

    protected Dialog dialogCreateHelper(int stringResource) {
        //stringResource for the positive button click

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(mResource, null, false);
        mEditText = (EditText) rootView.findViewById(R.id.edit_text_list_dialog);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                   onPositiveClick();
                    dismiss();
                }
                return true;
            }
        });

        setDefaultTextEditText(mCurrentName);

        builder.setView(rootView);

        builder.setPositiveButton(stringResource, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onPositiveClick();
            }
        });

        builder.setNegativeButton(R.string.negative_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });


        return builder.create();
    }

    protected void setDefaultTextEditText(String defaultText) {
        mEditText.setText(defaultText);
        mEditText.setSelection(defaultText.length());
    }

    private void onPositiveClick(){
        if (mEditText.getText().toString().length() == 0)
            mEditText.setError("List name can't be empty!");
        else {
            doListEdit();
            dismiss();
        }
    }
    protected abstract void doListEdit();

    protected String getInput() {
        return mEditText.getText().toString();
    }
}

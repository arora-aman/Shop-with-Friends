package com.aman_arora.firebase.swf.ui.activeLists;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;


public class AddListDialogFragment extends DialogFragment {

    private String encodedEmail;
    EditText mEditTextListName;


    public static AddListDialogFragment newInstance(String encodedEmail) {
        AddListDialogFragment addListDialogFragment = new AddListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PREFERENCE_ENCODED_EMAIL, encodedEmail);
        addListDialogFragment.setArguments(bundle);
        return addListDialogFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        encodedEmail = getArguments().getString(Constants.PREFERENCE_ENCODED_EMAIL);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_list, null);
        mEditTextListName = (EditText) rootView.findViewById(R.id.edit_text_list_name);


        mEditTextListName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    addShoppingList();
                }
                return true;
            }
        });

        builder.setView(rootView)
                .setPositiveButton(R.string.positive_button_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addShoppingList();
                    }
                });

        return builder.create();
    }

    public void addShoppingList() {
        String inputListName = mEditTextListName.getText().toString();
        if(inputListName.equals(""))return;
        DatabaseReference newListReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USER_LISTS_URL).child(encodedEmail).push();
//        DatabaseReference newList = databaseReference.push();
        HashMap<String, Object> dateCreated = new HashMap<>();
        dateCreated.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);
        ShoppingList shoppingList = new ShoppingList(encodedEmail, inputListName, dateCreated);
//        newList.setValue(shoppingList);

        HashMap<String, Object> newList = new HashMap<>();
        newList = Utils.createUpdatePackage(newList, encodedEmail, newListReference.getKey(), "", shoppingList, null);
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_URL);
        ref.updateChildren(newList);


    }

}


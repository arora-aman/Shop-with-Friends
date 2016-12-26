package com.aman_arora.firebase.swf.ui.ActiveListsDetails;

import android.app.Dialog;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class EditListItemDialog extends EditListDialogFragment {


    private String mListPushID;
    private String mItemPushID;

    public EditListItemDialog() {
    }

    public static EditListDialogFragment newInstance(String currentName, String listPushID, String itemPushId){
        EditListItemDialog editListItemDialog = new EditListItemDialog();
        Bundle args = EditListDialogFragment.newInstanceHelper(currentName, R.layout.dialog_edit_item);
        args.putString(Constants.KEY_PUSH_ID_ACTIVE_LIST, listPushID);
        args.putString(Constants.KEY_PUSH_ID_LIST_ITEM, itemPushId);
        editListItemDialog.setArguments(args);
        return editListItemDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListPushID = getArguments().getString(Constants.KEY_PUSH_ID_ACTIVE_LIST);
        mItemPushID = getArguments().getString(Constants.KEY_PUSH_ID_LIST_ITEM);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialogCreateHelper(R.string.positive_button_edit_item);
    }

    @Override
    protected void doListEdit() {

        HashMap<String, Object> update = new HashMap<>() ;

        HashMap<String, Object> timeStamp = new HashMap<>();
        timeStamp.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);

        HashMap<String, Object> newItemName = new HashMap<>();
        newItemName.put(Constants.FIREBASE_ITEM_NAME, getInput());

        update.put(Constants.FIREBASE_ACTIVE_LISTS_LOCATION + '/' + mListPushID + '/' + Constants.FIREBASE_PROPERTY_TIMESTAMP_UPDATED, timeStamp);
        update.put(Constants.LIST_ITEMS_LOCATION + '/' + mListPushID + '/' + '/' + mItemPushID + '/' + Constants.FIREBASE_ITEM_NAME, getInput());

        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL).updateChildren(update);


    }
}

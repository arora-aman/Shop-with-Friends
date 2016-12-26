package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.Dialog;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class EditListNameDialogFragment extends  EditListDialogFragment {
    private String mPushID;

    public static EditListDialogFragment newInstance(String pushID, String currentName, ShoppingList shoppingList){
        EditListDialogFragment editListDialogFragment = new EditListNameDialogFragment();
        Bundle params = EditListDialogFragment.newInstanceHelper(currentName, R.layout.dialog_edit_list);
        params.putString(Constants.KEY_PUSH_ID_ACTIVE_LIST, pushID);
        editListDialogFragment.setArguments(params);
        return  editListDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle params = getArguments();
        mPushID = params.getString(Constants.KEY_PUSH_ID_ACTIVE_LIST);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialogCreateHelper(R.string.positive_button_edit_item);
    }

    @Override
    protected void doListEdit() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_ACTIVE_LISTS_URL).child(mPushID);

        HashMap<String, Object> updatedShoppingList = new HashMap<>();
        updatedShoppingList.put(Constants.LIST_NAME_LOCATION, getInput());

        HashMap<String, Object> updatedDateStamp = new HashMap<>();
        updatedDateStamp.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);
        updatedShoppingList.put(Constants.FIREBASE_PROPERTY_TIMESTAMP_UPDATED, updatedDateStamp);

        ref.updateChildren(updatedShoppingList);
    }

}

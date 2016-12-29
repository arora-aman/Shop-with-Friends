package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.Dialog;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditListNameDialogFragment extends  EditListDialogFragment {
    private String mPushID;
    private String mListOwner;
    private String mOriginalName;
    private String mCurrentOwner;

    public static EditListDialogFragment newInstance(String pushID, String currentName, ShoppingList shoppingList){
        EditListDialogFragment editListDialogFragment = new EditListNameDialogFragment();
        Bundle params = EditListDialogFragment.newInstanceHelper(currentName, R.layout.dialog_edit_list, shoppingList);
        params.putString(Constants.KEY_PUSH_ID_USER_LIST, pushID);
        editListDialogFragment.setArguments(params);
        return  editListDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle params = getArguments();
        mPushID = params.getString(Constants.KEY_PUSH_ID_USER_LIST);
        mListOwner = params.getString(Constants.KEY_SHOPPING_LIST_OWNER);
        mCurrentOwner = getArguments().getString(Constants.KEY_SHOPPING_LIST_OWNER);
        mOriginalName = getArguments().getString(Constants.KEY_CURRENT_NAME);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialogCreateHelper(R.string.positive_button_edit_item);
    }

    @Override
    protected void doListEdit() {

        String newName = getInput();

        if(newName.equals("") || newName.equals(mOriginalName)) return;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL);

        HashMap<String, Object> updatedShoppingList = new HashMap<>();

        Utils.createUpdatePackage(updatedShoppingList, mCurrentOwner, mPushID, Constants.LIST_NAME_LOCATION, newName);
        Utils.createTimeStampUpdatePackage(updatedShoppingList, mCurrentOwner, mPushID);

        ref.updateChildren(updatedShoppingList);
    }

}

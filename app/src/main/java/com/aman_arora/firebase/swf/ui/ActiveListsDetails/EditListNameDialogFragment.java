package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.Dialog;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditListNameDialogFragment extends  EditListDialogFragment {
    private String mPushID;
    private String mOriginalName;
    private String mListOwner;
    private HashMap<String, User> mSharedWithUsersList;

    public static EditListDialogFragment newInstance(String pushID, String currentName, ShoppingList shoppingList,
                                    String encodedEmail,  HashMap<String, User> sharedWithList){
        EditListDialogFragment editListDialogFragment = new EditListNameDialogFragment();
        Bundle params = EditListDialogFragment.newInstanceHelper(currentName, R.layout.dialog_edit_list,
                shoppingList, encodedEmail, sharedWithList);
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
        mListOwner = getArguments().getString(Constants.KEY_SHOPPING_LIST_OWNER);
        mOriginalName = getArguments().getString(Constants.KEY_CURRENT_NAME);
        mSharedWithUsersList = (HashMap<String, User>) getArguments().getSerializable(Constants.KEY_SHARED_WITH_USERS);
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

        Utils.createUpdatePackage(updatedShoppingList, mListOwner, mPushID, Constants.LIST_NAME_LOCATION, newName, mSharedWithUsersList);
        Utils.createTimeStampUpdatePackage(updatedShoppingList, mListOwner, mPushID, mSharedWithUsersList);
        ref.updateChildren(updatedShoppingList);
    }

}

package com.aman_arora.firebase.swf.ui.ActiveListsDetails;

import android.app.Dialog;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditListItemDialog extends EditListDialogFragment {


    private String mListPushID;
    private String mItemPushID;
    private String mCurrentOwner;
    private String mOriginalName;
    private HashMap<String, User> mSharedWithUsersList;

    public EditListItemDialog() {
    }

    public static EditListDialogFragment newInstance(String currentName, String listPushID,
                                                     String itemPushId, ShoppingList shoppingList,
                                                     String encodedEmail, HashMap<String, User> sharedWithList){
        EditListItemDialog editListItemDialog = new EditListItemDialog();
        Bundle args = EditListDialogFragment.newInstanceHelper(currentName, R.layout.dialog_edit_item, shoppingList,
                encodedEmail, sharedWithList);
        args.putString(Constants.KEY_PUSH_ID_USER_LIST, listPushID);
        args.putString(Constants.KEY_PUSH_ID_LIST_ITEM, itemPushId);
        editListItemDialog.setArguments(args);
        return editListItemDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListPushID = getArguments().getString(Constants.KEY_PUSH_ID_USER_LIST);
        mItemPushID = getArguments().getString(Constants.KEY_PUSH_ID_LIST_ITEM);
        mCurrentOwner = getArguments().getString(Constants.KEY_SHOPPING_LIST_OWNER);
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

        HashMap<String, Object> update = new HashMap<>() ;
        Utils.createTimeStampUpdatePackage(update, mCurrentOwner, mListPushID, mSharedWithUsersList);
        update.put(Constants.LIST_ITEMS_LOCATION + '/' + mListPushID + '/' + '/' + mItemPushID +
                '/' + Constants.FIREBASE_ITEM_NAME, newName );

        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL).updateChildren(update);


    }
}

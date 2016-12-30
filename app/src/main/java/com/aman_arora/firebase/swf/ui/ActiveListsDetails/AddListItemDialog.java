package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.Dialog;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ListItem;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddListItemDialog extends EditListDialogFragment {

    private String mPushId;
    private String encodedEmail;
    private String mCurrentOwner;
    private HashMap<String, User> mSharedWithUsersList;
    public static AddListItemDialog newInstance(String pushID, String encodedEmail,
                                                ShoppingList shoppingList, HashMap<String, User> sharedWithList){
        AddListItemDialog addListItemDialog = new AddListItemDialog();
        Bundle args = EditListDialogFragment.newInstanceHelper("", R.layout.dialog_add_item,
                shoppingList, encodedEmail, sharedWithList);
        args.putString(Constants.KEY_PUSH_ID_USER_LIST, pushID);
        addListItemDialog.setArguments(args);
        return addListItemDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPushId = getArguments().getString(Constants.KEY_PUSH_ID_USER_LIST);
        encodedEmail = getArguments().getString(Constants.PREFERENCE_ENCODED_EMAIL);
        mCurrentOwner = getArguments().getString(Constants.KEY_SHOPPING_LIST_OWNER);
        mSharedWithUsersList = (HashMap<String, User>) getArguments().getSerializable(Constants.KEY_SHARED_WITH_USERS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialogCreateHelper(R.string.positive_button_add_list_item);
    }

    @Override
    protected void doListEdit() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL);

        DatabaseReference newItem = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_ITEM_URL).child(mPushId).push();

        HashMap<String, Object> update = new HashMap<>();
        update.put(Constants.LIST_ITEMS_LOCATION + '/' + mPushId + '/' + newItem.getKey(), new ListItem(getInput(), encodedEmail));
        update = Utils.createTimeStampUpdatePackage(update, mCurrentOwner, mPushId, mSharedWithUsersList);

        reference.updateChildren(update);
    }
}

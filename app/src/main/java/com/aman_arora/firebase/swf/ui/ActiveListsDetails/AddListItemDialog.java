package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.Dialog;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ListItem;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class AddListItemDialog extends EditListDialogFragment {

    private String mPushId;

    public static AddListItemDialog newInstance(String pushID){
        AddListItemDialog addListItemDialog = new AddListItemDialog();
        Bundle args = EditListDialogFragment.newInstanceHelper("", R.layout.dialog_add_item);
        args.putString(Constants.KEY_PUSH_ID_ACTIVE_LIST, pushID);
        addListItemDialog.setArguments(args);
        return addListItemDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPushId = getArguments().getString(Constants.KEY_PUSH_ID_ACTIVE_LIST);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialogCreateHelper(R.string.positive_button_add_list_item);
    }

    @Override
    protected void doListEdit() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL);

        DatabaseReference newItem = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_ITEM_URL).child(mPushId).push();

        HashMap<String, Object> timeStamp  = new HashMap<>();
        timeStamp.put(Constants.TIMESTAMP_OBJECT_KEY, ServerValue.TIMESTAMP);

        HashMap<String, Object> update = new HashMap<>();
        update.put(Constants.FIREBASE_ACTIVE_LISTS_LOCATION + '/' + mPushId + '/' + Constants.FIREBASE_PROPERTY_TIMESTAMP_UPDATED, timeStamp);

        update.put(Constants.LIST_ITEMS_LOCATION + '/' + mPushId + '/' + newItem.getKey(), new ListItem(getInput(), "BKL"));

        reference.updateChildren(update);
    }
}

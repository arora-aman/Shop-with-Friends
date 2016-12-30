package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RemoveListDialogFragment extends android.support.v4.app.DialogFragment {

    private String pushID;
    private String mListOwner;
    private HashMap<String, User> mSharedWithUsersList;

    public RemoveListDialogFragment() {
    }

    public static RemoveListDialogFragment newInstance(String pushID, ShoppingList shoppingList,
                                                       HashMap<String, User> sharedWithUsersList){

        RemoveListDialogFragment removeListDialogFragment = new RemoveListDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_PUSH_ID_USER_LIST, pushID);
        args.putString(Constants.KEY_SHOPPING_LIST_OWNER, shoppingList.getOwner());
        args.putSerializable(Constants.KEY_SHARED_WITH_USERS, sharedWithUsersList);
        removeListDialogFragment.setArguments(args);
        return  removeListDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        pushID = args.getString(Constants.KEY_PUSH_ID_USER_LIST);
        mListOwner = args.getString(Constants.KEY_SHOPPING_LIST_OWNER);
        mSharedWithUsersList = (HashMap<String, User>) getArguments().getSerializable(Constants.KEY_SHARED_WITH_USERS);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomTheme_Dialog);
        builder.setTitle(getString(R.string.action_remove_list));
        builder.setMessage(getString(R.string.dialog_message_are_you_sure_remove_list));
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                HashMap<String, Object> update = new HashMap<String, Object>();

                Utils.createUpdatePackage(update, mListOwner, pushID,"", null, mSharedWithUsersList);
                update.put(Constants.FIREBASE_SHARED_WITH_LOCATION + '/' + pushID, null);
                update.put(Constants.LIST_ITEMS_LOCATION + '/' + pushID, null);
                FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL)
                        .updateChildren(update);


            }
        });
        builder.setNegativeButton(getString(R.string.negative_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });


        return builder.create();
    }
}

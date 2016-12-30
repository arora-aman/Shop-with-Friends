package com.aman_arora.firebase.swf.ui.ActiveListsDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RemoveListItemDialogFragment extends DialogFragment {

    private String mListPushID;
    private String mItemPushID;
    private String mShoppingListOwner;
    private HashMap<String, User> mSharedWithUsersList;

    public RemoveListItemDialogFragment() {
    }

     public static RemoveListItemDialogFragment newInstance(String listOwner, String listPushId,
                                                            String itemPushId, HashMap<String, User> sharedWithUsersList ) {

        Bundle args = new Bundle();
        args.putString(Constants.KEY_PUSH_ID_USER_LIST, listPushId);
        args.putString(Constants.KEY_PUSH_ID_LIST_ITEM, itemPushId);
        args.putString(Constants.KEY_SHOPPING_LIST_OWNER, listOwner);
        args.putSerializable(Constants.KEY_SHARED_WITH_USERS, sharedWithUsersList);
        RemoveListItemDialogFragment fragment = new RemoveListItemDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mListPushID = args.getString(Constants.KEY_PUSH_ID_USER_LIST);
        mItemPushID = args.getString(Constants.KEY_PUSH_ID_LIST_ITEM);
        mShoppingListOwner = args.getString(Constants.KEY_SHOPPING_LIST_OWNER);
        mSharedWithUsersList = (HashMap<String, User>) getArguments().getSerializable(Constants.KEY_SHARED_WITH_USERS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        builder.setTitle(getString(R.string.remove_item_option));
        builder.setMessage(getString(R.string.dialog_message_are_you_sure_remove_item));
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, Object> update = new HashMap<>();
                update.put(Constants.LIST_ITEMS_LOCATION + '/' + mListPushID + '/' + mItemPushID, null);
                Utils.createTimeStampUpdatePackage(update, mShoppingListOwner, mListPushID, mSharedWithUsersList);
                FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL).updateChildren(update);
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

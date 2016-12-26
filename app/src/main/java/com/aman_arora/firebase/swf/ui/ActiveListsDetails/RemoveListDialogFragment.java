package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RemoveListDialogFragment extends android.support.v4.app.DialogFragment {

    private String pushID;

    public RemoveListDialogFragment() {
    }

    public static RemoveListDialogFragment newInstance(String pushID){

        RemoveListDialogFragment removeListDialogFragment = new RemoveListDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_PUSH_ID_ACTIVE_LIST, pushID);
        removeListDialogFragment.setArguments(args);
        return  removeListDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        pushID = args.getString(Constants.KEY_PUSH_ID_ACTIVE_LIST);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomTheme_Dialog);
        builder.setTitle("Delete list");
        builder.setMessage("Are you sure? This can't be undone!");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                HashMap<String, Object> removeList = new HashMap<String, Object>();
                removeList.put(Constants.FIREBASE_ACTIVE_LISTS_LOCATION + '/' + pushID, null);

                removeList.put(Constants.LIST_ITEMS_LOCATION + '/' + pushID, null);

                FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL).updateChildren(removeList);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });


        return builder.create();
    }
}

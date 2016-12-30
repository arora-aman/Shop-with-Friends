package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.Activity;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ListItem;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ListItemAdapter extends FirebaseListAdapter<ListItem> {

    private static final String TAG = "ListItemAdapter";
    private String mListPushID;
    private Activity mActivity;
    private TextView itemName;
    private String mCurrentUser;
    private String mListOwner;
    private TextView boughtBy;
    private TextView boughtByUser;
    private ImageButton deleteList;
    private boolean showDeleteButton = false;
    private HashMap<String, User> mSharedWithUsersList;

    public ListItemAdapter(Activity activity, Class<ListItem> modelClass, int modelLayout, Query ref,
                           String listPushId, String currentUser) {
        super(activity, modelClass, modelLayout, ref);
        mListPushID = listPushId;
        mActivity = activity;
        mCurrentUser = currentUser;
    }

    public void setListOwner(String listOwner) {
        this.mListOwner = listOwner;
    }

    public void setSharedWithUsersList(HashMap<String, User> sharedWithUsersList) {
        this.mSharedWithUsersList = sharedWithUsersList;
    }

    @Override
    protected void populateView(View view, final ListItem model, final int position) {
        itemName = (TextView) view.findViewById(R.id.text_view_active_list_item_name);
        deleteList = (ImageButton) view.findViewById(R.id.button_remove_item);
        boughtBy = (TextView) view.findViewById(R.id.text_view_bought_by);
        boughtByUser = (TextView) view.findViewById(R.id.text_view_bought_by_user);
        itemName.setText(model.getItemName());

        if(mCurrentUser.equals(mListOwner) || mCurrentUser.equals(model.getOwner())){
            showDeleteButton = true;
        }else deleteList.setVisibility(View.INVISIBLE);



        deleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveListItemDialogFragment dialog = RemoveListItemDialogFragment
                        .newInstance(mListOwner, mListPushID, getRef(position).getKey(), mSharedWithUsersList);
                dialog.show(mActivity.getFragmentManager(), "RemoveListItem");

            }
        });

        if(model.isBought())onBought(model);
        else onNotBought();
    }

    private void onBought(ListItem listItem) {
        deleteList.setVisibility(View.INVISIBLE);
        itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        boughtBy.setVisibility(View.VISIBLE);
        boughtByUser.setVisibility(View.VISIBLE);
        if (mCurrentUser.equals(listItem.getBoughtBy())) boughtByUser.setText(R.string.text_you);
        else {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Constants.FIREBASE_USERS_URL).child(mCurrentUser);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) boughtByUser.setText(user.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(mActivity.getClass().getSimpleName(),
                            mActivity.getString(R.string.log_error_the_read_failed) +
                                    databaseError.getMessage());

                }
            });
        }

    }

    private void onNotBought(){
        if(showDeleteButton)deleteList.setVisibility(View.VISIBLE);
        itemName.setPaintFlags(itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        boughtBy.setVisibility(View.INVISIBLE);
        boughtByUser.setVisibility(View.INVISIBLE);
    }
}

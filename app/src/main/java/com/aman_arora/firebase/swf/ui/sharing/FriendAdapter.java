package com.aman_arora.firebase.swf.ui.sharing;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.aman_arora.firebase.swf.utils.Utils;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FriendAdapter extends FirebaseListAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    private String mListPushID;
    private HashMap<DatabaseReference, ValueEventListener> mLocationListenerMap;
    private HashMap<String, User> mSharedUsersList;
    private ShoppingList mShoppingList;
    private DatabaseReference firebaseRef;


    public FriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                         Query ref, String listPushID) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListPushID = listPushID;
        this.firebaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL);
        mLocationListenerMap = new HashMap<>();
    }


    public void setShoppingList(ShoppingList shoppingList) {
        mShoppingList = shoppingList;
        this.notifyDataSetChanged();
    }

    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {
        mSharedUsersList = sharedUsersList;
        Log.d("TAG", "updateFriendInSharedWith2: " + mSharedUsersList.toString());
        this.notifyDataSetChanged();
    }


    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend, User friendToAddOrRemove) {

        HashMap<String, User> newSharedWith = new HashMap<String, User>(mSharedUsersList);
        HashMap<String, Object> update = new HashMap<>();
        User updateValue;
        ShoppingList updateList;
        if (addFriend) {
            mShoppingList.setTimestampLastChangedToNow();
            updateValue = friendToAddOrRemove;
            updateList = mShoppingList;
        } else {
            updateValue = null;
            updateList = null;
            newSharedWith.remove(friendToAddOrRemove.getEmail());
        }

        String updateLocation = Constants.FIREBASE_SHARED_WITH_LOCATION + '/' + mListPushID
                + '/' + friendToAddOrRemove.getEmail();
        String updateListLocation = Constants.FIREBASE_USER_LISTS_LOCATION + '/' +
                friendToAddOrRemove.getEmail() + '/' + mListPushID;

        update.put(updateLocation, updateValue);
        update.put(updateListLocation, updateList);
        Log.d("TAG", "updateFriendInSharedWith: " + newSharedWith.toString());
        Utils.createTimeStampUpdatePackage(update, mShoppingList.getOwner(), mListPushID, newSharedWith);
        return update;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        for (Map.Entry<DatabaseReference, ValueEventListener> entry : mLocationListenerMap.entrySet()) {
            ValueEventListener valueEventListener = entry.getValue();
            DatabaseReference databaseReference = entry.getKey();
            if (valueEventListener != null)
                databaseReference.removeEventListener(valueEventListener);
        }
    }

    @Override
    protected void populateView(View v, final User user, int position) {
        TextView userName = (TextView) v.findViewById(R.id.user_name);
        userName.setText(user.getName());
        final ImageButton toggleShare= (ImageButton) v.findViewById(R.id.button_toggle_share);

        DatabaseReference sharedWithFriend = FirebaseDatabase.getInstance().getReferenceFromUrl(
                Constants.FIREBASE_SHARED_WITH_URL).child(mListPushID).child(user.getEmail());
        Log.d(LOG_TAG, "populateView: " + user.getEmail());

        ValueEventListener sharedWithValueEventListener =
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(User.class) == null) {
                            Log.d(LOG_TAG, "onDataChange:3111" + user.getEmail());
                            toggleShare.setImageDrawable(
                                    mActivity.getResources().getDrawable(R.drawable.icon_add_friend));
                            toggleShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG_TAG, "onClick: false");
                                    HashMap<String, Object> updatedUserData =
                                            updateFriendInSharedWith(true, user);
                                    firebaseRef.updateChildren(updatedUserData);
                                }
                            });
                        } else {
                            Log.d(LOG_TAG, "onDataChange: 2131" + user.getEmail());
                            toggleShare.setImageDrawable(
                                    mActivity.getResources().getDrawable(R.drawable.ic_shared_check));
                            toggleShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG_TAG, "onClick: true");
                                    HashMap<String, Object> updatedUserData =
                                            updateFriendInSharedWith(false, user);
                                    firebaseRef.updateChildren(updatedUserData);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(mActivity.getClass().getSimpleName(),
                                mActivity.getString(R.string.log_error_the_read_failed) +
                                        databaseError.getMessage());
                    }
                };

        sharedWithFriend.addValueEventListener(sharedWithValueEventListener);

        mLocationListenerMap.put(sharedWithFriend, sharedWithValueEventListener);
    }


}
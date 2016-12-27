package com.aman_arora.firebase.swf.ui.sharing;

import android.app.Activity;
import android.view.View;

import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FriendAdapter extends FirebaseListAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    private HashMap <DatabaseReference, ValueEventListener> mLocationListenerMap;


    public FriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                         Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }


    public void setShoppingList(ShoppingList shoppingList) {

    }

    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {

    }


    /**
     * This method does the tricky job of adding or removing a friend from the sharedWith list.
     * @param addFriend This is true if the friend is being added, false is the friend is being removed.
     * @param friendToAddOrRemove This is the friend to either add or remove
     * @return
     */
    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend, User friendToAddOrRemove) {
        return null;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    protected void populateView(View v, User model, int position) {

    }
}
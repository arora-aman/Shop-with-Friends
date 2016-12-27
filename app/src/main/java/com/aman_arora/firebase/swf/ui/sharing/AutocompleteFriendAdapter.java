package com.aman_arora.firebase.swf.ui.sharing;



import android.app.Activity;
import android.view.View;

import com.aman_arora.firebase.swf.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;


public class AutocompleteFriendAdapter extends FirebaseListAdapter<User> {


    public AutocompleteFriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                                     Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    @Override
    protected void populateView(View v, User model, int position) {

    }

    private boolean isNotCurrentUser(User user) {
        return true;
    }


    private boolean isNotAlreadyAdded(DataSnapshot dataSnapshot, User user) {
        return true;
    }

}
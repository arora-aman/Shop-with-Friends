package com.aman_arora.firebase.swf.ui.sharing;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aman_arora.firebase.swf.R;
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


public class AutocompleteFriendAdapter extends FirebaseListAdapter<User> {

    private String mEncodedEmail;
    private DatabaseReference friendListRef;


    public AutocompleteFriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                                     Query ref, String encodedEmail) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mEncodedEmail = encodedEmail;
    }

    @Override
    protected void populateView(View v, final User model, int position) {
        TextView friendEmailTV = (TextView) v.findViewById(R.id.text_view_autocomplete_item);
        friendEmailTV.setText(Utils.decodeEmail(model.getEmail()));
        friendListRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USER_FRIENDS_URL)
                .child(mEncodedEmail);

        friendEmailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNotCurrentUser(model)) return;

                final DatabaseReference friendRef = friendListRef.child(model.getEmail());

                friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(isNotAlreadyAdded(dataSnapshot, model))
                            friendRef.setValue(model);
                            mActivity.finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(mActivity.getClass().getSimpleName(),
                                mActivity.getString(R.string.log_error_the_read_failed) +
                                        databaseError.getMessage());

                    }
                });

            }
        });

    }

    private boolean isNotCurrentUser(User user) {
        boolean isCurrentUser = user.getEmail().equals(mEncodedEmail);
        if (isCurrentUser) Toast.
                makeText(mActivity, mActivity.getResources().getString(R.string.toast_you_cant_add_yourself), Toast.LENGTH_LONG)
                .show();

        return !isCurrentUser;
    }


    private boolean isNotAlreadyAdded(DataSnapshot dataSnapshot, User user) {

        if (dataSnapshot.getValue() != null) {
            String errorMessage = mActivity.getResources()
                    .getString(R.string.toast_is_already_your_friend, user.getName());
            Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}
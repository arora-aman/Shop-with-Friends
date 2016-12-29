package com.aman_arora.firebase.swf.ui.activeLists;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.utils.Constants;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ActiveListsAdapter  extends FirebaseListAdapter<ShoppingList>{

    private String mEncodedEmail;
    private TextView userName;


    public ActiveListsAdapter(Activity activity, Class<ShoppingList> modelClass, int modelLayout, Query ref, String encodedEmail) {
        super(activity, modelClass, modelLayout, ref);
        mEncodedEmail = encodedEmail;
    }

    @Override
    protected void populateView(final View view, ShoppingList model, int position) {

        userName = (TextView)view.findViewById(R.id.text_view_created_by_user);
        TextView shoppingUsers = (TextView) view.findViewById(R.id.text_view_people_shopping_count);

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS_URL)
                .child(model.getOwner());

        if(mEncodedEmail.equals(model.getOwner()))userName.setText(mActivity.getString(R.string.text_you));
        else{
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    (userName).setText(user.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        String shoppingUsersText;
        if(model.getShoppingUsers() != null){
            switch (model.getShoppingUsers().size()) {
                case 1:
                    shoppingUsersText = mActivity.getString(R.string.person_shopping)
                            .replace("%d", "1");
                    break;
                default:
                    shoppingUsersText = mActivity.getString(R.string.people_shopping)
                            .replace("%d", String.valueOf(model.getShoppingUsers().size()));
                    break;
            }
            shoppingUsers.setText(shoppingUsersText);
        }

        ((TextView)view.findViewById(R.id.text_view_list_name)).setText(model.getListName());
    }
}

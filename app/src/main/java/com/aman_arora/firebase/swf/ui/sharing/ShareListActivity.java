package com.aman_arora.firebase.swf.ui.sharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.ui.BaseActivity;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ShareListActivity extends BaseActivity {
    private static final String LOG_TAG = ShareListActivity.class.getSimpleName();
    private ListView mListView;
    private DatabaseReference userFriendsRef;
    private DatabaseReference sharedWithRef;
    private DatabaseReference listRef;
    private String mListPushID;
    private FriendAdapter adapter;
    private ValueEventListener sharedFriendsValueEventListener;
    private ValueEventListener listValueEventListener;
    private ShoppingList mShoppingList;
    private  HashMap<String, User> sharedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);
        initializeScreen();
        mListPushID = getIntent().getStringExtra(Constants.KEY_PUSH_ID_USER_LIST);

        listRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USER_LISTS_URL)
                .child(mEncodedEmail)
                .child(mListPushID);

        userFriendsRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USER_FRIENDS_URL).child(mEncodedEmail);
        sharedWithRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_SHARED_WITH_URL).child(mListPushID);

        adapter = new FriendAdapter(this, User.class,
                R.layout.single_user_item, userFriendsRef, mListPushID);

        listValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                if (shoppingList != null) {
                    mShoppingList = shoppingList;
                    adapter.setShoppingList(mShoppingList);
                }else{
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG,
                        ShareListActivity.this.getString(R.string.log_error_the_read_failed) +
                                databaseError.getMessage());
            }
        };

        listRef.addValueEventListener(listValueEventListener);

        sharedFriendsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sharedUsers = new HashMap<>();
                for (DataSnapshot currentUser : dataSnapshot.getChildren()) {
                    sharedUsers.put(currentUser.getKey(), currentUser.getValue(User.class));
                }
                adapter.setSharedWithUsers(sharedUsers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG,
                        ShareListActivity.this.getString(R.string.log_error_the_read_failed) +
                                databaseError.getMessage());
            }
        };

        sharedWithRef.addValueEventListener(sharedFriendsValueEventListener);


        mListView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        adapter.cleanup();
        if (sharedFriendsValueEventListener != null)
            sharedWithRef.removeEventListener(sharedFriendsValueEventListener);
        if(listValueEventListener != null)
            listRef.removeEventListener(listValueEventListener);
        super.onDestroy();
    }


    public void initializeScreen() {
        mListView = (ListView) findViewById(R.id.list_view_friends_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onAddFriendPressed(View view) {
        Intent intent = new Intent(ShareListActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

}
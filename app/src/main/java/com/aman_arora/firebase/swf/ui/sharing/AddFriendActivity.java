package com.aman_arora.firebase.swf.ui.sharing;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.ui.BaseActivity;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFriendActivity extends BaseActivity {
    private EditText mEditTextAddFriendEmail;
    private ListView mListViewAutocomplete;

    private DatabaseReference allUsersRef;
    private AutocompleteFriendAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initializeScreen();

        allUsersRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS_URL);


        /**
         * Set interactive bits, such as click events/adapters
         */
        mEditTextAddFriendEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null)
                    adapter.cleanup();
                String wrapperText = s.toString().toLowerCase();
                if (wrapperText.length() > 1) {

                    adapter = new AutocompleteFriendAdapter(AddFriendActivity.this, User.class, R.layout.single_autocomplete_item,
                            allUsersRef.orderByChild("email").startAt(s.toString())
                                    .endAt(s.toString() + '~').limitToFirst(5),
                            mEncodedEmail);
                    mListViewAutocomplete.setAdapter(adapter);
                } else mListViewAutocomplete.setAdapter(null);


            }
        });
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (adapter != null)
            adapter.cleanup();
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        mListViewAutocomplete = (ListView) findViewById(R.id.list_view_friends_autocomplete);
        mEditTextAddFriendEmail = (EditText) findViewById(R.id.edit_text_add_friend_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        /* Add back button to the action bar */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}
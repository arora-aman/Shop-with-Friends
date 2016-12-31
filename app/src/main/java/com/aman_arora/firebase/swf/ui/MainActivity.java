package com.aman_arora.firebase.swf.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.User;
import com.aman_arora.firebase.swf.ui.activeLists.AddListDialogFragment;
import com.aman_arora.firebase.swf.ui.activeLists.ShoppingListsFragment;
import com.aman_arora.firebase.swf.ui.login.LoginActivity;
import com.aman_arora.firebase.swf.ui.meals.AddMealDialogFragment;
import com.aman_arora.firebase.swf.ui.meals.MealsFragment;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends BaseActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ValueEventListener mEmailValueEventListener;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeScreen();

        userRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_USERS_URL).child(mEncodedEmail);

        mEmailValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) setTitle(user.getName() + "'s Lists");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG,
                        MainActivity.this.getString(R.string.log_error_the_read_failed) +
                                databaseError.getMessage());
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addValueEventListener(mEmailValueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mEmailValueEventListener != null) userRef.removeEventListener(mEmailValueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.PREFERENCE_ENCODED_EMAIL, null);
                editor.putString(Constants.PREFERENCE_PROVIDER, null);
                editor.apply();
                editor.commit();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
        if(id == R.id.action_sort){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initializeScreen() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void showAddListDialog(View view) {
        DialogFragment dialog = AddListDialogFragment.newInstance(mEncodedEmail);
        dialog.show(MainActivity.this.getFragmentManager(), "AddListDialogFragment");
    }

    public void showAddMealDialog(View view) {
        DialogFragment dialog = AddMealDialogFragment.newInstance();
        dialog.show(MainActivity.this.getFragmentManager(), "AddMealDialogFragment");
    }

    public class SectionPagerAdapter extends FragmentStatePagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = ShoppingListsFragment.newInstance(mEncodedEmail);
                    break;
                case 1:
                    fragment = MealsFragment.newInstance();
                    break;
                default:
                    fragment = ShoppingListsFragment.newInstance(mEncodedEmail);
                    break;
            }

            return fragment;
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.pager_title_shopping_lists);
                case 1:
                default:
                    return getString(R.string.pager_title_meals);
            }
        }
    }
}

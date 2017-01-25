package com.aman_arora.firebase.swf.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.aman_arora.firebase.swf.ui.activeLists.AddListDialogFragment;
import com.aman_arora.firebase.swf.ui.activeLists.ShoppingListsFragment;
import com.aman_arora.firebase.swf.ui.login.LoginActivity;
import com.aman_arora.firebase.swf.ui.meals.AddMealDialogFragment;
import com.aman_arora.firebase.swf.ui.user_profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeScreen();
        onAuthChanged(FirebaseAuth.getInstance().getCurrentUser(), this);
        Log.d("mainActivity", "onCreate: " +  mEncodedEmail);
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
                writeEmailToSharedPreferences(null, null);
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
                    fragment = ProfileFragment.newInstance(mEncodedEmail);
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
                    return getString(R.string.pager_title_profile);
            }
        }
    }
}

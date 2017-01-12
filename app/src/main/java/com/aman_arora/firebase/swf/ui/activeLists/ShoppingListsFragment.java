package com.aman_arora.firebase.swf.ui.activeLists;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.ui.ActiveListsDetails.ActiveListsDetailsActivity;
import com.aman_arora.firebase.swf.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ShoppingListsFragment extends Fragment {
    private ListView mListView;
    private ActiveListsAdapter activeListsAdapter;
    private DatabaseReference shoppingListDatabase;
    private String mEncodedEmail;
    public String[] sortPreference;
    public ShoppingListsFragment() {

    }

    public static ShoppingListsFragment newInstance(String encodedEmail) {
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PREFERENCE_ENCODED_EMAIL, encodedEmail);
        fragment.setArguments(args);
        return fragment;
    }
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEncodedEmail = getArguments().getString(Constants.PREFERENCE_ENCODED_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        initializeScreen(rootView);

        sortPreference = getResources().getStringArray(R.array.pref_sortType_values_lists);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        shoppingListDatabase = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USER_LISTS_URL).child(mEncodedEmail);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPreferences.getString(Constants.KEY_PREF_SORT_ORDER_LISTS, Constants.ORDER_BY_KEY);
        Query sortQuery;

        if(sortOrder.equals(Constants.ORDER_BY_KEY)){
            sortQuery = shoppingListDatabase.orderByKey();
        }else{
            sortQuery = shoppingListDatabase.orderByChild(sortOrder);
        }

        activeListsAdapter = new ActiveListsAdapter(getActivity(), ShoppingList.class,
                R.layout.single_active_list, sortQuery, mEncodedEmail);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActiveListsDetailsActivity.class);
                intent.putExtra(Constants.KEY_PUSH_ID_USER_LIST, activeListsAdapter.getRef(position).getKey());
                startActivity(intent);
            }
        });

        mListView.setAdapter(activeListsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        activeListsAdapter.cleanup();
    }

    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_lists);
    }

}

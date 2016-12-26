package com.aman_arora.firebase.swf.ui.activeLists;


import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;


public class ShoppingListsFragment extends Fragment {
    private ListView mListView;
    private ActiveListsAdapter activeListsAdapter;
    private ValueEventListener eventListener;
    private DatabaseReference databaseRef;
    public ShoppingListsFragment() {

    }

    public static ShoppingListsFragment newInstance() {
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        Bundle args = new Bundle();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        initializeScreen(rootView);

        databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_ACTIVE_LISTS_URL);

        activeListsAdapter = new ActiveListsAdapter(getActivity(), ShoppingList.class, R.layout.single_active_list, databaseRef);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActiveListsDetailsActivity.class);
                intent.putExtra(Constants.KEY_PUSH_ID_ACTIVE_LIST, activeListsAdapter.getRef(position).getKey());
                startActivity(intent);
            }
        });

        mListView.setAdapter(activeListsAdapter);

        return rootView;
    }

    @Override
    public void onDestroy() {

        activeListsAdapter.cleanup();
        super.onDestroy();
    }

    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_lists);
    }

}

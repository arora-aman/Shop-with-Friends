package com.aman_arora.firebase.swf.ui.ActiveListsDetails;


import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ListItem;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

public class ListItemAdapter extends FirebaseListAdapter<ListItem> {

    private String mListPushID;
    private Activity mActivity;

    public ListItemAdapter(Activity activity, Class<ListItem> modelClass, int modelLayout, Query ref, String listPushId) {
        super(activity, modelClass, modelLayout, ref);
        mListPushID = listPushId;
        mActivity = activity;

    }

    @Override
    protected void populateView(View view, final ListItem model, final int position) {
        ((TextView)view.findViewById(R.id.text_view_active_list_item_name)).setText(model.getItemName());
        ((ImageButton)view.findViewById(R.id.button_remove_item)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveListItemDialogFragment dialog = RemoveListItemDialogFragment.newInstance(mListPushID, getRef(position).getKey());
                dialog.show(mActivity.getFragmentManager(), "RemoveListItem");

            }
        });
    }
}

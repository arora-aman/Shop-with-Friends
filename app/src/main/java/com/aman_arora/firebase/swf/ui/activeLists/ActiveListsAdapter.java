package com.aman_arora.firebase.swf.ui.activeLists;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.aman_arora.firebase.swf.R;
import com.aman_arora.firebase.swf.model.ShoppingList;
import com.aman_arora.firebase.swf.utils.Utils;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

public class ActiveListsAdapter  extends FirebaseListAdapter<ShoppingList>{

    public ActiveListsAdapter(Activity activity, Class<ShoppingList> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View view, ShoppingList model, int position) {

        ((TextView)view.findViewById(R.id.text_view_list_name)).setText(model.getListName());
        ((TextView)view.findViewById(R.id.created_by)).setText(model.getOwner());
        ((TextView)view.findViewById(R.id.text_view_created_by_user)).setText(Utils.SIMPLE_DATE_FORMAT.format(model.getTimeStampLong()));
        ((TextView)view.findViewById(R.id.text_view_edit_time)).setText(Utils.SIMPLE_DATE_FORMAT.format(model.getTimeStampLastUpdatedLong()));
    }
}

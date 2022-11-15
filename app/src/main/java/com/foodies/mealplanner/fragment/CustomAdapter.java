package com.foodies.mealplanner.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foodies.mealplanner.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<String> nameList;
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, List<String> nameList){
        this.context = applicationContext;
        this.nameList = nameList;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.users_listview, null);
        TextView names = view.findViewById(R.id.userView);
        names.setText(nameList.get(i));
        return view;
    }
}

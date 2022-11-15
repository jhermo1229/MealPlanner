package com.foodies.mealplanner.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class UsersListFragment extends Fragment {

    private View usersListFragmentView;
    public static final String TAG = UsersListFragment.class.getName();
    private final DatabaseHelper db = new DatabaseHelper();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<User> users = new ArrayList<>();
        List<String> fullName = new ArrayList<>();
        usersListFragmentView = inflater.inflate(R.layout.fragment_users_list, container, false);
        ListView list = usersListFragmentView.findViewById(R.id.userList);

                db.getAllUsers(userList -> {
                    users.addAll(userList);
            for(User user : userList) {

                fullName.add(user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName());
                Log.d("USERSSSSS", user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName());
            }
            });

                CustomAdapter adapter = new CustomAdapter(getContext(), fullName);
                list.setAdapter(adapter);

        return usersListFragmentView;
    }
}
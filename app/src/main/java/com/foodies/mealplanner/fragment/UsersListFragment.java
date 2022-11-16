package com.foodies.mealplanner.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;


public class UsersListFragment extends Fragment {

    public static final String TAG = UsersListFragment.class.getName();
    private final DatabaseHelper db = new DatabaseHelper();
    private View usersListFragmentView;
    private AdminProfileViewModel adminProfileViewModel;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        usersListFragmentView = inflater.inflate(R.layout.fragment_users_list, container, false);
        //Set sharemodel to share User data to different fragments
        adminProfileViewModel = new ViewModelProvider(requireActivity()).get(AdminProfileViewModel.class);
        List<User> users = new ArrayList<>();
        ArrayList<String> fullName = new ArrayList<>();

        db.getAllUsers(userList -> {
            users.addAll(userList);
            for (User user : userList) {

                fullName.add(user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName());
                Log.d("USERSSSSS", user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName());
            }

            ListView list = usersListFragmentView.findViewById(R.id.userListView);

//        CustomAdapter adapter = new CustomAdapter(this.getContext(), fullName);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.users_listview, R.id.userView, fullName);
            list.setAdapter(adapter);

            list.setClickable(true);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    User user = users.get(i);
                    Log.i("ON CLICK LIST", user.getEmail());
                    adminProfileViewModel.setSelectedItem(user);

                    UserUpdateProfileFragment userUpdateProfileFragment = new UserUpdateProfileFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(UsersListFragment.TAG);
                    transaction.replace(R.id.loginHomeFrame, userUpdateProfileFragment);
                    transaction.commit();
                }
            });
        });
        return usersListFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("ONVIEWCREATED", "CALLED HERE");




    }
}
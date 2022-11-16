package com.foodies.mealplanner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UsersListFragment extends Fragment {

    public static final String TAG = UsersListFragment.class.getName();
    private final DatabaseHelper db = new DatabaseHelper();
    private View usersListFragmentView;
    private AdminProfileViewModel adminProfileViewModel;
    private String[] sortArray;
    EditText searchFilter;


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

        searchFilter = usersListFragmentView.findViewById(R.id.searchFilter);
        List<User> users = new ArrayList<>();
        ArrayList<String> fullName = new ArrayList<>();
        Spinner sort = usersListFragmentView.findViewById(R.id.sortingSpinner);
        sortArray = getResources().getStringArray(R.array.sort);

        //Set adapter of spinner
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, sortArray);
        sort.setAdapter(adapter1);




        ListView list = usersListFragmentView.findViewById(R.id.userListView);
        db.getAllUsers(userList -> {
            users.addAll(userList);
            for (User user : userList) {

                fullName.add(user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName());
                Log.d("USERSSSSS", user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName());
            }

//            sort.setOnClickListener(usersListFragmentView ->{
//                Collections.sort(fullName, (o1, o2) -> o2
//                        .compareTo(o1));
//                Collections.sort(users, (o1, o2) -> o2.getUserDetails().getFirstName()
//                        .compareTo(o1.getUserDetails().getFirstName()));
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.users_listview, R.id.userView, fullName);
//                list.setAdapter(adapter);
//            });
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.users_listview, R.id.userView, fullName);
            sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i == 1){
                        Collections.sort(fullName, (o1, o2) -> o1.toLowerCase()
                                .compareTo(o2.toLowerCase()));
                        Collections.sort(users, (o1, o2) -> o1.getUserDetails().getFirstName().toLowerCase()
                                .compareTo(o2.getUserDetails().getFirstName().toLowerCase()));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.users_listview, R.id.userView, fullName);
                        list.setAdapter(adapter);
                        extracted(adapter);
                        sort.setSelection(0);
                    }else if(i == 2){
                        Collections.sort(fullName, (o1, o2) -> o2.toLowerCase()
                                .compareTo(o1.toLowerCase()));
                        Collections.sort(users, (o1, o2) -> o2.getUserDetails().getFirstName().toLowerCase()
                                .compareTo(o1.getUserDetails().getFirstName().toLowerCase()));

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.users_listview, R.id.userView, fullName);
                        list.setAdapter(adapter);
                        extracted(adapter);
                        sort.setSelection(0);
                    }

//                    adapter = new ArrayAdapter<String>(getContext(), R.layout.users_listview, R.id.userView, fullName);
//                    list.setAdapter(adapter);

                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    sort.setSelection(0);
                    return;
                }

            });



//        CustomAdapter adapter = new CustomAdapter(this.getContext(), fullName);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.users_listview, R.id.userView, fullName);
            list.setAdapter(adapter);
            extracted(adapter);


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

    /**
     * @param adapter
     */
    private void extracted(ArrayAdapter<String> adapter) {
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("ONVIEWCREATED", "CALLED HERE");


    }
}
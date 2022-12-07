package com.foodies.mealplanner.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.adapter.UserListViewAdapter;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.viewmodel.UserUpdateViewModel;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for users
 * @author herje
 * @version 1
 */
public class UsersListFragment extends Fragment {

    public static final String TAG = UsersListFragment.class.getName();
    private final UserRepository userRepository = new UserRepository();
    private View usersListFragmentView;
    private UserUpdateViewModel userUpdateViewModel;
    private String[] sortArray;
    private EditText searchFilter;
    private TextView userCount;
    private Spinner sortSpinner;
    private ListView userListView;

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
        userUpdateViewModel = new ViewModelProvider(requireActivity()).get(UserUpdateViewModel.class);
        userCount = usersListFragmentView.findViewById(R.id.userCount);

        searchFilter = usersListFragmentView.findViewById(R.id.searchFilter);
        sortSpinner = usersListFragmentView.findViewById(R.id.sortingSpinner);
        sortArray = getResources().getStringArray(R.array.sort);

        //Set adapter of spinner
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, sortArray);
        sortSpinner.setAdapter(sortAdapter);

        userListView = usersListFragmentView.findViewById(R.id.userListView);
        userRepository.getAllUsers(userList -> {

            userCount.setText("Users(" + userList.size() + ")");
            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        UserListViewAdapter adapter = new UserListViewAdapter(getContext(), userList, i);
                        userListView.setAdapter(adapter);
                        textChangeListener(adapter);
                        sortSpinner.setSelection(0);
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    sortSpinner.setSelection(0);
                    return;
                }

            });

            UserListViewAdapter adapter = new UserListViewAdapter(getContext(), userList, 0);
            userListView.setAdapter(adapter);
            textChangeListener(adapter);

            userListView.setClickable(true);
            userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    User user = userList.get(i);
                    userUpdateViewModel.setSelectedItem(user);

                    UserViewUpdateFragment userUpdateProfileFragment = new UserViewUpdateFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(UsersListFragment.TAG);
                    transaction.replace(R.id.adminProfileFrame, userUpdateProfileFragment);
                    transaction.commit();
                }
            });
        });
        return usersListFragmentView;
    }

    /**
     * Listener for any changes in the field.
     * @param adapter - custom adapter specific for user.
     */
    private void textChangeListener(UserListViewAdapter adapter) {
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
    }
}
package com.foodies.mealplanner.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.foodies.mealplanner.R;

/**
 * After signup fragment that says a message and a button for going back to login page.
 * @author herje
 * @version 1
 */
public class RegisterDoneFragment extends Fragment {

    private Button backToLoginButton;
    private View registerDoneFragmentView;
    private TextView registerTxt;

    public RegisterDoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerDoneFragmentView = inflater.inflate(R.layout.fragment_register_done, container, false);

        backToLoginButton = registerDoneFragmentView.findViewById(R.id.backToLoginBtn);
        registerTxt = registerDoneFragmentView.findViewById(R.id.txtRegisterDoneMessage);

        return registerDoneFragmentView;
    }

    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        registerTxt.setText(R.string.thankyouMessage);
        backToLoginButton.setOnClickListener((registerDoneFragmentView) ->{
            getActivity().finish();
        });
    }

}
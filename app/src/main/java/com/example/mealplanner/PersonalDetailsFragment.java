package com.example.mealplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PersonalDetailsFragment extends Fragment {


    public static String TAG = PersonalDetailsFragment.class.getName();
    private View personalDetailsView;
    private String province[];


    public PersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        personalDetailsView = inflater.inflate(R.layout.fragment_personal_details, container, false);
        Spinner spinner = personalDetailsView.findViewById(R.id.province_spinner);
        province = getResources().getStringArray(R.array.province_canada);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, province);
        spinner.setAdapter(adapter);
        return personalDetailsView;
    }
}
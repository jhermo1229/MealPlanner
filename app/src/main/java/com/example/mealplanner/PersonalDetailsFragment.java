package com.example.mealplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalDetailsFragment extends Fragment {


    public static String TAG = PersonalDetailsFragment.class.getName();
    private View personalDetailsView;
    private String province[];
    private Button nextBtn;


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
                R.layout.spinner_item, province);
        spinner.setAdapter(adapter);


        nextBtn = personalDetailsView.findViewById(R.id.nextButton);

        nextBtn.setOnClickListener((personalDetailView) -> {
            MealsDeliveryRateFragment mealsDeliveryFrag = new MealsDeliveryRateFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MealsDeliveryRateFragment.TAG);
            transaction.replace(R.id.homeFrame, mealsDeliveryFrag);

            transaction.commit();
        });

        return personalDetailsView;
    }
}
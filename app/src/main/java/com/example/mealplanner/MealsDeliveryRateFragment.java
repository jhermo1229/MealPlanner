package com.example.mealplanner;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealsDeliveryRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealsDeliveryRateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static String TAG = MealsDeliveryRateFragment.class.getName();
    private Button oneBtn, twoBtn, threeBtn;
    private View mealsDeliveryView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MealsDeliveryRateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealsDeliveryRate.
     */
    // TODO: Rename and change types and number of parameters
    public static MealsDeliveryRateFragment newInstance(String param1, String param2) {
        MealsDeliveryRateFragment fragment = new MealsDeliveryRateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mealsDeliveryView = inflater.inflate(R.layout.fragment_meals_delivery_rate, container, false);
        oneBtn = mealsDeliveryView.findViewById(R.id.oneButton);
        twoBtn = mealsDeliveryView.findViewById(R.id.twoButton);
        threeBtn = mealsDeliveryView.findViewById(R.id.threeButton);

        oneBtn.setOnClickListener((mealsDeliveryView) -> {

            oneBtn.setBackgroundColor(Color.RED);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        twoBtn.setOnClickListener((mealsDeliveryView) -> {

            twoBtn.setBackgroundColor(Color.RED);
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        threeBtn.setOnClickListener((mealsDeliveryView) -> {

            threeBtn.setBackgroundColor(Color.RED);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        return mealsDeliveryView;
    }
}
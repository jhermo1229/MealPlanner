package com.example.mealplanner;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentDetailsFragment extends Fragment {

    public static final String TAG = PaymentDetailsFragment.class.getName();
    private AppUtils app;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;


    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentDetailsFragment newInstance(String param1, String param2) {
        PaymentDetailsFragment fragment = new PaymentDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        addDataToFirestore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }


    private void addDataToFirestore() {

        // creating a collection reference
        // for our Firebase Firetore database.
        app = new AppUtils();
        CollectionReference dbCourses = db.collection("userCredentials");
        User user = new User();
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName("Jeffeeeeeeeeee");
        user.setUserDetails(userDetails);
        user.setEmail("jbhermo@yahoo.com");


        user.setPassword(app.EncodeBase64("JOHNCENA"));

        Log.d(">>>>>>>", user.getPassword());

        String data = app.DecodeBase64(user.getPassword());
        Log.d(">>>>>>>", data);
        // below method is use to add data to Firebase Firestore.
        dbCourses.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(getContext(), "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(getContext(), "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

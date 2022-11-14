package com.foodies.mealplanner.repository;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.activity.MainActivity;
import com.foodies.mealplanner.fragment.MyCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Database handler for meal planner.
 * Uses firebase -> cloud firestore
 */
public class DatabaseHelper {

    public static final String USER_CREDENTIALS = "userCredentials";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * Add customer in firebase database
     *
     * @param user
     * @param activity
     */
    public void addCustomerUser(User user, Activity activity) {

        CollectionReference dbCourses = db.collection(USER_CREDENTIALS);
        dbCourses.document(user.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.i("Database", "Successful adding user");

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final EditText text = new EditText(activity);
                builder.setTitle("Meal Planner").setMessage("Successfully Created Meal Order").setView(text);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int ii) {
                        Intent i = new Intent(activity, MainActivity.class);
                        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Call Only, if you wants to clears the activity stack else ignore it.
                        activity.startActivity(i);
                        activity.finish();
                    }
                });
                builder.create().show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Unsuccessful adding user" + e.toString());
            }
        });
    }

    /**
     * Check if email is existing in firebase account
     *
     * @param user
     */
    public boolean getEmailIfExisting(User user) {

        final boolean[] isExist = {false};

        DocumentReference docRef = db.collection("userCredentials").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    isExist[0] = document.exists();
                    Log.d("EMAIL", "exist: " + document.exists());
                }
            }

        });
        return isExist[0];
    }

    /**
     * Callback function to return query.
     * Uses Callback interface as firestore cloud is asynchronous
     * @param myCallBack
     * @param user
     */
    public void readData(MyCallBack myCallBack, User user){

        DocumentReference docRef = db.collection("userCredentials").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        myCallBack.onCallBack(user);
                        Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("DATABASE", "No such document");
                        myCallBack.onCallBack(null);
                    }
                } else {
                    Log.d("DATABASE", "get failed with ", task.getException());
                }
            }
        });

    }

}

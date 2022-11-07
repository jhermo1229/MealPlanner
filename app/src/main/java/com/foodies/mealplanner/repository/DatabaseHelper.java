package com.foodies.mealplanner.repository;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.ui.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
     * Get customer in firebase account
     *
     * @param user
     */
    public User getCustomer(User user) {

        final User[] userList = {new User()};

        DocumentReference docRef = db.collection("userCredentials").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userList[0] = documentSnapshot.toObject(User.class);
            }
        });
        return userList[0];
    }


}

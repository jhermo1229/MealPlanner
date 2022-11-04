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
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Database handler for meal planner.
 * Uses firebase -> cloud firestore
 */
public class DatabaseHelper {

    public static final String USER_CREDENTIALS = "userCredentials";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addCustomerUser(User user, Activity activity) {

        CollectionReference dbCourses = db.collection(USER_CREDENTIALS);
        dbCourses.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(@NonNull @org.jetbrains.annotations.NotNull DocumentReference documentReference) {

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
}

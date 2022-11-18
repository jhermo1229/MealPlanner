package com.foodies.mealplanner.repository;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.Interface.UserListCallBack;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.activity.MainActivity;
import com.foodies.mealplanner.Interface.UserCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * User repository for meal planner.
 * Uses firebase -> cloud firestore
 */
public class UserRepository {

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
     * Callback function to return query.
     * Uses Callback interface as firestore cloud is asynchronous
     * @param userCallBack
     * @param user
     */
    public void getUser(UserCallBack userCallBack, User user){

        DocumentReference docRef = db.collection("userCredentials").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        userCallBack.onCallBack(user);
                        Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("DATABASE", "No such document");
                        userCallBack.onCallBack(null);
                    }
                } else {
                    Log.d("DATABASE", "get failed with ", task.getException());
                }
            }
        });

    }

    public void getAllUsers(UserListCallBack userListCallBack){

        List<User> userList = new ArrayList<>();
        db.collection("userCredentials").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        userList.add(document.toObject(User.class));
                    }
                    userListCallBack.onCallBack(userList);
                }else{
                    Log.d("DATABASE", "Error retrieving all users");
                }
            }
        });
    }

    /**
     * Update user profile by batch process in firestore
     * @param user
     * @param activity
     */
    public void updateUser(User user, Activity activity){
        // Get a new write batch
        WriteBatch batch = db.batch();

        DocumentReference ref = db.collection("userCredentials").document(user.getEmail());
        batch.update(ref, "email", user.getEmail());
        Log.d("DATABASE", user.getPassword());
        batch.update(ref, "password", user.getPassword());
        batch.update(ref, "status", user.getStatus());
        batch.update(ref, "userType", user.getUserType());
        batch.update(ref, "userDetails.firstName", user.getUserDetails().getFirstName());
        batch.update(ref, "userDetails.lastName", user.getUserDetails().getLastName());
        batch.update(ref, "userDetails.phoneNumber", user.getUserDetails().getPhoneNumber());

        batch.update(ref, "userDetails.address.houseNumber", user.getUserDetails().getAddress().getHouseNumber());
        batch.update(ref, "userDetails.address.street", user.getUserDetails().getAddress().getStreet());
        batch.update(ref, "userDetails.address.city", user.getUserDetails().getAddress().getCity());
        batch.update(ref, "userDetails.address.province", user.getUserDetails().getAddress().getProvince());
        batch.update(ref, "userDetails.address.postalCode", user.getUserDetails().getAddress().getPostalCode());

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("USER DATABASE UPDATE", "Successfully updated");

                Toast toast=Toast.makeText(activity.getApplicationContext(), "Successfully updated user",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}

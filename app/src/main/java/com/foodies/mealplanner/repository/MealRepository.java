package com.foodies.mealplanner.repository;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.Interface.MealListCallBack;
import com.foodies.mealplanner.activity.LoginActivity;
import com.foodies.mealplanner.activity.MainActivity;
import com.foodies.mealplanner.Interface.MealCallBack;
import com.foodies.mealplanner.Interface.UserListCallBack;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.User;
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
public class MealRepository {

    public static final String COLLECTION_NAME = "meals";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbCollection = db.collection(COLLECTION_NAME);


    /**
     * Add customer in firebase database
     *
     * @param meal
     * @param activity
     */
    public void addMeal(Meal meal, Activity activity) {


        dbCollection.document(meal.getMealName()).set(meal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.i("MEAL DATABASE", "Successful adding meal");

                Toast toast=Toast.makeText(activity.getApplicationContext(), "Successfully created meal",Toast.LENGTH_SHORT);
                toast.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Unsuccessful adding meal" + e.toString());
            }
        });
    }


    /**
     * Callback function to return query.
     * Uses Callback interface as firestore cloud is asynchronous
     * @param mealCallBack - will callback together with the object queried
     * @param mealParam - contains meal name
     */
    public void getMeal(MealCallBack mealCallBack, String mealParam){

        DocumentReference docRef = dbCollection.document(mealParam);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Meal meal = document.toObject(Meal.class);
                        mealCallBack.onCallBack(meal);
                        Log.d("MEAL DATABASE", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("MEAL DATABASE", "No such document");
                        mealCallBack.onCallBack(null);
                    }
                } else {
                    Log.d("DATABASE", "get failed with ", task.getException());
                }
            }
        });

    }

    /**
     * Gets all the meal available
     * @param mealListCallBack - will return all the meals queried
     */
    public void getAllMeals(MealListCallBack mealListCallBack){

        List<Meal> mealList = new ArrayList<>();
        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        mealList.add(document.toObject(Meal.class));
                    }
                    Log.d("MEAL DATABASE", "Successfully retrieved all");
                    mealListCallBack.onCallBack(mealList);
                }else{
                    Log.d("MEAL DATABASE", "Error retrieving all meals");
                }
            }
        });
    }

    /**
     * Update meals by batch process in firestore
     * @param meal - meal to be updated
     * @param activity - get current activity
     */
    public void updateMeal(Meal meal, Activity activity){
        // Get a new write batch
        WriteBatch batch = db.batch();

        DocumentReference ref = dbCollection.document(meal.getMealName());
        batch.update(ref, "mealName", meal.getMealName());
        batch.update(ref, "mealDescription", meal.getMealDescription());
        batch.update(ref, "mealIngredient", meal.getMealIngredients());
        batch.update(ref, "mealPrice", meal.getMealPrice());
        batch.update(ref, "mealStatus", meal.getMealStatus());
        batch.update(ref, "mealType", meal.getMealType());

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("MEAL DATABASE UPDATE", "Successfully updated");
                Toast toast=Toast.makeText(activity.getApplicationContext(), "Successfully updated meal",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}

package com.foodies.mealplanner.repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.Interface.MealListCallBack;
import com.foodies.mealplanner.Interface.MealNameCallBack;
import com.foodies.mealplanner.model.Meal;
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
 * Uses firebase cloud firestore
 *
 * @author herje
 * @version 1
 */
public class MealRepository {

    public static final String COLLECTION_NAME = "meals";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbCollection = db.collection(COLLECTION_NAME);


    /**
     * Add customer in firebase database
     *
     * @param meal - meal object to be saved.
     * @param activity - current activity in the view.
     */
    public void addMeal(Meal meal, Activity activity) {


        dbCollection.document(meal.getMealName()).set(meal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast toast = Toast.makeText(activity.getApplicationContext(), "Successfully created meal", Toast.LENGTH_SHORT);
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
     * Get all meals depending on type
     *
     * @param mealListCallBack - meal callback for the list. Waits for all the list to load and then back to view.
     * @param mealType - parameter on what type of meal (meat, vegetable, or both)
     */
    public void getAllMealType(MealListCallBack mealListCallBack, String mealType) {

        List<Meal> mealList = new ArrayList<>();

        dbCollection.whereEqualTo("mealType", mealType)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mealList.add(document.toObject(Meal.class));
                            }
                            Log.d("MEAL DATABASE", "Successfully retrieved all");
                            mealListCallBack.onCallBack(mealList);
                        } else {
                            Log.d("MEAL DATABASE", "Error retrieving all meals");
                        }
                    }
                });
    }

    /**
     * Gets all the meal available
     *
     * @param mealListCallBack - will return all the meals queried
     */
    public void getAllMeals(MealListCallBack mealListCallBack) {

        List<Meal> mealList = new ArrayList<>();
        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        mealList.add(document.toObject(Meal.class));
                    }
                    Log.d("MEAL DATABASE", "Successfully retrieved all");
                    mealListCallBack.onCallBack(mealList);
                } else {
                    Log.d("MEAL DATABASE", "Error retrieving all meals");
                }
            }
        });
    }

    /**
     * Update meals by batch process in firestore
     *
     * @param meal     - meal to be updated
     * @param activity - get current activity
     */
    public void updateMeal(Meal meal, Activity activity) {
        // Get a new write batch
        WriteBatch batch = db.batch();

        DocumentReference ref = dbCollection.document(meal.getMealName());
        batch.update(ref, "mealName", meal.getMealName());
        batch.update(ref, "mealDescription", meal.getMealDescription());
        batch.update(ref, "mealIngredient", meal.getMealIngredients());
        batch.update(ref, "mealPrice", meal.getMealPrice());
        batch.update(ref, "mealStatus", meal.getMealStatus());
        batch.update(ref, "mealType", meal.getMealType());
        batch.update(ref, "imageUrl", meal.getImageUrl());

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("MEAL DATABASE UPDATE", "Successfully updated");
                Toast toast = Toast.makeText(activity.getApplicationContext(), "Successfully updated meal", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}

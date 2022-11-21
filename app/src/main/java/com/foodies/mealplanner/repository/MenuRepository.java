package com.foodies.mealplanner.repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.model.Menu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuRepository {

    public static final String COLLECTION_NAME = "menus";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbCollection = db.collection(COLLECTION_NAME);


    /**
     * Add customer in firebase database
     *
     * @param menu
     * @param activity
     */
    public void addMenu(Menu menu, Activity activity) {


        dbCollection.document(menu.getMenuName()).set(menu).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.i("MENU DATABASE", "Successful adding menu");

                Toast toast=Toast.makeText(activity.getApplicationContext(), "Successfully created menu",Toast.LENGTH_SHORT);
                toast.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Unsuccessful adding menu" + e.toString());
            }
        });
    }
}

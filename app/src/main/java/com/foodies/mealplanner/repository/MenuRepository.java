package com.foodies.mealplanner.repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.Interface.MealListCallBack;
import com.foodies.mealplanner.Interface.MenuListCallBack;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.Menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class MenuRepository {

    public static final String COLLECTION_NAME = "menus";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbCollection = db.collection(COLLECTION_NAME);


    /**
     * Add menu in firebase database
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


    /**
     * Gets all the menu available
     * @param menuListCallBack - will return all the meals queried
     */
    public void getAllMenus(MenuListCallBack menuListCallBack){

        List<Menu> menuList = new ArrayList<>();
        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        menuList.add(document.toObject(Menu.class));
                    }
                    Log.d("MENU DATABASE", "Successfully retrieved all");
                    menuListCallBack.onCallBack(menuList);
                }else{
                    Log.d("MENU DATABASE", "Error retrieving all menu");
                }
            }
        });
    }

    /**
     * Update menu by batch process in firestore
     * @param menu - meal to be updated
     * @param activity - get current activity
     */
    public void updateMenu(Menu menu, Activity activity){
        // Get a new write batch
        WriteBatch batch = db.batch();

        DocumentReference ref = dbCollection.document(menu.getMenuName());
        batch.update(ref, "menuName", menu.getMenuName());
        batch.update(ref, "meatMeal", menu.getMeatMeal());
        batch.update(ref, "vegetableMeal", menu.getVegetableMeal());
        batch.update(ref, "bothMeal", menu.getBothMeal());

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("MENU DATABASE UPDATE", "Successfully updated");
                Toast toast=Toast.makeText(activity.getApplicationContext(), "Successfully updated menu",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}

package com.foodies.mealplanner.repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foodies.mealplanner.Interface.EmailCallBack;
import com.foodies.mealplanner.model.Email;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

/**
 * This is the repository for email transactions
 *
 * @author herje
 * @version 1
 */
public class EmailRepository {

    public static final String COLLECTION_NAME = "emails";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbCollection = db.collection(COLLECTION_NAME);

    /**
     * Save composed email in firebase database to be sent on a later date.
     *
     * @param email    - model containing the email parts
     * @param activity - current activity of the application
     */
    public void saveEmail(Email email, Activity activity) {


        dbCollection.document(email.getDeliveryDate()).set(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.i("EMAIL DATABASE", "Successful saved email");

                Toast toast = Toast.makeText(activity.getApplicationContext(), "Successfully saved email", Toast.LENGTH_SHORT);
                toast.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Unsuccessfully saved email" + e.toString());
            }
        });
    }

    /**
     * Callback function to return query.
     * Uses Callback interface as firestore cloud is asynchronous
     *
     * @param emailCallBack - callback to be used for sending the email back to the view
     * @param emailDate - document name to be used.
     */
    public void getEmail(EmailCallBack emailCallBack, String emailDate) {

        DocumentReference docRef = db.collection("emails").document(emailDate);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Email email = document.toObject(Email.class);

                        if (!email.getSent()) {
                            emailCallBack.onCallBack(email);
                        } else {
                            emailCallBack.onCallBack(null);
                        }
                        Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("DATABASE", "No such document");
                        emailCallBack.onCallBack(null);
                    }
                } else {
                    Log.d("DATABASE", "get failed with ", task.getException());
                }
            }
        });

    }

    /**
     * Update user profile by batch process in firestore
     *
     * @param deliveryDate - this will be used for the document name
     * @param isSent - sets if the email is already sent or not.
     */
    public void updateEmail(String deliveryDate, Boolean isSent) {
        // Get a new write batch
        WriteBatch batch = db.batch();

        DocumentReference ref = db.collection("emails").document(deliveryDate);
        batch.update(ref, "sent", isSent);
        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("EMAIL DATABASE UPDATE", "Successfully updated");

            }
        });
    }

}

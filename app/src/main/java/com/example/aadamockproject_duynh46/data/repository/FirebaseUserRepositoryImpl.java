package com.example.aadamockproject_duynh46.data.repository;

import androidx.annotation.NonNull;

import com.example.aadamockproject_duynh46.domain.model.UserModel;
import com.example.aadamockproject_duynh46.domain.repository.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

public class FirebaseUserRepositoryImpl implements UserRepository {
    private final DatabaseReference userRef;

    @Inject
    public FirebaseUserRepositoryImpl(){
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase
                .getInstance("https://mymockproject-f68d9-default-rtdb.asia-southeast1.firebasedatabase.app/");
        userRef = mFirebaseDatabase.getReference("user").child("current_user");
    }
    @Override
    public void getCurrentUser(UserCallback callback) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                if (user != null) {
                    callback.onUserLoaded(user);
                } else {
                    callback.onError("User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    @Override
    public void saveCurrentUser(UserModel user, SaveCallback callback) {
        userRef.setValue(user)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}

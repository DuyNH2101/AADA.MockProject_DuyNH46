package com.example.aadamockproject_duynh46.domain.repository;

import com.example.aadamockproject_duynh46.domain.model.UserModel;

public interface UserRepository {
    void getCurrentUser(UserCallback callback);
    void saveCurrentUser(UserModel user, SaveCallback callback);

    interface UserCallback {
        void onUserLoaded(UserModel user);
        void onError(String error);
    }

    interface SaveCallback {
        void onSuccess();
        void onError(String error);
    }
}

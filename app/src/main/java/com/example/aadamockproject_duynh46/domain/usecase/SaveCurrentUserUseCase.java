package com.example.aadamockproject_duynh46.domain.usecase;

import com.example.aadamockproject_duynh46.domain.model.UserModel;
import com.example.aadamockproject_duynh46.domain.repository.UserRepository;

import javax.inject.Inject;

public class SaveCurrentUserUseCase {
    private final UserRepository userRepository;

    @Inject
    public SaveCurrentUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UserModel user, UserRepository.SaveCallback callback) {
        userRepository.saveCurrentUser(user, callback);
    }
}

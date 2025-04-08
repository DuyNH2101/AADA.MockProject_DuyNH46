package com.example.aadamockproject_duynh46.domain.usecase;

import com.example.aadamockproject_duynh46.domain.repository.UserRepository;

import javax.inject.Inject;

public class GetCurrentUserUseCase {
    private final UserRepository userRepository;

    @Inject
    public GetCurrentUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UserRepository.UserCallback callback) {
        userRepository.getCurrentUser(callback);
    }
}

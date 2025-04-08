package com.example.aadamockproject_duynh46.presentation.profile;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aadamockproject_duynh46.domain.model.UserModel;
import com.example.aadamockproject_duynh46.domain.repository.UserRepository;
import com.example.aadamockproject_duynh46.domain.usecase.GetCurrentUserUseCase;
import com.example.aadamockproject_duynh46.domain.usecase.SaveCurrentUserUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<UserModel> mutableLiveDataUserModel = new MutableLiveData<>();
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final SaveCurrentUserUseCase saveCurrentUserUseCase;

    public MutableLiveData<UserModel> getMutableLiveDataUserModel() {
        return mutableLiveDataUserModel;
    }

    @Inject
    public ProfileViewModel(GetCurrentUserUseCase getCurrentUserUseCase,
                            SaveCurrentUserUseCase saveCurrentUserUseCase){
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.saveCurrentUserUseCase = saveCurrentUserUseCase;
    }
    public void saveUser(UserModel userModel){
        saveCurrentUserUseCase.execute(userModel, new UserRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                Log.d("Profile View Model", "Save user success");
            }

            @Override
            public void onError(String error) {
                Log.d("Profile View Model", "Save user failed");
            }
        });
    }

    public void getUser(){
        getCurrentUserUseCase.execute(new UserRepository.UserCallback() {
            @Override
            public void onUserLoaded(UserModel user) {
                if(user == null){
                    user = new UserModel("", "Duy Hoang", "hoangduy@gmail.com", "21/01/2004", "Male");
                    saveUser(user);
                }
                mutableLiveDataUserModel.setValue(user);
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }
}

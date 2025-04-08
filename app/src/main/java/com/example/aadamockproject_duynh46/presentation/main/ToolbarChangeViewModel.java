package com.example.aadamockproject_duynh46.presentation.main;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ToolbarChangeViewModel extends ViewModel {
    private final MutableLiveData<String> mutableLiveDataCurrentFragment = new MutableLiveData<>();

    @Inject
    public ToolbarChangeViewModel(){}
    public MutableLiveData<String> getMutableLiveDataCurrentFragment() {
        return mutableLiveDataCurrentFragment;
    }

    public void updateCurrentFragment(String newCurrentFragment){
        mutableLiveDataCurrentFragment.setValue(newCurrentFragment);
    }
}

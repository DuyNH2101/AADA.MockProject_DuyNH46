package com.example.aadamockproject_duynh46.presentation.upcoming;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aadamockproject_duynh46.presentation.Model.Moviedat;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UpcomingMovieDetailsViewModel extends ViewModel {
    private MutableLiveData<Moviedat> upcomingMovieDetailsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> goToTicketMutableLiveData = new MutableLiveData<>();

    @Inject
    public UpcomingMovieDetailsViewModel(){}

    public MutableLiveData<Moviedat> getUpcomingMovieDetailsMutableLiveData() {
        return upcomingMovieDetailsMutableLiveData;
    }

    public MutableLiveData<String> getGoToTicketMutableLiveData() {
        return goToTicketMutableLiveData;
    }
}

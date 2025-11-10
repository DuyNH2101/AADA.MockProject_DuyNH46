package com.example.aadamockproject_duynh46.presentation.ticket;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aadamockproject_duynh46.presentation.Model.Ticket;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TicketDetailsViewModel extends ViewModel {
    @Inject
    public TicketDetailsViewModel(){
    }
    private MutableLiveData<Ticket> ticketDetailsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Ticket> getTicketDetailsMutableLiveData() {
        return ticketDetailsMutableLiveData;
    }
}

package com.example.aadamockproject_duynh46.presentation.reminderlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.aadamockproject_duynh46.domain.model.ReminderModel;
import com.example.aadamockproject_duynh46.domain.usecase.ReminderUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReminderListViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ReminderModel>> mutableLiveDataAllReminderList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ReminderModel>> mutableLiveDataFirstTwoReminderList = new MutableLiveData<>();

    private final MutableLiveData<ReminderModel> mutableLiveDataChangedReminder = new MutableLiveData<>();
    private final ReminderUseCase reminderUseCase;
    private final Observer<List<ReminderModel>> reminderObserver = reminders -> {
        ArrayList<ReminderModel> reminderList = new ArrayList<>(reminders);
        mutableLiveDataAllReminderList.setValue(reminderList);

        ArrayList<ReminderModel> firstTwo = new ArrayList<>();
        boolean checkIfRemoved = true;
        for (int i = 0; i < reminderList.size(); i++) {
            if(i < 2){
                firstTwo.add(reminderList.get(i));
            }
            if(mutableLiveDataChangedReminder.getValue() != null
                    && reminderList.get(i).getMovie().getId() == mutableLiveDataChangedReminder.getValue().getMovie().getId()){
                checkIfRemoved = false;
            }
            if(i >= 2 && !checkIfRemoved){
                break;
            }
        }
        if(checkIfRemoved){
            mutableLiveDataChangedReminder.setValue(null);
        }
        mutableLiveDataFirstTwoReminderList.setValue(firstTwo);
    };

    public MutableLiveData<ArrayList<ReminderModel>> getMutableLiveDataAllReminderList() {
        return mutableLiveDataAllReminderList;
    }

    public MutableLiveData<ArrayList<ReminderModel>> getMutableLiveDataFirstTwoReminderList() {
        return mutableLiveDataFirstTwoReminderList;
    }

    public MutableLiveData<ReminderModel> getMutableLiveDataChangedReminder() {
        return mutableLiveDataChangedReminder;
    }

    @Inject
    public ReminderListViewModel(ReminderUseCase reminderUseCase){
        this.reminderUseCase = reminderUseCase;
        reminderUseCase.getAllReminderLiveData().observeForever(reminderObserver);
    }


    public void addReminder(ReminderModel reminderModel){
        if(reminderUseCase.containReminderWithMovieId(reminderModel.getMovie().getId())){
            reminderUseCase.deleteByMovieId(reminderModel.getMovie().getId());
        }
        reminderUseCase.insertReminder(reminderModel);
        ReminderModel savedReminder = reminderUseCase.getReminderByMovieId(reminderModel.getMovie().getId());
        mutableLiveDataChangedReminder.setValue(savedReminder);

    }

    public void removeReminder(ReminderModel reminderModel){
        reminderUseCase.deleteReminder(reminderModel);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.reminderUseCase.getAllReminderLiveData().removeObserver(reminderObserver);
    }
}

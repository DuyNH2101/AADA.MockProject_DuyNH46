package com.example.aadamockproject_duynh46.presentation.reminderlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aadamockproject_duynh46.domain.model.ReminderModel;
import com.example.aadamockproject_duynh46.domain.usecase.ReminderUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReminderListViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ReminderModel>> mutableLiveDataAllReminderList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ReminderModel>> mutableLiveDataFirstTwoReminderList = new MutableLiveData<>();

    private final MutableLiveData<ReminderModel> mutableLiveDataChangedReminder = new MutableLiveData<>();
    private final ReminderUseCase reminderUseCase;

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
    }

    public void loadAllReminder(){
        mutableLiveDataAllReminderList.setValue(reminderUseCase.getAllReminder());
        ArrayList<ReminderModel> tempFirstTwoList = new ArrayList<>();
        for(int i = 0; i < mutableLiveDataAllReminderList.getValue().size() && i < 2; i++){
            tempFirstTwoList.add(mutableLiveDataAllReminderList.getValue().get(i));
        }

        mutableLiveDataFirstTwoReminderList.setValue(tempFirstTwoList);
    }

    public void addReminder(ReminderModel reminderModel){
        ArrayList<ReminderModel> tempAllList = (mutableLiveDataAllReminderList.getValue() == null) ? (new ArrayList<>()) : (mutableLiveDataAllReminderList.getValue());
        ArrayList<ReminderModel> tempFirstTwoList = (mutableLiveDataFirstTwoReminderList.getValue() == null) ? (new ArrayList<>()) : (mutableLiveDataFirstTwoReminderList.getValue());

        reminderUseCase.insertReminder(reminderModel);
        ReminderModel savedReminder = reminderUseCase.getReminderByMovieId(reminderModel.getMovie().getId());
        tempAllList.removeIf(reminderModel1 -> reminderModel1.getMovie().getId() == savedReminder.getMovie().getId());
        tempFirstTwoList.removeIf(reminderModel1 -> reminderModel1.getMovie().getId() == savedReminder.getMovie().getId());

        tempAllList.add(savedReminder);
        tempFirstTwoList.add(savedReminder);
        if(tempFirstTwoList.size() > 2){
            tempFirstTwoList.remove(2);
        }

        mutableLiveDataAllReminderList.setValue(new ArrayList<>(tempAllList));
        mutableLiveDataFirstTwoReminderList.setValue(new ArrayList<>(tempFirstTwoList));
        mutableLiveDataChangedReminder.setValue(savedReminder);

    }

    public void removeReminder(ReminderModel reminderModel){
        ArrayList<ReminderModel> tempAllList = (mutableLiveDataAllReminderList.getValue() == null) ? (new ArrayList<>()) : (mutableLiveDataAllReminderList.getValue());
        ArrayList<ReminderModel> tempFirstTwoList = (mutableLiveDataFirstTwoReminderList.getValue() == null) ? (new ArrayList<>()) : (mutableLiveDataFirstTwoReminderList.getValue());

        reminderUseCase.deleteReminder(reminderModel);

        tempAllList.remove(reminderModel);
        for(ReminderModel r : tempFirstTwoList){
            if(r.getId() == reminderModel.getId()){
                tempFirstTwoList.remove(r);
            }
        }
        if(tempAllList.size() < 2){
            mutableLiveDataAllReminderList.setValue(new ArrayList<>(tempAllList));
            mutableLiveDataFirstTwoReminderList.setValue(new ArrayList<>(tempFirstTwoList));

            return;
        }
        if(tempFirstTwoList.size() < 2){
            for(ReminderModel r : tempAllList){
                if(r.getId() != reminderModel.getId()){
                    tempFirstTwoList.add(r);
                    break;
                }
            }
        }
        mutableLiveDataAllReminderList.setValue(new ArrayList<>(tempAllList));
        mutableLiveDataFirstTwoReminderList.setValue(new ArrayList<>(tempFirstTwoList));
        mutableLiveDataChangedReminder.setValue(reminderModel);
    }
}

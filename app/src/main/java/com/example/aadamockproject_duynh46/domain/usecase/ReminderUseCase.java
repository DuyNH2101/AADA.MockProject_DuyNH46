package com.example.aadamockproject_duynh46.domain.usecase;

import static com.example.aadamockproject_duynh46.data.mapper.EntityMapper.toReminderEntity;
import static com.example.aadamockproject_duynh46.data.mapper.EntityMapper.toReminderModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;
import com.example.aadamockproject_duynh46.data.source.local.MovieDatabase;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;
import com.example.aadamockproject_duynh46.domain.repository.ReminderRepository;
import com.example.aadamockproject_duynh46.domain.usecase.base.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ReminderUseCase extends UseCase {
    private ReminderRepository reminderRepository;

    @Inject
    public ReminderUseCase(ReminderRepository reminderRepository){
        this.reminderRepository = reminderRepository;
    }

    public void insertReminder(ReminderModel reminderModel){
        reminderRepository.insert(reminderModel);
    }

    public void deleteReminder(ReminderModel reminderModel){
        reminderRepository.delete(reminderModel);
    }

    public void updateReminder(ReminderModel reminderModel){
        reminderRepository.update(reminderModel);
    }

    public void deleteByMovieId(ReminderModel reminderModel){
        reminderRepository.deleteByMovieId(reminderModel.getMovie().getId());
    }

    public void deleteByMovieId(int movieId){
        reminderRepository.deleteByMovieId(movieId);
    }

    public boolean containReminderWithMovieId(int movieId) {
        return reminderRepository.containReminderWithMovieId(movieId);
    }

    public ReminderModel getReminderByMovieId(int movieId){
        return toReminderModel(reminderRepository.getReminderByMovieId(movieId));
    }
    public ArrayList<ReminderModel> getAllReminder(){
        ArrayList<ReminderModel> reminderModels = new ArrayList<>();
        for(ReminderEntity r : reminderRepository.getAllReminders()){
            reminderModels.add(toReminderModel(r));
        }
        return reminderModels;
    }

    public LiveData<List<ReminderModel>> getAllReminderLiveData() {
        return Transformations.map(
                reminderRepository.getAllRemindersLiveData(),
                entities -> {
                    List<ReminderModel> models = new ArrayList<>();
                    if(entities == null){
                        return models;
                    }
                    for (ReminderEntity entity : entities) {
                        models.add(toReminderModel(entity));
                    }
                    return models;
                }
        );
    }

}

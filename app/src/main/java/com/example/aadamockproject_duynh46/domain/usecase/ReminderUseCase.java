package com.example.aadamockproject_duynh46.domain.usecase;

import static com.example.aadamockproject_duynh46.data.mapper.EntityMapper.toReminderEntity;
import static com.example.aadamockproject_duynh46.data.mapper.EntityMapper.toReminderModel;

import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;
import com.example.aadamockproject_duynh46.data.source.local.MovieDatabase;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;
import com.example.aadamockproject_duynh46.domain.usecase.base.UseCase;

import java.util.ArrayList;

import javax.inject.Inject;

public class ReminderUseCase extends UseCase {
    private MovieDatabase movieDatabase;

    @Inject
    public ReminderUseCase(MovieDatabase movieDatabase){
        this.movieDatabase = movieDatabase;
    }

    public void insertReminder(ReminderModel reminderModel){
        movieDatabase.reminderDao().insert(toReminderEntity(reminderModel));
    }

    public void deleteReminder(ReminderModel reminderModel){
        movieDatabase.reminderDao().delete(toReminderEntity(reminderModel));
    }

    public void updateReminder(ReminderModel reminderModel){
        movieDatabase.reminderDao().update(toReminderEntity(reminderModel));
    }

    public void deleteByMovieId(ReminderModel reminderModel){
        movieDatabase.reminderDao().deleteByMovieId(reminderModel.getMovie().getId());
    }

    public void deleteByMovieId(int movieId){
        movieDatabase.reminderDao().deleteByMovieId(movieId);
    }

    public boolean containReminderWithMovieId(int movieId) {
        return movieDatabase.reminderDao().containReminderWithMovieId(movieId);
    }

    public ReminderModel getReminderByMovieId(int movieId){
        return toReminderModel(movieDatabase.reminderDao().getReminderByMovieId(movieId));
    }
    public ArrayList<ReminderModel> getAllReminder(){
        ArrayList<ReminderModel> reminderModels = new ArrayList<>();
        for(ReminderEntity r : movieDatabase.reminderDao().getAllReminders()){
            reminderModels.add(toReminderModel(r));
        }
        return reminderModels;
    }
}

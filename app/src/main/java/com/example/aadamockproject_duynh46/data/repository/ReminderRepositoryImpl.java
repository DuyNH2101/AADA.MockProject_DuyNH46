package com.example.aadamockproject_duynh46.data.repository;

import static com.example.aadamockproject_duynh46.data.mapper.EntityMapper.toReminderEntity;

import androidx.lifecycle.LiveData;

import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;
import com.example.aadamockproject_duynh46.data.source.local.MovieDatabase;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;
import com.example.aadamockproject_duynh46.domain.repository.ReminderRepository;

import java.util.List;

import javax.inject.Inject;

public class ReminderRepositoryImpl implements ReminderRepository {
    private final MovieDatabase movieDatabase;

    @Inject
    public ReminderRepositoryImpl(MovieDatabase movieDatabase){
        this.movieDatabase = movieDatabase;
    }

    @Override
    public List<ReminderEntity> getAllReminders(){
        return movieDatabase.reminderDao().getAllReminders();
    }

    @Override
    public LiveData<List<ReminderEntity>> getAllRemindersLiveData() {
        return movieDatabase.reminderDao().getAllRemindersLiveData();
    }

    @Override
    public void insert(ReminderModel reminderModel){
        movieDatabase.reminderDao().insert(toReminderEntity(reminderModel));
    }
    @Override
    public void delete(ReminderModel reminderModel){
        movieDatabase.reminderDao().delete(toReminderEntity(reminderModel));
    }
    @Override
    public void update(ReminderModel reminderModel){
        movieDatabase.reminderDao().update(toReminderEntity(reminderModel));
    }
    @Override
    public boolean containReminderWithMovieId(int movieId){
        return movieDatabase.reminderDao().containReminderWithMovieId(movieId);
    }

    @Override
    public void deleteByMovieId(int movieId){
        movieDatabase.reminderDao().deleteByMovieId(movieId);
    }

    @Override
    public ReminderEntity getReminderByMovieId(int movieId){
        return movieDatabase.reminderDao().getReminderByMovieId(movieId);
    }
}

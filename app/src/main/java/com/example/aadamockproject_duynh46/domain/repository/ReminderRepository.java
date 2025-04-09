package com.example.aadamockproject_duynh46.domain.repository;

import static com.example.aadamockproject_duynh46.data.mapper.EntityMapper.toReminderEntity;

import androidx.lifecycle.LiveData;

import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;

import java.util.List;

public interface ReminderRepository {
    List<ReminderEntity> getAllReminders();
    LiveData<List<ReminderEntity>> getAllRemindersLiveData();
    void insert(ReminderModel reminderModel);
    void delete(ReminderModel reminderModel);

    void update(ReminderModel reminderModel);
    boolean containReminderWithMovieId(int movieId);

    void deleteByMovieId(int movieId);

    ReminderEntity getReminderByMovieId(int movieId);
}

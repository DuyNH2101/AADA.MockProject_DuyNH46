package com.example.aadamockproject_duynh46.data.source.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;

import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminders")
    List<ReminderEntity> getAllReminders();

    @Query("SELECT * FROM reminders")
    LiveData<List<ReminderEntity>> getAllRemindersLiveData();
    @Insert
    void insert (ReminderEntity reminderEntity);

    @Delete
    void delete(ReminderEntity reminderEntity);

    @Update
    void update(ReminderEntity reminderEntity);

    @Query("DELETE FROM reminders WHERE movieId = :movieId")
    void deleteByMovieId(int movieId);

    @Query("SELECT EXISTS (SELECT 1 FROM reminders WHERE movieId = :movieId)")
    boolean containReminderWithMovieId(int movieId);

    @Query("SELECT * FROM reminders WHERE movieId = :movieId LIMIT 1")
    ReminderEntity getReminderByMovieId(int movieId);
}

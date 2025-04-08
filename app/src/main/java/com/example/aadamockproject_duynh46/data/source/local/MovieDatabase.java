package com.example.aadamockproject_duynh46.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;
import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;
import com.example.aadamockproject_duynh46.data.source.local.dao.FavoriteMovieDao;
import com.example.aadamockproject_duynh46.data.source.local.dao.ReminderDao;


@Database(entities = {MovieEntity.class, ReminderEntity.class}, version = 3)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract FavoriteMovieDao favoriteMovieDao();
    public abstract ReminderDao reminderDao();
}

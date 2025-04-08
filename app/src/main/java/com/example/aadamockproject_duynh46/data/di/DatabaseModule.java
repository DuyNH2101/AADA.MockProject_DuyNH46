package com.example.aadamockproject_duynh46.data.di;

import android.content.Context;

import androidx.room.Room;


import com.example.aadamockproject_duynh46.data.source.local.MovieDatabase;
import com.example.aadamockproject_duynh46.data.source.local.dao.FavoriteMovieDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public MovieDatabase provideMovieDatabase(@ApplicationContext Context context){
        return Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, "movies_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    public FavoriteMovieDao provideFavoriteMovieDao(MovieDatabase favoriteMovieDatabase){
        return favoriteMovieDatabase.favoriteMovieDao();
    }
}

package com.example.aadamockproject_duynh46.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;

import java.util.List;

@Dao
public interface FavoriteMovieDao {
    @Insert
    void insert(MovieEntity movie);
    @Delete
    void delete(MovieEntity movie);
    @Query("SELECT * FROM favorite_movies")
    List<MovieEntity> loadAllFavoriteMovies();

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_movies where id = :id)")
    boolean existFavoriteMovieWithId(int id);
}

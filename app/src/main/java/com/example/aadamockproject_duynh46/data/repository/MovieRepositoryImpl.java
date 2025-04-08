package com.example.aadamockproject_duynh46.data.repository;

import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;
import com.example.aadamockproject_duynh46.data.source.local.MovieDatabase;
import com.example.aadamockproject_duynh46.domain.repository.MovieRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MovieRepositoryImpl implements MovieRepository {
    private final MovieDatabase movieDatabase;

    @Inject
    public MovieRepositoryImpl(MovieDatabase movieDatabase){
        this.movieDatabase = movieDatabase;
    }


    @Override
    public List<MovieEntity> getAllFavoriteMovie() {
        return movieDatabase.favoriteMovieDao().loadAllFavoriteMovies();
    }

    @Override
    public void insert(MovieEntity movieEntity) {
        movieDatabase.favoriteMovieDao().insert(movieEntity);
    }

    @Override
    public void delete(MovieEntity movieEntity) {
        movieDatabase.favoriteMovieDao().delete(movieEntity);
    }

    @Override
    public boolean existFavoriteMovieWithId(int id){
        return movieDatabase.favoriteMovieDao().existFavoriteMovieWithId(id);
    }

}

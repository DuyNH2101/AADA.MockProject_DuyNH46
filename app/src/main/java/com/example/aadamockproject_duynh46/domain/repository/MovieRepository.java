package com.example.aadamockproject_duynh46.domain.repository;

import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;

import java.util.List;

public interface MovieRepository {
    List<MovieEntity> getAllFavoriteMovie();
    void insert(MovieEntity movieEntity);
    void delete(MovieEntity movieEntity);
    boolean existFavoriteMovieWithId(int id);
}

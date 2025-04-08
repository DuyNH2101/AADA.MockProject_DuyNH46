package com.example.aadamockproject_duynh46.domain.usecase;

import com.example.aadamockproject_duynh46.data.mapper.EntityMapper;
import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.repository.MovieRepository;
import com.example.aadamockproject_duynh46.domain.usecase.base.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LocalMovieListUseCase extends UseCase {
    private MovieRepository movieRepository;

    @Inject
    public LocalMovieListUseCase(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public ArrayList<MovieModel> loadFavoriteMovieList(){
        List<MovieEntity> favoriteEntityList = movieRepository.getAllFavoriteMovie();

        ArrayList<MovieModel> favoriteModeList = new ArrayList<>();

        for(MovieEntity m : favoriteEntityList){
            favoriteModeList.add(EntityMapper.toMovieModel(m));
        }

        return favoriteModeList;
    }

    public void insertFavoriteMovie(MovieModel movieModel){
        movieRepository.insert(EntityMapper.toMovieEntity(movieModel));
    }

    public void deleteFavoriteMovie(MovieModel movieModel){
        movieRepository.delete(EntityMapper.toMovieEntity(movieModel));
    }
}

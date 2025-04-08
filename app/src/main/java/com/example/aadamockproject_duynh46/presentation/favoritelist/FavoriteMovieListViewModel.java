package com.example.aadamockproject_duynh46.presentation.favoritelist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.usecase.LocalMovieListUseCase;
import com.example.aadamockproject_duynh46.domain.usecase.ReminderUseCase;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavoriteMovieListViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<MovieModel>> mutableLiveDataFavoriteMovies = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<MovieModel>> mutableLiveDataFilteredFavoriteMovies = new MutableLiveData<>();
    private final LocalMovieListUseCase localMovieListUseCase;

    private final MutableLiveData<MovieModel> mutableLiveDataChangedFavMovie = new MutableLiveData<>();

    public MutableLiveData<ArrayList<MovieModel>> getMutableLiveDataFavoriteMovies() {
        return mutableLiveDataFavoriteMovies;
    }

    public MutableLiveData<ArrayList<MovieModel>> getMutableLiveDataFilteredFavoriteMovies() {
        return mutableLiveDataFilteredFavoriteMovies;
    }

    public MutableLiveData<MovieModel> getMutableLiveDataChangedFavMovie() {
        return mutableLiveDataChangedFavMovie;
    }


    @Inject
    public FavoriteMovieListViewModel(LocalMovieListUseCase localMovieListUseCase){
        this.localMovieListUseCase = localMovieListUseCase;
        loadFavoriteMovieList();
    }
    public void loadFavoriteMovieList(){
        mutableLiveDataFavoriteMovies.setValue(localMovieListUseCase.loadFavoriteMovieList());
    }
    public void addFavoriteMovie(MovieModel movieModel){
        ArrayList<MovieModel> currentList = mutableLiveDataFavoriteMovies.getValue();
        ArrayList<MovieModel> updatedList = currentList != null ? new ArrayList<>(currentList) : new ArrayList<>();
        updatedList.add(0, movieModel);
        mutableLiveDataFavoriteMovies.setValue(updatedList);

        mutableLiveDataChangedFavMovie.setValue(movieModel);
        localMovieListUseCase.insertFavoriteMovie(movieModel);
    }
    public void removeFavoriteMovie(MovieModel movieModel){
        ArrayList<MovieModel> currentList = mutableLiveDataFavoriteMovies.getValue();
        ArrayList<MovieModel> updatedList = currentList != null ? new ArrayList<>(currentList) : new ArrayList<>();
        for(MovieModel m : updatedList){
            if(m.getId() == movieModel.getId()){
                mutableLiveDataChangedFavMovie.setValue(m);
                updatedList.remove(m);
                break;
            }
        }
        mutableLiveDataFavoriteMovies.setValue(updatedList);
        localMovieListUseCase.deleteFavoriteMovie(movieModel);
    }

    public void filter(String constraint){
        if(constraint.isEmpty()){
            mutableLiveDataFilteredFavoriteMovies.setValue(null);
            return;
        }
        ArrayList<MovieModel> filteredList = new ArrayList<>();

        for(MovieModel m : Objects.requireNonNull(mutableLiveDataFavoriteMovies.getValue())){
            if(m.getTitle().contains(constraint)){
                filteredList.add(m);
            }
        }

        mutableLiveDataFilteredFavoriteMovies.setValue(filteredList);
    }
}

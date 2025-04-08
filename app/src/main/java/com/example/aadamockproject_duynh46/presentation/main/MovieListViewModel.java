package com.example.aadamockproject_duynh46.presentation.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.usecase.LoadRemoteMovieListUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Flowable;
@HiltViewModel
public class MovieListViewModel extends ViewModel {

    //Movie Lists
    private final MutableLiveData<PagingData<MovieModel>> mutableLiveDataMovies = new MutableLiveData<>();


    //Load Conditions and Type
    private final MutableLiveData<String> movieLoadType = new MutableLiveData<>();
    private final MutableLiveData<String> sortBy = new MutableLiveData<>();
    private final MutableLiveData<Float> fromRating = new MutableLiveData<>();
    private final MutableLiveData<Integer> fromYear = new MutableLiveData<>();

    //Use case
    private final LoadRemoteMovieListUseCase loadRemoteMovieListUseCase;

    public MutableLiveData<PagingData<MovieModel>> getMutableLiveDataMovies() {
        return mutableLiveDataMovies;

    }
    public MutableLiveData<String> getMovieLoadType() {
        return movieLoadType;
    }

    public MutableLiveData<String> getSortBy() {
        return sortBy;
    }

    public MutableLiveData<Integer> getFromYear() {
        return fromYear;
    }

    public MutableLiveData<Float> getFromRating() {
        return fromRating;
    }

    @Inject
    public MovieListViewModel(LoadRemoteMovieListUseCase loadRemoteMovieListUseCase){
        this.loadRemoteMovieListUseCase = loadRemoteMovieListUseCase;
        Log.d("View Model", "movieLoadType.getValue(): " + movieLoadType.getValue());
        loadRemoteMovieList();
    }

    public void loadRemoteMovieList(){
        if(!checkConditions()){
            Log.d("View Model", "Condition not met");
            return;
        }
        loadRemoteMovieListUseCase.setMovieLoadType(movieLoadType.getValue());
        loadRemoteMovieListUseCase.setFromRating(fromRating.getValue());
        loadRemoteMovieListUseCase.setFromYear(fromYear.getValue());
        loadRemoteMovieListUseCase.setSortBy(sortBy.getValue());
        loadRemoteMovieListUseCase.execute(
                movieEntityPagingData -> {
                    mutableLiveDataMovies.setValue(movieEntityPagingData);
                    Log.d("View Model", "LoadOK");
                    Log.d("View Model", "Paging Data" + (movieEntityPagingData == null));
                },
                t -> Log.d("ERROR", "Load remote list failed"),
                () -> Log.d("Finished", "Load Remote Finished"));
        Log.d("ViewModel", "Load started");
    }

    private boolean checkConditions(){
        return (fromRating.getValue() != null) && (fromYear.getValue() != null) && (sortBy.getValue() != null) && (movieLoadType.getValue() != null);
    }
}

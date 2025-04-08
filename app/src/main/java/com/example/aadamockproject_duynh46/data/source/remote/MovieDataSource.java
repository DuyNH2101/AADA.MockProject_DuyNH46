package com.example.aadamockproject_duynh46.data.source.remote;




import static com.example.aadamockproject_duynh46.data.utils.Constants.API_KEY;
import static com.example.aadamockproject_duynh46.data.utils.Constants.POPULAR_LOAD_TYPE;
import static com.example.aadamockproject_duynh46.data.utils.Constants.SORT_BY_NONE;
import static com.example.aadamockproject_duynh46.data.utils.Constants.SORT_BY_RATING;
import static com.example.aadamockproject_duynh46.data.utils.Constants.SORT_BY_RELEASE_DATE;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxPagingSource;

import com.example.aadamockproject_duynh46.data.source.local.MovieDatabase;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;



public class MovieDataSource extends RxPagingSource<Integer, MovieModel> {
    private List<MovieModel> movieList;
    private RetrofitAPI retrofitAPI;
    private MovieDatabase movieDatabase;

    private String movieLoadType = POPULAR_LOAD_TYPE;

    private float fromRating = 0f;
    private int fromYear = 0;
    private String sortBy = SORT_BY_NONE;

    public void setMovieLoadType(String movieLoadType) {
        this.movieLoadType = movieLoadType;
    }

    public void setFromRating(float fromRating) {
        this.fromRating = fromRating;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Inject
    public MovieDataSource(RetrofitAPI retrofitAPI, MovieDatabase movieDatabase){
        this.retrofitAPI = retrofitAPI;
        this.movieDatabase = movieDatabase;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, MovieModel> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition != null) {
            return (anchorPosition / 20) + 1;
        }
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, MovieModel>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
        Log.d("Datasource", "loadSingle");
        return retrofitAPI.getPageByTypeAndPageNumber(movieLoadType, API_KEY, page)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    movieList = response.getResults();
                    ExecutorService executor = Executors.newFixedThreadPool(4);
                    List<MovieModel> filteredList = movieList.stream()
                            .filter(movie -> movie.getVoteAverage() >= fromRating)
                            .filter(movie -> {
                                String[] temp = movie.getReleaseDate().split("-");
                                if(temp[0] == null){
                                    return false;
                                }
                                return Integer.parseInt(temp[0]) >= fromYear;
                            })
                            .peek(movie -> executor.submit(() -> movie.setFav(movieDatabase.favoriteMovieDao().existFavoriteMovieWithId(movie.getId()))))
                            .collect(Collectors.toList());
                    executor.shutdown();

                    switch (sortBy){
                        case SORT_BY_RATING:{
                            filteredList.sort(Comparator.comparing(MovieModel::getVoteAverage));
                            break;
                        }
                        case SORT_BY_RELEASE_DATE:{
                            filteredList.sort(Comparator.comparing(MovieModel::getReleaseDate));
                            break;
                        }
                    }
                    Log.d("Datasource", "loadOK, filter size" + filteredList.size());
                    return toLoadResult(filteredList, page);
                })
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<Integer, MovieModel> toLoadResult(List<MovieModel> results, Integer page) {
        return new LoadResult.Page<>(
                results,
                page == 1 ? null : page - 1,
                page < 500 ? page + 1 : null
        );
    }
}

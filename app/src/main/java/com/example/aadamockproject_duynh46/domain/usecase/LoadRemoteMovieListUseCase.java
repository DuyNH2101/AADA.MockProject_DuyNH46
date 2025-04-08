package com.example.aadamockproject_duynh46.domain.usecase;

import android.util.Log;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava2.PagingRx;

import com.example.aadamockproject_duynh46.data.source.remote.MovieDataSource;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.usecase.base.FlowableUseCase;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
@Singleton
public class LoadRemoteMovieListUseCase extends FlowableUseCase<PagingData<MovieModel>> {
    private final Flowable<PagingData<MovieModel>> pagingDataFlowable;
    private final MovieDataSource movieDataSource;


    @Inject
    public LoadRemoteMovieListUseCase(MovieDataSource movieDataSource){
        Pager<Integer, MovieModel> pager = new Pager<>(
                new PagingConfig(
                        20,
                        5,
                        false,
                        10,
                        30
                ),
                () -> movieDataSource);
        this.pagingDataFlowable = PagingRx.getFlowable(pager);
        this.movieDataSource = movieDataSource;
    }
    @Override
    protected Flowable<PagingData<MovieModel>> buildUseCaseFlowable() {
        return pagingDataFlowable;
    }

    public void setMovieLoadType(String loadType){
        Log.d("LoadRemoteListUsecase", "setMovieLoadType " + loadType);
        movieDataSource.setMovieLoadType(loadType);
    }



    public void setFromRating(float fromRating){
        movieDataSource.setFromRating(fromRating);
    }

    public void setFromYear(int fromYear){
        movieDataSource.setFromYear(fromYear);
    }

    public void setSortBy(String sortBy){
        movieDataSource.setSortBy(sortBy);
    }


}

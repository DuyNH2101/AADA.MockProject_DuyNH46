package com.example.aadamockproject_duynh46.domain.usecase;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava2.PagingRx;

import com.example.aadamockproject_duynh46.data.source.remote.CreditsDataSource;
import com.example.aadamockproject_duynh46.domain.model.PeopleModel;
import com.example.aadamockproject_duynh46.domain.usecase.base.FlowableUseCase;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;


@Singleton
public class LoadCastAndCrewUseCase extends FlowableUseCase<PagingData<PeopleModel>> {

    private final CreditsDataSource.Factory dataSourceFactory;

    private CreditsDataSource creditsDataSource;
    private Flowable<PagingData<PeopleModel>> pagingDataFlowable;

    private int movieId = -1;

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Inject
    public LoadCastAndCrewUseCase(CreditsDataSource.Factory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    protected Flowable<PagingData<PeopleModel>> buildUseCaseFlowable() {
        if(movieId==-1){
            return null;
        }
        if (creditsDataSource == null){
            creditsDataSource = dataSourceFactory.create(movieId);
        }
        if(pagingDataFlowable == null){
            Pager<Integer, PeopleModel> pager = new Pager<>(
                    new PagingConfig(
                            20,
                            5,
                            false,
                            10,
                            30),
                    () -> dataSourceFactory.create(movieId)
            );
            pagingDataFlowable = PagingRx.getFlowable(pager);
        }
        return pagingDataFlowable;
    }
}

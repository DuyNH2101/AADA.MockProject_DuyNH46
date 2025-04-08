package com.example.aadamockproject_duynh46.data.source.remote;

import static com.example.aadamockproject_duynh46.data.utils.Constants.API_KEY;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxPagingSource;

import com.example.aadamockproject_duynh46.domain.model.CreditsModel;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.model.PeopleModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class CreditsDataSource extends RxPagingSource<Integer, PeopleModel> {

    private RetrofitAPI retrofitAPI;
    private List<PeopleModel> fullCastAndCrewList;

    private static final int PAGE_SIZE = 20;
    private int movieId;

    @AssistedInject
    public CreditsDataSource(RetrofitAPI retrofitAPI, @Assisted int movieId){
        this.retrofitAPI = retrofitAPI;
        this.movieId = movieId;
    }

    @AssistedFactory
    public interface Factory {
        CreditsDataSource create(int movieId);
    }



    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, PeopleModel> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition != null) {
            return (anchorPosition / 20) + 1;
        }
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, PeopleModel>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;

        if (fullCastAndCrewList != null) {
            return Single.just(toLoadResult(fullCastAndCrewList, page));
        }

        return retrofitAPI.getMovieCredits(movieId, API_KEY)
                .subscribeOn(Schedulers.io())
                .map(creditsModel -> {
                    fullCastAndCrewList = new ArrayList<>();
                    fullCastAndCrewList.addAll(creditsModel.getCast());
                    fullCastAndCrewList.addAll(creditsModel.getCrew());
                    Log.d("SIZE", "" + fullCastAndCrewList.size());
                    return toLoadResult(fullCastAndCrewList, page);
                });
    }

    private LoadResult<Integer, PeopleModel> toLoadResult(List<PeopleModel> results, Integer page) {
        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, results.size());

        if (fromIndex >= results.size()) {
            return new LoadResult.Page<>(Collections.emptyList(), null, null);
        }

        List<PeopleModel> pageList = results.subList(fromIndex, toIndex);

        return new LoadResult.Page<>(
                pageList,
                page == 1 ? null : page - 1,
                page < (results.size()/PAGE_SIZE + 1) ? page + 1 : null
        );
    }
}

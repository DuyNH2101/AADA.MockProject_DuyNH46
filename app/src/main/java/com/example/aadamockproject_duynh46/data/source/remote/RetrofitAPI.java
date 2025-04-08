package com.example.aadamockproject_duynh46.data.source.remote;



import com.example.aadamockproject_duynh46.domain.model.CreditsModel;
import com.example.aadamockproject_duynh46.domain.model.PageModel;

import javax.inject.Singleton;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Singleton
public interface RetrofitAPI {
    @GET("{load_type}")
    Single<PageModel> getPageByTypeAndPageNumber(
            @Path("load_type") String loadType,
            @Query("api_key") String apiKey,
            @Query("page") int page);

    @GET("{movie_id}/credits")
    Single<CreditsModel> getMovieCredits(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey);
}


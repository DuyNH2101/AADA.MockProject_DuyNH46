package com.example.aadamockproject_duynh46.data.di;

import static com.example.aadamockproject_duynh46.data.utils.Constants.BASE_URL;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava2.PagingRx;

import com.example.aadamockproject_duynh46.data.source.local.MovieDatabase;
import com.example.aadamockproject_duynh46.data.source.remote.MovieDataSource;
import com.example.aadamockproject_duynh46.data.source.remote.RetrofitAPI;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Provides
    @Singleton
    public static Retrofit provideRetrofit(RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                                           GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Singleton
    @Provides
    public static GsonConverterFactory provideGsonConverterFactory(){
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    public static RxJava2CallAdapterFactory provideRxJava2CallAdapterFactory(){
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    public static RetrofitAPI provideRetrofitAPI(Retrofit retrofit){
        return retrofit.create(RetrofitAPI.class);
    }

    @Provides
    @Singleton
    public static MovieDataSource provideMovieDataSource(RetrofitAPI retrofitAPI, MovieDatabase movieDatabase){
        return new MovieDataSource(retrofitAPI, movieDatabase);
    }


}

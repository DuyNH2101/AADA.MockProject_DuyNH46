package com.example.aadamockproject_duynh46.data.di;


import androidx.paging.PagingData;

import com.example.aadamockproject_duynh46.data.repository.FirebaseUserRepositoryImpl;
import com.example.aadamockproject_duynh46.data.repository.MovieRepositoryImpl;
import com.example.aadamockproject_duynh46.data.repository.ReminderRepositoryImpl;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.repository.MovieRepository;
import com.example.aadamockproject_duynh46.domain.repository.ReminderRepository;
import com.example.aadamockproject_duynh46.domain.repository.UserRepository;
import com.example.aadamockproject_duynh46.domain.usecase.LoadRemoteMovieListUseCase;
import com.example.aadamockproject_duynh46.domain.usecase.base.FlowableUseCase;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public abstract class BindModule {
    @Binds
    @Singleton
    public abstract MovieRepository provideMovieRepository(MovieRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract ReminderRepository provideReminderRepository(ReminderRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract UserRepository provideUserRepository(FirebaseUserRepositoryImpl impl);
}

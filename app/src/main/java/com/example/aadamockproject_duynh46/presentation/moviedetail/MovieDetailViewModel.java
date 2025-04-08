package com.example.aadamockproject_duynh46.presentation.moviedetail;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.aadamockproject_duynh46.domain.model.CreditsModel;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.model.PeopleModel;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;
import com.example.aadamockproject_duynh46.domain.usecase.LoadCastAndCrewUseCase;
import com.example.aadamockproject_duynh46.domain.usecase.ReminderUseCase;
import com.example.aadamockproject_duynh46.framework.worker.NotificationWorker;

import java.util.Calendar;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MovieDetailViewModel extends ViewModel {
    private MutableLiveData<MovieModel> mutableLiveDataMovieDetail = new MutableLiveData<>();

    private MutableLiveData<ReminderModel> mutableLiveDataReminder = new MutableLiveData<>();

    private MutableLiveData<PagingData<PeopleModel>> mutableLiveDataListCredits = new MutableLiveData<>();


    private final LoadCastAndCrewUseCase loadCastAndCrewUseCase;

    private final ReminderUseCase reminderUseCase;

    public MutableLiveData<MovieModel> getMutableLiveDataMovieDetail() {
        return mutableLiveDataMovieDetail;
    }

    public MutableLiveData<ReminderModel> getMutableLiveDataReminder() {
        return mutableLiveDataReminder;
    }

    public MutableLiveData<PagingData<PeopleModel>> getMutableLiveDataListCredits() {
        return mutableLiveDataListCredits;
    }

    @Inject
    public MovieDetailViewModel(LoadCastAndCrewUseCase loadCastAndCrewUseCase, ReminderUseCase reminderUseCase){
        this.loadCastAndCrewUseCase = loadCastAndCrewUseCase;
        this.reminderUseCase = reminderUseCase;
    }

    public void loadCastAndCrewList(){
        if(mutableLiveDataMovieDetail.getValue()==null){
            return;
        }
        loadCastAndCrewUseCase.setMovieId(mutableLiveDataMovieDetail.getValue().getId());
        loadCastAndCrewUseCase.execute(
                peopleModelPagingData -> {
                    mutableLiveDataListCredits.setValue(peopleModelPagingData);
                },
                t -> Log.d("MovieDetailViewModel", "Load Cast and Crew Failed"),
                () -> {});
    }
    public void loadMovieReminder(){
        if(mutableLiveDataMovieDetail.getValue()==null){
            return;
        }
        mutableLiveDataReminder.setValue(reminderUseCase.getReminderByMovieId(mutableLiveDataMovieDetail.getValue().getId()));
    }
    public ReminderModel createNotification(Context context,
                                   int selectedYear,
                                   int selectedMonth,
                                   int selectedDay,
                                   int selectedHour,
                                   int selectedMinute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
        Log.d("INFOTIME", "year: " + selectedYear + " month: " + selectedMonth + " day: " + selectedDay + " hour: " + selectedHour + " minute: " + selectedMinute);
        long time;

        time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
        if (System.currentTimeMillis() > time) {
            if (Calendar.AM_PM == 0)
                time = time + (1000 * 60 * 60 * 12);
            else
                time = time + (1000 * 60 * 60 * 24);
        }

        ReminderModel newReminder = new ReminderModel();
        newReminder.setMovie(mutableLiveDataMovieDetail.getValue());
        newReminder.setTimeInMillis(time);
        newReminder.setTimeInString(selectedYear + "/" + selectedMonth + "/" + selectedDay + " " + selectedHour + ":" + selectedMinute);


        Data inputData = new Data.Builder()
                .putInt("MOVIE_ID", newReminder.getMovie().getId())
                .build();

        calendar.setTimeInMillis(System.currentTimeMillis());
        long currentTime = calendar.getTimeInMillis();
        WorkRequest notificationWorkRequest =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .setInputData(inputData)
                        .setInitialDelay(newReminder.getTimeInMillis() - currentTime, MILLISECONDS)
                        .build();
        WorkManager.getInstance(context).enqueue(notificationWorkRequest);
        return newReminder;
    }
}

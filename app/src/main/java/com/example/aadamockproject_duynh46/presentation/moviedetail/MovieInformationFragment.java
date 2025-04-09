package com.example.aadamockproject_duynh46.presentation.moviedetail;



import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import com.example.aadamockproject_duynh46.databinding.FragmentMovieInformationBinding;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.framework.worker.NotificationWorker;
import com.example.aadamockproject_duynh46.presentation.favoritelist.FavoriteMovieListViewModel;
import com.example.aadamockproject_duynh46.presentation.main.MovieListViewModel;
import com.example.aadamockproject_duynh46.presentation.main.ToolbarChangeViewModel;
import com.example.aadamockproject_duynh46.presentation.reminderlist.ReminderListViewModel;

import java.util.Calendar;


import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MovieInformationFragment extends Fragment {
    private FragmentMovieInformationBinding binding;

    private CastAndCrewListAdapter castAndCrewListAdapter;

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;
    private Calendar calendar;

    private MovieDetailViewModel detailViewModel;
    private ReminderListViewModel reminderListViewModel;


    public MovieInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieInformationBinding.inflate(inflater, container, false);


        detailViewModel = new ViewModelProvider(requireActivity()).get(MovieDetailViewModel.class);
        FavoriteMovieListViewModel favoriteMovieListViewModel = new ViewModelProvider(requireActivity()).get(FavoriteMovieListViewModel.class);
        ToolbarChangeViewModel toolbarChangeViewModel = new ViewModelProvider(requireActivity()).get(ToolbarChangeViewModel.class);

        MovieModel movieModel = detailViewModel.getMutableLiveDataMovieDetail().getValue();

        toolbarChangeViewModel.updateCurrentFragment("movie_detail");

        reminderListViewModel = new ViewModelProvider(requireActivity()).get(ReminderListViewModel.class);

        binding.setMovie(movieModel);

        detailViewModel.loadCastAndCrewList();
        detailViewModel.loadMovieReminder();

        if(!detailViewModel.getMutableLiveDataListCredits().hasObservers()){
            detailViewModel.getMutableLiveDataListCredits().observe(getViewLifecycleOwner(), peopleModelPagingData -> {
                castAndCrewListAdapter = new CastAndCrewListAdapter();

                binding.castAndCrewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                binding.castAndCrewRecyclerView.setAdapter(castAndCrewListAdapter);
                binding.loadCastAndCrewProgressbar.setVisibility(View.GONE);

                if(peopleModelPagingData != null){
                    castAndCrewListAdapter.submitData(getLifecycle(), peopleModelPagingData);
                }

            });
        }

        favoriteMovieListViewModel.getMutableLiveDataChangedFavMovie().observe(getViewLifecycleOwner(), movieModel1 -> {
            if(movieModel.getId() == movieModel1.getId()){
                movieModel.setFav(movieModel1.isFav());
                binding.setMovie(movieModel);
            }
        });

        binding.movieInfoIsFavStar.setOnClickListener(l->{
            movieModel.setFav(!movieModel.isFav());
            if(movieModel.isFav()){
                favoriteMovieListViewModel.addFavoriteMovie(movieModel);
            } else {
                favoriteMovieListViewModel.removeFavoriteMovie(movieModel);
            }
        });

        detailViewModel.getMutableLiveDataReminder().observe(getViewLifecycleOwner(), reminderModel -> {
            if(reminderModel != null){
                binding.movieInfoReminderTime.setText(reminderModel.getTimeInString());
                binding.movieInfoReminderTime.setVisibility(View.VISIBLE);
            } else {
                binding.movieInfoReminderTime.setText("");
                binding.movieInfoReminderTime.setVisibility(View.GONE);
            }
        });

        binding.movieInfoReminderBtn.setOnClickListener(l->{
            calendar = Calendar.getInstance();
            showDatePickerDialog();
        });

        reminderListViewModel.getMutableLiveDataChangedReminder().observe(getViewLifecycleOwner(), reminderModel -> {
            detailViewModel.loadMovieReminder();
        });

        return binding.getRoot();
    }
    private void showDatePickerDialog() {
        int year = (selectedYear != 0) ? selectedYear : calendar.get(Calendar.YEAR);
        int month = (selectedYear != 0) ? selectedMonth : calendar.get(Calendar.MONTH);
        int day = (selectedYear != 0) ? selectedDay : calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog,
                (view, pickedYear, pickedMonth, pickedDay) -> {
                    selectedYear = pickedYear;
                    selectedMonth = pickedMonth;
                    selectedDay = pickedDay;

                    showTimePickerDialog();
                },
                year, month, day
        );
        datePickerDialog.getDatePicker().setOnDateChangedListener((datePicker, i, i1, i2) -> {
            StringBuilder tempSB = new StringBuilder();
            calendar.set(i, i1, i2);
            tempSB.append(getDayOfWeekString())
                    .append((calendar.get(Calendar.MONTH)+1<=9)
                            ? ("0" + (calendar.get(Calendar.MONTH)+1))
                            : (calendar.get(Calendar.MONTH)+1))
                    .append("/")
                    .append((calendar.get(Calendar.DAY_OF_MONTH)<=9)
                            ? ("0" + (calendar.get(Calendar.DAY_OF_MONTH)))
                            : (calendar.get(Calendar.DAY_OF_MONTH)))
                    .append("/")
                    .append(calendar.get(Calendar.YEAR));
            datePickerDialog.setTitle(tempSB.toString());
        });

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = (selectedHour != 0) ? selectedHour : calendar.get(Calendar.HOUR_OF_DAY);
        int minute = (selectedHour != 0) ? selectedMinute : calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog,
                (view, pickedHour, pickedMinute) -> {
                    selectedHour = pickedHour;
                    selectedMinute = pickedMinute;

                    createNotification();

                },
                hour, minute, true
        );


        if (selectedYear == calendar.get(Calendar.YEAR) &&
                selectedMonth == calendar.get(Calendar.MONTH) &&
                selectedDay == calendar.get(Calendar.DAY_OF_MONTH)) {

            timePickerDialog.updateTime(Math.max(calendar.get(Calendar.HOUR_OF_DAY), hour),
                    Math.max(calendar.get(Calendar.MINUTE), minute));
        }
        timePickerDialog.setTitle("Set Time");
        timePickerDialog.show();
    }
    public void createNotification(){
        reminderListViewModel.addReminder(detailViewModel.createNotification(requireContext(),
                selectedYear,
                selectedMonth,
                selectedDay,
                selectedHour,
                selectedMinute));
    }
    @NonNull
    private String getDayOfWeekString(){
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:{
                return "Sun, ";
            }
            case 2:{
                return "Mon, ";
            }
            case 3:{
                return "Tue, ";
            }
            case 4:{
                return "Wed, ";
            }
            case 5:{
                return "Thu, ";
            }
            case 6:{
                return "Fri, ";
            }
            case 7:{
                return "Sat, ";
            }
            default:{
                return "";
            }
        }
    }


}
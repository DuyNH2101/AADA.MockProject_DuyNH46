package com.example.aadamockproject_duynh46.presentation.reminderlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.databinding.FragmentReminderListBinding;
import com.example.aadamockproject_duynh46.presentation.main.ToolbarChangeViewModel;
import com.example.aadamockproject_duynh46.presentation.moviedetail.MovieDetailViewModel;


public class ReminderListFragment extends Fragment {




    public ReminderListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReminderListBinding binding = FragmentReminderListBinding.inflate(inflater, container, false);

        ToolbarChangeViewModel toolbarChangeViewModel = new ViewModelProvider(requireActivity()).get(ToolbarChangeViewModel.class);
        toolbarChangeViewModel.updateCurrentFragment("reminder_list");

        ReminderListViewModel reminderListViewModel = new ViewModelProvider(requireActivity()).get(ReminderListViewModel.class);

        MovieDetailViewModel movieDetailViewModel = new ViewModelProvider(requireActivity()).get(MovieDetailViewModel.class);


        binding.reminderListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ReminderListAdapter reminderListAdapter = new ReminderListAdapter(getContext(), reminderListViewModel, movieDetailViewModel);
        binding.reminderListRecyclerView.setAdapter(reminderListAdapter);


        if(reminderListViewModel.getMutableLiveDataAllReminderList().getValue() != null){
            reminderListAdapter.submitList(reminderListViewModel.getMutableLiveDataAllReminderList().getValue());
        }


        reminderListViewModel.getMutableLiveDataAllReminderList().observe(getViewLifecycleOwner(), reminderModels -> {
            if(reminderModels != null){
                reminderListAdapter.submitList(reminderModels);
            }
        });


        return binding.getRoot();
    }
}
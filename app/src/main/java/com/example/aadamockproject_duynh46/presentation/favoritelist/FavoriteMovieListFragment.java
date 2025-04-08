package com.example.aadamockproject_duynh46.presentation.favoritelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aadamockproject_duynh46.databinding.FragmentMovieListBinding;
import com.example.aadamockproject_duynh46.presentation.main.MovieListViewModel;


import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoriteMovieListFragment extends Fragment {

    private FavoriteMovieListAdapter movieListAdapter;

    private FragmentMovieListBinding binding;

    private FavoriteMovieListViewModel favoriteViewModel;


    public FavoriteMovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieListBinding.inflate(inflater, container, false);

        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteMovieListViewModel.class);

        movieListAdapter = new FavoriteMovieListAdapter(favoriteViewModel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.movieListRecyclerView.setLayoutManager(layoutManager);
        binding.movieListRecyclerView.setAdapter(movieListAdapter);
        binding.listLinearFragPBLoading.setVisibility(View.GONE);

        if(favoriteViewModel.getMutableLiveDataFavoriteMovies().getValue() != null){
            movieListAdapter.submitList(favoriteViewModel.getMutableLiveDataFavoriteMovies().getValue());
        }

        favoriteViewModel.getMutableLiveDataFavoriteMovies().observe(getViewLifecycleOwner(), movieModels -> {
            movieListAdapter.submitList(movieModels);
        });

        favoriteViewModel.getMutableLiveDataFilteredFavoriteMovies().observe(getViewLifecycleOwner(), movieModels -> {
            if(movieModels != null){
                movieListAdapter.submitList(movieModels);
            } else {
                movieListAdapter.submitList(favoriteViewModel.getMutableLiveDataFavoriteMovies().getValue());
            }
        });



        return binding.getRoot();
    }
}
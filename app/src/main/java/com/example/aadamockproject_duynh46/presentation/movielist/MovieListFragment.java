package com.example.aadamockproject_duynh46.presentation.movielist;

import static com.example.aadamockproject_duynh46.data.utils.Constants.POPULAR_LOAD_TYPE;
import static com.example.aadamockproject_duynh46.data.utils.Constants.SORT_BY_NONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;
import com.example.aadamockproject_duynh46.databinding.FragmentMovieListBinding;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.presentation.favoritelist.FavoriteMovieListViewModel;
import com.example.aadamockproject_duynh46.presentation.main.MovieListViewModel;
import com.example.aadamockproject_duynh46.presentation.main.ToolbarChangeViewModel;
import com.example.aadamockproject_duynh46.presentation.moviedetail.MovieDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class MovieListFragment extends Fragment {

    private MovieListAdapter movieListAdapter;

    private FragmentMovieListBinding binding;

    private MovieListViewModel movieListViewModel;
    private FavoriteMovieListViewModel favoriteViewModel;


    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieListBinding.inflate(inflater, container, false);

        movieListViewModel = new ViewModelProvider(requireActivity()).get(MovieListViewModel.class);
        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteMovieListViewModel.class);
        ToolbarChangeViewModel toolbarChangeViewModel = new ViewModelProvider(requireActivity()).get(ToolbarChangeViewModel.class);
        toolbarChangeViewModel.updateCurrentFragment("movie_list");

        if(movieListAdapter == null){
            movieListAdapter = new MovieListAdapter(favoriteViewModel, new ViewModelProvider(requireActivity()).get(MovieDetailViewModel.class));
            binding.movieListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.movieListRecyclerView.setAdapter(movieListAdapter);
        } else{
            binding.movieListRecyclerView.setAdapter(movieListAdapter);
            if(movieListAdapter.getCurrentListType()==MovieListAdapter.IS_LINEAR){
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                binding.movieListRecyclerView.setLayoutManager(layoutManager);
            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                binding.movieListRecyclerView.setLayoutManager(layoutManager);
            }
        }

        binding.listLinearFragPBLoading.setVisibility(View.GONE);

        if(!movieListViewModel.getMutableLiveDataMovies().hasObservers()){
            movieListViewModel.getMutableLiveDataMovies().observe(requireActivity(), movieEntityPagingData -> {
                Log.e("MovieFragment", "Observed changes");
                movieListAdapter.submitData(getLifecycle(), movieEntityPagingData);
            });
        }


        movieListViewModel.getMovieLoadType().observe(requireActivity(), s -> {
            Log.e("MovieFragment", "Load type change");
            movieListViewModel.loadRemoteMovieList();
        });
        movieListViewModel.getFromYear().observe(requireActivity(), integer -> {
            Log.e("MovieFragment", "From year change");
            movieListViewModel.loadRemoteMovieList();
        });

        movieListViewModel.getFromRating().observe(requireActivity(), aFloat -> {
            Log.e("MovieFragment", "Rating change");
            movieListViewModel.loadRemoteMovieList();
        });

        movieListViewModel.getSortBy().observe(requireActivity(), s -> {
            Log.e("MovieFragment", "sortBy change");
            movieListViewModel.loadRemoteMovieList();
        });

        favoriteViewModel.getMutableLiveDataChangedFavMovie().observe(requireActivity(), movieModel -> {
            for(int i = 0; i < movieListAdapter.snapshot().size(); i++){
                if(movieListAdapter.snapshot().get(i).getId() == movieModel.getId()){
                    movieListAdapter.snapshot().get(i).setFav(movieModel.isFav());
                    movieListAdapter.notifyItemChanged(i);
                }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String loadType = sharedPreferences.getString("category", POPULAR_LOAD_TYPE);
        int fromYear = Integer.parseInt(sharedPreferences.getString("fromYear", "0"));
        float fromRating = sharedPreferences.getInt("fromRating", 0)/10.0f;
        String sortBy = sharedPreferences.getString("sortBy", SORT_BY_NONE);

        movieListViewModel.getMovieLoadType().setValue(loadType);
        movieListViewModel.getFromYear().setValue(fromYear);
        movieListViewModel.getFromRating().setValue(fromRating);
        movieListViewModel.getSortBy().setValue(sortBy);

        return binding.getRoot();
    }

    public boolean changeListViewType(){
        boolean isLinearList = movieListAdapter.changeListType();
        RecyclerView.LayoutManager currentLayoutManager = binding.movieListRecyclerView.getLayoutManager();
        int position = 0;
        if (currentLayoutManager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) currentLayoutManager).findFirstVisibleItemPosition();
        } else if (currentLayoutManager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) currentLayoutManager).findFirstVisibleItemPosition();
        }
        if(isLinearList){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.movieListRecyclerView.setLayoutManager(layoutManager);
            layoutManager.scrollToPosition(position*6/4);
        } else{
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            binding.movieListRecyclerView.setLayoutManager(layoutManager);
            layoutManager.scrollToPosition(position*4/6);
        }
        return isLinearList;
    }
}
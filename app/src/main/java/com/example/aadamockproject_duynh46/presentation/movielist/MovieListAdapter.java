package com.example.aadamockproject_duynh46.presentation.movielist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;
import com.example.aadamockproject_duynh46.databinding.MovieItemViewGridBinding;
import com.example.aadamockproject_duynh46.databinding.MovieItemViewLinearBinding;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;

import com.example.aadamockproject_duynh46.presentation.favoritelist.FavoriteMovieListViewModel;
import com.example.aadamockproject_duynh46.presentation.moviedetail.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Singleton;

@Singleton
public class MovieListAdapter extends PagingDataAdapter<MovieModel, RecyclerView.ViewHolder> {

    public static final int IS_LINEAR = 0;
    public static final int IS_GRID = 1;

    private int currentListType = IS_LINEAR;
    private FavoriteMovieListViewModel favoriteViewModel;

    private MovieDetailViewModel movieDetailViewModel;

    public MovieListAdapter(FavoriteMovieListViewModel favoriteViewModel, MovieDetailViewModel movieDetailViewModel) {
        super(DIFF_CALLBACK);
        this.favoriteViewModel = favoriteViewModel;
        this.movieDetailViewModel = movieDetailViewModel;
    }

    public boolean changeListType(){
        if(currentListType == IS_LINEAR){
            currentListType = IS_GRID;
        } else currentListType = IS_LINEAR;

        return currentListType == IS_LINEAR;
    }
    public int getCurrentListType(){
        return currentListType;
    }

    @Override
    public int getItemViewType(int position) {
        return currentListType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == IS_LINEAR){
            MovieItemViewLinearBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item_view_linear, parent, false);
            return new LinearViewHolder(binding);
        } else {
            MovieItemViewGridBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item_view_grid, parent, false);
            return new GridViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieModel movieModel = getItem(position);
        Log.d("Adapter", "Position Bind: " + position);
        if(holder instanceof LinearViewHolder){
            LinearViewHolder tempHolder = ((LinearViewHolder) holder);
            tempHolder.linearBinding.itemViewLinearMovieName.setText(movieModel.getTitle());
            tempHolder.linearBinding.itemViewLinearValueReleaseDate.setText(movieModel.getReleaseDate());
            tempHolder.linearBinding.itemViewLinearValueRating.setText(String.format("%s/10", movieModel.getVoteAverage()));
            tempHolder.linearBinding.itemViewLinearOverview.setText(movieModel.getOverview());
            if(!movieModel.isAdult()){
                tempHolder.linearBinding.itemViewLinearIsAdultIcon.setVisibility(View.GONE);
            }
            if(movieModel.isFav()){
                tempHolder.linearBinding.itemViewLinearIsFavStar.setImageResource(R.drawable.ic_fav_star);
            } else{
                tempHolder.linearBinding.itemViewLinearIsFavStar.setImageResource(R.drawable.ic_not_fav_star);
            }
            Picasso.get().load("https://image.tmdb.org/t/p/original/" + movieModel.getPosterPath())
                    .placeholder(R.drawable.ic_default_image)
                    .error(R.drawable.ic_error_image)
                    .into(tempHolder.linearBinding.itemViewLinearMoviePoster);

            tempHolder.linearBinding.itemViewLinearIsFavStar.setOnClickListener(l->{
                movieModel.setFav(!movieModel.isFav());
                if(movieModel.isFav()){
                    favoriteViewModel.addFavoriteMovie(movieModel);
                    tempHolder.linearBinding.itemViewLinearIsFavStar.setImageResource(R.drawable.ic_fav_star);
                } else {
                    favoriteViewModel.removeFavoriteMovie(movieModel);
                    tempHolder.linearBinding.itemViewLinearIsFavStar.setImageResource(R.drawable.ic_not_fav_star);
                }
            });

            tempHolder.itemView.setOnClickListener(v -> {
                movieDetailViewModel.getMutableLiveDataMovieDetail().setValue(movieModel);
            });


        } else if (holder instanceof GridViewHolder){

            GridViewHolder tempHolder = ((GridViewHolder) holder);
            tempHolder.gridBinding.itemViewGridMovieName.setText(movieModel.getTitle());
            Picasso.get().load("https://image.tmdb.org/t/p/original/" + movieModel.getPosterPath())
                    .placeholder(R.drawable.ic_default_image)
                    .error(R.drawable.ic_error_image)
                    .into(tempHolder.gridBinding.itemViewGridMoviePoster);

            tempHolder.itemView.setOnClickListener(v -> {
                movieDetailViewModel.getMutableLiveDataMovieDetail().setValue(movieModel);
            });
        }

    }

    public static class LinearViewHolder extends RecyclerView.ViewHolder {
        MovieItemViewLinearBinding linearBinding;

        public LinearViewHolder(@NonNull MovieItemViewLinearBinding linearBinding) {
            super(linearBinding.getRoot());
            this.linearBinding = linearBinding;
        }
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        MovieItemViewGridBinding gridBinding;

        public GridViewHolder(@NonNull MovieItemViewGridBinding gridBinding) {
            super(gridBinding.getRoot());
            this.gridBinding = gridBinding;
        }
    }
    public static DiffUtil.ItemCallback<MovieModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@android.support.annotation.NonNull MovieModel oldItem, @android.support.annotation.NonNull MovieModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@android.support.annotation.NonNull MovieModel oldItem, @android.support.annotation.NonNull MovieModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}

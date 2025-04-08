package com.example.aadamockproject_duynh46.presentation.favoritelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;
import com.example.aadamockproject_duynh46.databinding.MovieItemViewLinearBinding;

import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.presentation.main.MovieListViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Singleton;

@Singleton
public class FavoriteMovieListAdapter extends RecyclerView.Adapter<FavoriteMovieListAdapter.LinearViewHolder> {

    private final AsyncListDiffer<MovieModel> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private FavoriteMovieListViewModel favoriteViewModel;

    public FavoriteMovieListAdapter(FavoriteMovieListViewModel favoriteViewModel){
        this.favoriteViewModel = favoriteViewModel;
    }
    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieItemViewLinearBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item_view_linear, parent, false);
        return new LinearViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        MovieModel movieModel = differ.getCurrentList().get(position);

        holder.linearBinding.itemViewLinearMovieName.setText(movieModel.getTitle());
        holder.linearBinding.itemViewLinearValueReleaseDate.setText(movieModel.getReleaseDate());
        holder.linearBinding.itemViewLinearValueRating.setText(String.format("%s/10", movieModel.getVoteAverage()));
        holder.linearBinding.itemViewLinearOverview.setText(movieModel.getOverview());
        if (!movieModel.isAdult()) {
            holder.linearBinding.itemViewLinearIsAdultIcon.setVisibility(View.GONE);
        }
        if (movieModel.isFav()) {
            holder.linearBinding.itemViewLinearIsFavStar.setImageResource(R.drawable.ic_fav_star);
        } else {
            holder.linearBinding.itemViewLinearIsFavStar.setImageResource(R.drawable.ic_not_fav_star);
        }
        Picasso.get().load("https://image.tmdb.org/t/p/original/" + movieModel.getPosterPath())
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_error_image)
                .into(holder.linearBinding.itemViewLinearMoviePoster);


        holder.linearBinding.itemViewLinearIsFavStar.setOnClickListener(v -> {
            movieModel.setFav(false);
            favoriteViewModel.removeFavoriteMovie(movieModel);
        });

    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class LinearViewHolder extends RecyclerView.ViewHolder {
        MovieItemViewLinearBinding linearBinding;

        public LinearViewHolder(@NonNull MovieItemViewLinearBinding linearBinding) {
            super(linearBinding.getRoot());
            this.linearBinding = linearBinding;
        }
    }

    public void submitList(ArrayList<MovieModel> newMovieModelList) {
        differ.submitList(newMovieModelList);
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

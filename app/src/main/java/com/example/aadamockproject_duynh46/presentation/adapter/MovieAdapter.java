package com.example.aadamockproject_duynh46.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.Model.Moviedat;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context context;
    private final List<Moviedat> movieList;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Moviedat movie);
    }

    public MovieAdapter(Context context, List<Moviedat> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dat_item_movie_upcomming, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Moviedat movie = movieList.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView releaseDate;
        TextView rating;
        TextView overview;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            title = itemView.findViewById(R.id.movie_title);
            releaseDate = itemView.findViewById(R.id.movie_release_date_value);
            rating = itemView.findViewById(R.id.movie_rating_value);
            overview = itemView.findViewById(R.id.movie_overview_value);
        }

        public void bind(final Moviedat movie, final OnMovieClickListener listener) {
            Glide.with(itemView.getContext())
                 .load(movie.getPosterUrl())
                 .placeholder(R.drawable.ic_movie)
                 .into(poster);

            title.setText(movie.getMovieName());
            releaseDate.setText(movie.getTimeStart());
            rating.setText(movie.getRating() + "/10"); // Set text directly
            overview.setText(movie.getOverview());

            itemView.setOnClickListener(v -> listener.onMovieClick(movie));
        }
    }
}

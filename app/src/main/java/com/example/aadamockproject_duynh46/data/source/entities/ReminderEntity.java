package com.example.aadamockproject_duynh46.data.source.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "reminders")
public class ReminderEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean isAdult;
    private String backdrop_path;
    private int movieId;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private String title;
    private boolean isVideo;
    private double voteAverage;
    private boolean isFav;
    private long timeInMillis;
    private String timeInString;

    public ReminderEntity() {
    }

    public ReminderEntity(int id,MovieEntity movieEntity, long timeInMillis, String timeInString) {
        this.id = id;
        this.isAdult = movieEntity.isAdult();
        this.backdrop_path = movieEntity.getBackdrop_path();
        this.movieId = movieEntity.getId();
        this.overview = movieEntity.getOverview();
        this.posterPath = movieEntity.getPosterPath();
        this.releaseDate = movieEntity.getReleaseDate();
        this.title = movieEntity.getTitle();
        this.isVideo = movieEntity.isVideo();
        this.voteAverage = movieEntity.getVoteAverage();
        this.isFav = movieEntity.isFav();
        this.timeInMillis = timeInMillis;
        this.timeInString = timeInString;
    }
    public ReminderEntity(int id, boolean isAdult, String backdrop_path, int movieId, String overview, String posterPath,String releaseDate,
            String title, boolean isVideo, float voteAverage, boolean isFav, long timeInMillis, String timeInString) {
        this.id = id;
        this.isAdult = isAdult;
        this.backdrop_path = backdrop_path;
        this.movieId = movieId;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.isVideo = isVideo;
        this.voteAverage = voteAverage;
        this.isFav = isFav;
        this.timeInMillis = timeInMillis;
        this.timeInString = timeInString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getTimeInString() {
        return timeInString;
    }

    public void setTimeInString(String timeInString) {
        this.timeInString = timeInString;
    }
}

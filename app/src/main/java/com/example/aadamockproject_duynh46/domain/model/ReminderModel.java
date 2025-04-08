package com.example.aadamockproject_duynh46.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class ReminderModel {
    private int id;
    private MovieModel movie;
    private long timeInMillis;
    private String timeInString;

    public ReminderModel() {
    }

    public ReminderModel(int id, MovieModel movie, long timeInMillis, String timeInString) {
        this.id = id;
        this.movie = movie;
        this.timeInMillis = timeInMillis;
        this.timeInString = timeInString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReminderModel)) return false;
        ReminderModel that = (ReminderModel) o;
        return id == that.id && timeInMillis == that.timeInMillis && Objects.equals(movie, that.movie) && Objects.equals(timeInString, that.timeInString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, timeInMillis, timeInString);
    }
}

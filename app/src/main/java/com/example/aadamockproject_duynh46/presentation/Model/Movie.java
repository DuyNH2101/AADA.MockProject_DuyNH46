package com.example.aadamockproject_duynh46.presentation.Model;

public class Movie {
    private String MovieId;
    private String MovieName;
    private String MovieStatus;
    private String TimeStart;
    private String TimeEnd;


    public Movie() {

    }

    public Movie(String movieName, String movieStatus, String timeStart, String timeEnd, String id) {
        MovieId = id;
        MovieName = movieName;
        MovieStatus = movieStatus;
        TimeStart = timeStart;
        TimeEnd = timeEnd;
    }

    public String getId() {
        return MovieId;
    }

    public void setId(String id) {
        MovieId = id;
    }

    public String getMovieName() { return MovieName; }
    public String getMovieStatus() { return MovieStatus; }
    public String getTimeStart() { return TimeStart; }
    public String getTimeEnd() { return TimeEnd; }
}

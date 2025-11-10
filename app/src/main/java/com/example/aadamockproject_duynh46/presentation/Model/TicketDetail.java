package com.example.aadamockproject_duynh46.presentation.Model;

import com.google.firebase.Timestamp;

public class TicketDetail {
    public String TicketId;
    public String MovieId;
    public String MovieName;
    public Timestamp CreatAt;
    public String TimeStart;
    public String Price;

    public TicketDetail() {
    }

    public TicketDetail(String movieId, String movieName, Timestamp creatAt, String timeStart, String price, String ticketId) {
        TicketId = ticketId;
        MovieId = movieId;
        MovieName = movieName;
        CreatAt = creatAt;
        TimeStart = timeStart;
        Price = price;
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    public String getMovieId() {
        return MovieId;
    }

    public void setMovieId(String movieId) {
        MovieId = movieId;
    }

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String movieName) {
        MovieName = movieName;
    }

    public Timestamp getCreatAt() {
        return CreatAt;
    }

    public void setCreatAt(Timestamp creatAt) {
        CreatAt = creatAt;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}

package com.example.aadamockproject_duynh46.presentation.Model;

import com.google.firebase.Timestamp;

public class Ticket {
    private String TicketId;
    private String Name;
    private String MovieId;
    private String UserId;
    private String StartTime;
    private String EndTime;
    private String Status;
    private int MaxTicket;
    private Timestamp CreatedAt;

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    public Ticket() {}

    public Ticket(String name, String movieId, String userId, String startTime, String endTime, String status, int maxTicket, Timestamp createdAt, String ticketId) {
        TicketId = ticketId;
        Name = name;
        MovieId = movieId;
        UserId = userId;
        StartTime = startTime;
        EndTime = endTime;
        Status = status;
        MaxTicket = maxTicket;
        CreatedAt = createdAt;
    }

    public String getTicketId() {
        return TicketId;
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        CreatedAt = createdAt;
    }

    public String getName() { return Name; }
    public String getMovieId() { return MovieId; }
    public String getUserId() { return UserId; }
    public String getStartTime() { return StartTime; }
    public String getEndTime() { return EndTime; }
    public String getStatus() { return Status; }
    public int getMaxTicket() { return MaxTicket; }
}

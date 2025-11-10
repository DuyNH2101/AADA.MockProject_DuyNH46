package com.example.aadamockproject_duynh46.presentation.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public class Moviedat implements Parcelable {

    private String movieId;
    private String movieName;
    private String timeStart;
    private String timeEnd;
    private String movieStatus;
    private String posterUrl;
    private String overview;
    private String rating;
    private int totalSeats;
    private int remainingSeats;
    private int vipSeats;

    public Moviedat() {}

    // Getters
    public String getMovieId() { return movieId; }
    @PropertyName("MovieName")
    public String getMovieName() { return movieName; }
    @PropertyName("TimeStart")
    public String getTimeStart() { return timeStart; }
    @PropertyName("TimeEnd")
    public String getTimeEnd() { return timeEnd; }
    @PropertyName("MovieStatus")
    public String getMovieStatus() { return movieStatus; }
    @PropertyName("PosterUrl")
    public String getPosterUrl() { return posterUrl; }
    @PropertyName("Overview")
    public String getOverview() { return overview; }
    @PropertyName("Rating")
    public String getRating() { return rating; }
    @PropertyName("TotalSeats")
    public int getTotalSeats() { return totalSeats; }
    @PropertyName("RemainingSeats")
    public int getRemainingSeats() { return remainingSeats; }
    @PropertyName("VipSeats")
    public int getVipSeats() { return vipSeats; }

    // Setters
    public void setMovieId(String movieId) { this.movieId = movieId; }
    @PropertyName("MovieName")
    public void setMovieName(String movieName) { this.movieName = movieName; }
    @PropertyName("TimeStart")
    public void setTimeStart(String timeStart) { this.timeStart = timeStart; }
    @PropertyName("TimeEnd")
    public void setTimeEnd(String timeEnd) { this.timeEnd = timeEnd; }
    @PropertyName("MovieStatus")
    public void setMovieStatus(String movieStatus) { this.movieStatus = movieStatus; }
    @PropertyName("PosterUrl")
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    @PropertyName("Overview")
    public void setOverview(String overview) { this.overview = overview; }
    @PropertyName("Rating")
    public void setRating(String rating) { this.rating = rating; }
    @PropertyName("TotalSeats")
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    @PropertyName("RemainingSeats")
    public void setRemainingSeats(int remainingSeats) { this.remainingSeats = remainingSeats; }
    @PropertyName("VipSeats")
    public void setVipSeats(int vipSeats) { this.vipSeats = vipSeats; }

    // Parcelable implementation
    protected Moviedat(Parcel in) {
        movieId = in.readString();
        movieName = in.readString();
        timeStart = in.readString();
        timeEnd = in.readString();
        movieStatus = in.readString();
        posterUrl = in.readString();
        overview = in.readString();
        rating = in.readString();
        totalSeats = in.readInt();
        remainingSeats = in.readInt();
        vipSeats = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(movieName);
        dest.writeString(timeStart);
        dest.writeString(timeEnd);
        dest.writeString(movieStatus);
        dest.writeString(posterUrl);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeInt(totalSeats);
        dest.writeInt(remainingSeats);
        dest.writeInt(vipSeats);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Moviedat> CREATOR = new Creator<Moviedat>() {
        @Override
        public Moviedat createFromParcel(Parcel in) {
            return new Moviedat(in);
        }

        @Override
        public Moviedat[] newArray(int size) {
            return new Moviedat[size];
        }
    };
}

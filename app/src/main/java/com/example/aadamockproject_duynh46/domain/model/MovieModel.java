package com.example.aadamockproject_duynh46.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MovieModel implements Parcelable {
    @SerializedName("adult")
    private boolean isAdult;
    private String backdrop_path;
    
    private int id;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    private String title;
    @SerializedName("video")
    private boolean isVideo;
    @SerializedName("vote_average")
    private double voteAverage;
    private boolean isFav;
    public MovieModel() {}

    public MovieModel(boolean isAdult, String backdrop_path, int id, String overview, String posterPath, String releaseDate, String title, boolean isVideo, double voteAverage, boolean isFav) {
        this.isAdult = isAdult;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.isVideo = isVideo;
        this.voteAverage = voteAverage;
        this.isFav = isFav;
    }

    protected MovieModel(Parcel in) {
        isAdult = in.readByte() != 0;
        backdrop_path = in.readString();
        id = in.readInt();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        isVideo = in.readByte() != 0;
        voteAverage = in.readDouble();
        isFav = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeString(backdrop_path);
        dest.writeInt(id);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeDouble(voteAverage);
        dest.writeByte((byte) (isFav ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieModel)) return false;
        MovieModel movie = (MovieModel) o;
        return isAdult == movie.isAdult && id == movie.id && isVideo == movie.isVideo && Double.compare(voteAverage, movie.voteAverage) == 0 && isFav == movie.isFav && Objects.equals(backdrop_path, movie.backdrop_path) && Objects.equals(overview, movie.overview) && Objects.equals(posterPath, movie.posterPath) && Objects.equals(releaseDate, movie.releaseDate) && Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAdult, backdrop_path, id, overview, posterPath, releaseDate, title, isVideo, voteAverage, isFav);
    }
}

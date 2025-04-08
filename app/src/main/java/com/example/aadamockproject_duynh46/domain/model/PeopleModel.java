package com.example.aadamockproject_duynh46.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class PeopleModel {
    protected int id;
    protected String name;
    @SerializedName("profile_path")
    protected String profilePath;

    public PeopleModel(int id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }
    public PeopleModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeopleModel)) return false;
        PeopleModel that = (PeopleModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profilePath, that.profilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profilePath);
    }
}

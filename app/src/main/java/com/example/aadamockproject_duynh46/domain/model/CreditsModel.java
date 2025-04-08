package com.example.aadamockproject_duynh46.domain.model;

import java.util.ArrayList;

public class CreditsModel {
    private int id;
    private ArrayList<PeopleModel> cast;
    private ArrayList<PeopleModel> crew;

    public CreditsModel() {}

    public CreditsModel(int id, ArrayList<PeopleModel> cast, ArrayList<PeopleModel> crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<PeopleModel> getCast() {
        return cast;
    }

    public void setCast(ArrayList<PeopleModel> cast) {
        this.cast = cast;
    }

    public ArrayList<PeopleModel> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<PeopleModel> crew) {
        this.crew = crew;
    }
}

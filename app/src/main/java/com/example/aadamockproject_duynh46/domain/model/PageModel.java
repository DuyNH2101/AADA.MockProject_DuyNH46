package com.example.aadamockproject_duynh46.domain.model;

import java.util.ArrayList;

public class PageModel {
    private int page;
    private ArrayList<MovieModel> results;

    public PageModel() {}

    public PageModel(ArrayList<MovieModel> results, int page) {
        this.results = results;
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MovieModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieModel> results) {
        this.results = results;
    }
}

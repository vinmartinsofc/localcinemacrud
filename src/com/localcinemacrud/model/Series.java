package com.localcinemacrud.model;

import java.time.Year;

public class Series extends Title {
    private String creator;
    private boolean seasonEnded;


    public Series() {

    }


    public Series(String name, Year releaseDate, String category, String creator, boolean seasonEnded) {
        this.setName(name);
        this.setReleaseDate(releaseDate);
        this.setCategory(category);
        this.creator = creator;
        this.seasonEnded = seasonEnded;
    }

    public String getCreator() {
        return creator;
    }

    public boolean isSeasonEnded() {
        return seasonEnded;
    }
}



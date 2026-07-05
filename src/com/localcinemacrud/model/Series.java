package com.localcinemacrud.model;

import java.time.Year;

public class Series extends Title {
    private String creator;
    private boolean seriesEnded;


    public Series() {

    }

    public Series(String creator, boolean seasonEnded) {
        this.creator = creator;
        this.seriesEnded = seasonEnded;
    }


    public Series(String creator, boolean seriesEnded, String name, Year releaseDate, String category, String genre) {
        this.creator = creator;
        this.seriesEnded = seriesEnded;
        super.setName(name);
        super.setReleaseDate(releaseDate);
        super.setCategory(category);
        super.setGenre(genre);
    }
}



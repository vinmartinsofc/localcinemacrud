package com.localcinemacrud.model;


import java.time.Year;

public class Movie extends Title {
    public Movie() {
    }

    public Movie(String name, Year releaseDate, String director, int duration, String category, String genre) {
        super(name, releaseDate, director, duration, category, genre);
    }

}

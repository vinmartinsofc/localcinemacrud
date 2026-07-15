package com.localcinemacrud.model;


public class Movie extends Title {
    private String director;

    public Movie() {
    }

    public Movie(String director, int duration) {
        this.director = director;
        this.setDuration(duration);
    }

    public Movie(String name, int releaseDate, String category, String genre, String director, int duration) {
        super(name, releaseDate, category, genre);
        this.director = director;
        this.setDuration(duration);
    }

    public Movie(String name, int releaseDate, String director, int duration, String category, String genre) {
       this.setName(name);
       this.setReleaseDate(releaseDate);
       this.setDirector(director);
       this.setDuration(duration);
       this.setCategory(category);
       this.setGenre(genre);
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }


    @Override
    public String toString() {
        return String.format("[%d] Movie Name: %s (%d), Category: %s, Genre: %s, Director: %s, Duration: %d",
                getId(), getName(), getReleaseDate(), getCategory(), getGenre(), getDirector(), getDuration());
    }
}

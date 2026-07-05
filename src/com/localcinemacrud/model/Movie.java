package com.localcinemacrud.model;


public class Movie extends Title {
    private String director;
    private int duration;

    public Movie() {
    }

    public Movie(String director, int duration) {
        this.director = director;
        this.duration = duration;
    }

    public Movie(String name, int releaseDate, Category category, Genres genre, String director, int duration) {
        super(name, releaseDate, category, genre);
        this.director = director;
        this.duration = duration;
    }

    public Movie(String name, int releaseDate, String director, int duration, Category category, Genres genre) {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("Movie Name: %s (%d), Category: %s, Genre: %s, Director: %s, Duration: %d", getName(), getReleaseDate(), getCategory(), getGenre(), getDirector(), getDuration());
    }
}

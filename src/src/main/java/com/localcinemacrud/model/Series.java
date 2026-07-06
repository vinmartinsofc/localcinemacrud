package com.localcinemacrud.model;

public class Series extends Title {
    private  String creator;
    private  boolean newSeasons;


    public Series() {}

    public Series(String creator, boolean newSeasons) {
        this.creator = creator;
        this.newSeasons = newSeasons;
    }

    public Series(String name, int releaseDate, String category, String genre, int duration, String creator, boolean newSeasons) {
        super(name, releaseDate, category, genre, duration);
        this.creator = creator;
        this.newSeasons = newSeasons;
    }

    public Series(String name, int releaseDate, String category, String genre, String creator, boolean newSeasons) {
        super.setName(name);
        super.setReleaseDate(releaseDate);
        super.setCategory(category);
        super.setGenre(genre);
        this.creator = creator;
        this.newSeasons = newSeasons;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isNewSeasons() {
        return newSeasons;
    }

    public void setNewSeasons(boolean newSeasons) {
        this.newSeasons = newSeasons;
    }

    @Override
    public String toString() {
       String msg = "No";

       if (newSeasons) {
           msg = "Yes";
       }

        return String.format("Series Name: %s (%d), Category: %s, Genre: %s, Creator: %s, New Seasons: %s%n", getName(), getReleaseDate(), getCategory(), getGenre(), getCreator(), msg);
    }
}



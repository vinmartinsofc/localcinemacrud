package com.localcinemacrud.model;

public class Series extends Title {
    private  String creator;
    private  boolean newSeasons;


   public Series() {

   }


    public Series(String creator, boolean newSeasons) {
        this.creator = creator;
        this.newSeasons = newSeasons;
    }

    public Series(String name, int releaseDate, Category category, Genres genre, String creator, boolean newSeasons) {
        super(name, releaseDate, category, genre);
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
        return String.format("Series Name: %s (%d), Category: %s, Genre: %s, Creator: %s, New Seasons: %s%n", getName(), getReleaseDate(), getCategory(), getGenre(), getCreator(), isNewSeasons());
    }
}



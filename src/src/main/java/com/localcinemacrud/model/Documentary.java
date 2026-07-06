package com.localcinemacrud.model;

public class Documentary extends Title{
    private String creator;

    public Documentary(String creator) {
        this.creator = creator;
    }

    public Documentary(String name, int releaseDate, String category, String genre, String creator, int duration) {
        super(name, releaseDate, category, genre, duration);
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return String.format("[%d] Documentary Name: %s (%d), Category: %s, Genre: %s, Creator: %s, Duration: %d",
                getId(), getName(), getReleaseDate(), getCategory(), getGenre(), getCreator(), getDuration());
    }
}

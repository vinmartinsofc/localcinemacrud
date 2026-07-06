package com.localcinemacrud.model;

public class Title {
    private String name;
    private int releaseDate;
    private String category;
    private String genre;
    private int duration;

    public Title() {
    }

    public Title(String name, int releaseDate, String category, String genre, int duration) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.category = category;
        this.genre = genre;
        this.duration = duration;
    }

    public Title(String name, int releaseDate, String category, String genre) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.category = category;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Title{" +
                "name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", category='" + category + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
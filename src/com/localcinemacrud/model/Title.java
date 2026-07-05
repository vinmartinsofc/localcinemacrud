package com.localcinemacrud.model;

import java.security.SecureRandom;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class Title {
    private String name;
    private Year releaseDate;
    private String director;
    private int duration;
    private String category;
    private String genre;


    private final Map<Long, Title> moviesDB = new HashMap<>();
    private final Map<Long, Title> seriesDB = new HashMap<>();
    private Map<Long, Title> watchList = new HashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();


    public Title() {

    }

    public Title(String name, Year releaseDate, String director, int duration, String category, String genre) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.director = director;
        this.duration = duration;
        this.category = category;
        this.genre = genre;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Year getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Year releaseDate) {
        this.releaseDate = releaseDate;
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

    public void menu() {
        System.out.println("1 - Save Movie");
        System.out.println("2 - Save Series");
        System.out.println("3 - Get All Movies");
        System.out.println("4 - Get All Series");
        System.out.println("5 - Get Movie By Id");
        System.out.println("6 - Get Series By Id");
        System.out.println("7 - Update");
        System.out.println("8 - Delete");
        System.out.println("9 - Watchlist");
        System.out.println("10 - Watched");
        System.out.println("0 - Exit");

    }


    public void save(Title title) {
        long generatedKey = 1 + secureRandom.nextInt(1000);

        if (moviesDB.containsKey(generatedKey)) {
            System.out.println("Key already in use, try again");
            return;
        }

        if (title instanceof Series) {
            seriesDB.put(generatedKey, title);
        }

        moviesDB.put(generatedKey, title);

    }

    public void getAllMovies() {
        if (moviesDB.isEmpty()) {
            System.out.println("Storage is empty");
            return;
        }

        moviesDB.forEach((key, value) -> {
            String msg = key + ": " + value;
            System.out.println(msg);
        });
    }

    public void getAllSeries() {
        if (moviesDB.isEmpty()) {
            System.out.println("Storage is empty");
            return;
        }

        seriesDB.forEach((key, value) -> {
            String msg = key + ": " + value;
            System.out.println(msg);
        });
    }

    public void getMovieById(Long id) {
        if (!moviesDB.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

        System.out.println(moviesDB.get(id));
    }


    public void getSeriesById(Long id) {
        if (!moviesDB.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

        System.out.println(seriesDB.get(id));
    }

    public void update(Long id) {

    }

    public void delete(Long id) {
        if (!moviesDB.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

        moviesDB.remove(id);

    }

    @Override
    public String toString() {
        return "Title{" +
                "name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", director='" + director + '\'' +
                ", duration=" + duration +
                ", category='" + category + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

    public Map<Long, Title> getWatchList() {
        return watchList;
    }

    public void setWatchList(Map<Long, Title> watchList) {
        this.watchList = watchList;
    }
}

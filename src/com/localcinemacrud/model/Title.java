package com.localcinemacrud.model;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Title {
    private String name;
    private int releaseDate;
    private Category category;
    private Genres genre;


    private final Map<Integer, Title> storage = new HashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Genres getGenre() {
        return genre;
    }

    public void setGenre(Genres genre) {
        this.genre = genre;
    }

    public Title() {

    }

    public Title(String name, int releaseDate, Category category, Genres genre) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.category = category;
        this.genre = genre;
    }


    public void menu() {
        System.out.println("1 - Save Movie");
        System.out.println("2 - Save Series");
        System.out.println("3 - Get All");
        System.out.println("4 - Get By Id");
        System.out.println("5 - Update");
        System.out.println("6 - Delete");
        System.out.println("7 - Watchlist");
        System.out.println("8 - Watched");
        System.out.println("0 - Exit");

    }


    public void save(Title title) {
        int generatedKey = 1 + secureRandom.nextInt(1000);

        if (storage.containsKey(generatedKey)) {
            System.out.println("Key already in use, try again");
            return;
        }

        storage.put(generatedKey, title);

    }


    public void getAll() {
        if (storage.isEmpty()) {
            System.out.println("Storage is empty");
            return;
        }

        storage.forEach((key, value) -> {
            String msg = key + ": " + value;
            System.out.println(msg);
        });
    }


    public void getById(Integer id) {
        if (!storage.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

        System.out.println(storage.get(id));
    }


    public void update(Integer id) {
        if (!storage.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

    }

    public void delete(Integer id) {
        if (!storage.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

        storage.remove(id);

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

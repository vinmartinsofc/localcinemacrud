package com.localcinemacrud.model;

import com.localcinemacrud.service.Operations;

import java.security.SecureRandom;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class Title implements Operations {
    private String name;
    private Year releaseDate;
    private String director;
    private int duration;
    private String category;
    private Map<String, Title> storage = new HashMap<>();
    private int keyInt;
    private String keyString = "TK";
    private String nameCondition = "Movie";


    public Title() {

    }

    public Title(String name, Year releaseDate, String director, int duration, String category) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.director = director;
        this.duration = duration;
        this.category = category;

        SecureRandom secureRandom = new SecureRandom();
        keyInt = 1 + secureRandom.nextInt(1000);
    }

    public Title(String name, Year releaseDate, String category) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void menu() {
        System.out.println("1 - Save");
        System.out.println("2 - Get All");
        System.out.println("3 - Update");
        System.out.println("4 - Delete");
        System.out.println("5 - Watchlist");
        System.out.println("6 - Watched");
        System.out.println("0 - Exit");

    }

    @Override
    public void save(Title title) {

        if (title instanceof Series) {
            keyString = "TKS";
            nameCondition = "Series";
        }

        if (storage.containsKey(keyString + keyInt)) {
            System.out.println("Key already in use, try again");
            return;
        }

        storage.put(keyString + keyInt, title);

    }

    public void getAll() {
        if (storage.isEmpty()) {
            System.out.println("Storage is empty");
            return;
        }

        storage.forEach((key, title) -> {
            String msg = key + ": " + title;
            System.out.println(msg);
        });
    }

    @Override
    public void getById(String id) {
        if (!storage.containsKey(id)) {
            System.out.println("There's no such key");
        }

        System.out.println(storage.get(id));

    }


    @Override
    public void update(String id) {

    }

    @Override
    public void delete(String id) {
        if (!storage.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

        storage.remove(keyString + keyInt);

    }

    public Map<String, Title> getStorage() {
        return storage;
    }

    @Override
    public String toString() {
        return String.format("%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n",
                "** Title (" + nameCondition + ") **",
                "id: " + keyString + keyInt,
                "name: " + name,
                "release: " + releaseDate,
                "director: "+ director,
                "duration: " + duration,
                "category: " + category,
                "** ** ** ** **");
    }
}

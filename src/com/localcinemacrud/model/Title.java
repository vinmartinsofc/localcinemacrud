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
    Category category;
    private final Map<String, Title> storage = new HashMap<>();
    int titleKey;

    public Title() {
        SecureRandom secureRandom = new SecureRandom();
        titleKey = 1 + secureRandom.nextInt(1000);
    }

    public Title(String name, Year releaseDate, String director, int duration, Category category, int titleKey) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.director = director;
        this.duration = duration;
        this.category = category;
        this.titleKey = titleKey;
    }

    public void menu() {
        System.out.println("1 - save movie");
        System.out.println("2 - save series");
        System.out.println("3 - delete movie");
        System.out.println("4 - delete series");
        System.out.println("5 - update movie");
        System.out.println("6 - update series");
        System.out.println("7 - listAll movies");
        System.out.println("8 - listAll series");
        System.out.println("9 - Get movie By Id");
        System.out.println("10 - Get series By Id");

    }


    public String getName() {
        return name;
    }

    public Year getReleaseDate() {
        return releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public int getDuration() {
        return duration;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Title{" +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", director='" + director + '\'' +
                ", duration=" + duration +
                ", category=" + category +
                '}';
    }

    @Override
    public void save(Title title) {
        String generatedKey = "TK" + titleKey;
        storage.put(generatedKey, title);
    }

    @Override
    public void delete(String id) {
        if (!storage.containsKey(id)) {
            System.out.println("There's no such key");
        }

        storage.remove(id);

    }

    @Override
    public void update(String id) {

    }

    @Override
    public Map<String, Title> getAll() {
        return storage;
    }

    @Override
    public void getById(String id) {

        if (!storage.containsKey(id)) {
            System.out.println("There's no such key");
            return;
        }

        System.out.println(storage.get(id));


    }



}

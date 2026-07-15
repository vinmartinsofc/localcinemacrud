package com.localcinemacrud.service;

import com.localcinemacrud.model.Title;
import com.localcinemacrud.repository.TitleRepository;

import java.util.List;

public class TitleService {
    private final TitleRepository repository = new TitleRepository();

    public void menu() {
        System.out.println("1 - Save Movie");
        System.out.println("2 - Save Series");
        System.out.println("3 - Get All");
        System.out.println("4 - Get By Id");
        System.out.println("5 - Update");
        System.out.println("6 - Delete");
        System.out.println("7 - Search by Name");
        System.out.println("8 - Add Watched");
        System.out.println("9 - Save Documentary");
        System.out.println("10 - List Watched");
        System.out.println("11 - Delete Watched");
        System.out.println("0 - Exit");
    }

    public void save(Title title) {
        repository.save(title);
    }

    public void getAll() {
        List<Title> titles = repository.getAll();

        if (titles.isEmpty()) {
            System.out.println("Storage is empty");
            return;
        }

        titles.forEach(System.out::println);
    }

    public void getById(Integer id) {
        Title title = repository.getById(id);

        if (title != null) {
            System.out.println(title);
        }
    }

    public void update(Integer id) {
        Title title = repository.getById(id);

        if (title == null) {
            return;
        }

        // lógica de update vem depois
    }

    public void delete(Integer id) {
        repository.delete(id);
    }

    public void searchByName(String name) {
        List<Title> titles = repository.searchByName(name);

        if (titles.isEmpty()) {
            System.out.println("No titles found for: " + name);
            return;
        }

        titles.forEach(System.out::println);
    }

}
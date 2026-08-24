package com.localcinemacrud.service;

import com.localcinemacrud.model.WatchedTitle;
import com.localcinemacrud.repository.WatchedTitleRepository;

import java.time.LocalDate;
import java.util.List;

public class WatchedService {
    private final WatchedTitleRepository repository = new WatchedTitleRepository();

    public boolean save(int titleId, LocalDate date, double rating, String comment) {
        try {
            repository.save(new WatchedTitle(titleId, date, rating, comment));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void getAll() {
        List<WatchedTitle> watched = repository.getAll();

        if (watched.isEmpty()) {
            System.out.println("No watched titles yet");
            return;
        }

        watched.forEach(System.out::println);
    }

    public void delete(int id) {
        repository.delete(id);
    }

}
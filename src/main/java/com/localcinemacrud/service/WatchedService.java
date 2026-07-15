package com.localcinemacrud.service;

import com.localcinemacrud.model.WatchedTitle;
import com.localcinemacrud.repository.WatchedTitleRepository;

import java.time.LocalDate;
import java.util.List;

public class WatchedService {
    private final WatchedTitleRepository repository = new WatchedTitleRepository();

    public void save(int titleId, LocalDate date, double rating, String comment) {
        repository.save(new WatchedTitle(titleId, date, rating, comment));
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
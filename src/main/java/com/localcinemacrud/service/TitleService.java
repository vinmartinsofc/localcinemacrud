package com.localcinemacrud.service;

import com.localcinemacrud.model.Title;
import com.localcinemacrud.model.TmdbResult;
import com.localcinemacrud.repository.TitleRepository;

import java.util.List;

public class TitleService {
    private final TitleRepository repository = new TitleRepository();
    private final TmdbService tmdbService = new TmdbService();

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
        System.out.println("12 - Search Online (All)");
        System.out.println("13 - Search Online (Movies)");
        System.out.println("14 - Search Online (TV Series)");
        System.out.println("15 - Search Online (Documentaries)");
        System.out.println("16 - Import from TMDB");
        System.out.println("0 - Exit");
    }

    public boolean save(Title title) {
        int result = repository.save(title);
        return result != -1;
    }

    public void getAll() {
        List<Title> titles = repository.getAll();

        if (titles.isEmpty()) {
            System.out.println("Storage is empty");
            return;
        }

        titles.forEach(System.out::println);
    }
    public Title getById(Integer id) {
        Title title = repository.getById(id);

        if (title != null) {
            System.out.println(title);
        }

        return title;
    }


    public boolean update(Integer id, Title updatedTitle) {

        Title existing = repository.getById(id);

        if (existing == null) {
            System.out.println("Title with ID " + id + " not found!");
            return false;
        }

        updatedTitle.setId(id);

        repository.update(updatedTitle);
        return true;
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

    public List<TmdbResult> searchOnlineAll(String query) {
        return tmdbService.searchMulti(query);
    }

    public List<TmdbResult> searchOnlineMovies(String query) {
        return tmdbService.searchMovies(query);
    }

    public List<TmdbResult> searchOnlineTV(String query) {
        return tmdbService.searchTVShows(query);
    }

    public List<TmdbResult> searchOnlineDocumentaries(String query) {
        return tmdbService.searchDocumentaries(query);
    }

    public boolean importFromTmdb(TmdbResult tmdbResult) {
        Title title = tmdbService.convertToTitle(tmdbResult);
        return save(title);
    }


}
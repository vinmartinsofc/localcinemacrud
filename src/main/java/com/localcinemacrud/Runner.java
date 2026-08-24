package com.localcinemacrud;

import com.localcinemacrud.model.*;
import com.localcinemacrud.service.TitleService;
import com.localcinemacrud.service.WatchedService;
import com.localcinemacrud.service.TmdbService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        var setNewTitle = new TitleService();
        var watchedService = new WatchedService();
        var tmdbService = new TmdbService();
        int option = 0;

        try {

            do {
                try {
                    setNewTitle.menu();
                    option = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option. Please enter a number.");
                    continue;
                }

                try {
                    switch (option) {
                        case 0: {
                            System.out.print("Application closed.");
                        }

                        break;

                        case 1: {
                            System.out.print("Name: ");
                            String name = scanner.nextLine();

                            System.out.print("Release Date: ");
                            int releaseDate = Integer.parseInt(scanner.nextLine());

                            System.out.print("Director: ");
                            String director = scanner.nextLine();

                            System.out.print("Duration: ");
                            int duration = Integer.parseInt(scanner.nextLine());

                            System.out.print("Category: ");
                            String category = scanner.nextLine();

                            System.out.print("Genre: ");
                            String genre = scanner.nextLine();

                            boolean success = setNewTitle.save(new Movie(name, releaseDate, director, duration, category, genre));
                            if (!success) {
                                System.out.println("Failed to save movie.");
                            }
                        }

                        break;

                        case 2: {

                            System.out.print("Name: ");
                            String name = scanner.nextLine();

                            System.out.print("Release Date: ");
                            int releaseDate = Integer.parseInt(scanner.nextLine());

                            System.out.print("Category: ");
                            String category = scanner.nextLine();

                            System.out.print("Genre: ");
                            String genre = scanner.nextLine();

                            System.out.print("Creator: ");
                            String creator = scanner.nextLine();

                            System.out.print("Will be continued? (y/n) ");
                            String seriesEnded = scanner.nextLine();
                            boolean end = seriesEnded.equalsIgnoreCase("y");

                            boolean success = setNewTitle.save(new Series(name, releaseDate, category, genre, creator, end));
                            if (!success) {
                                System.out.println("Failed to save series.");
                            }

                        }

                        break;

                        case 3: {
                            setNewTitle.getAll();
                        }
                        break;

                        case 4: {
                            System.out.print("Enter the title id: ");
                            Integer id = Integer.parseInt(scanner.nextLine());
                            setNewTitle.getById(id);
                        }

                        break;

                        case 5: {
                            System.out.print("Enter the title id you want to update: ");
                            int id = Integer.parseInt(scanner.nextLine());

                            Title existing = setNewTitle.getById(id);
                            if (existing == null) {
                                System.out.println("Title not found!");
                                break;
                            }

                            System.out.println("\nEnter new data (press Enter to keep current value)");
                            System.out.println("Current: " + existing);
                            System.out.println();

                            System.out.print("Name (" + existing.getName() + "): ");
                            String name = scanner.nextLine();
                            if (name.isEmpty()) name = existing.getName();

                            System.out.print("Release Date (" + existing.getReleaseDate() + "): ");
                            String releaseStr = scanner.nextLine();
                            int releaseDate = releaseStr.isEmpty() ? existing.getReleaseDate() : Integer.parseInt(releaseStr);

                            System.out.print("Category (" + existing.getCategory() + "): ");
                            String category = scanner.nextLine();
                            if (category.isEmpty()) category = existing.getCategory();

                            System.out.print("Genre (" + existing.getGenre() + "): ");
                            String genre = scanner.nextLine();
                            if (genre.isEmpty()) genre = existing.getGenre();

                            Title updated = null;

                            if (existing instanceof Movie) {
                                Movie movie = (Movie) existing;
                                System.out.print("Director (" + movie.getDirector() + "): ");
                                String director = scanner.nextLine();
                                if (director.isEmpty()) director = movie.getDirector();

                                System.out.print("Duration (" + movie.getDuration() + "): ");
                                String durationStr = scanner.nextLine();
                                int duration = durationStr.isEmpty() ? movie.getDuration() : Integer.parseInt(durationStr);

                                updated = new Movie(name, releaseDate, category, genre, director, duration);

                            } else if (existing instanceof Series) {
                                Series series = (Series) existing;
                                System.out.print("Creator (" + series.getCreator() + "): ");
                                String creator = scanner.nextLine();
                                if (creator.isEmpty()) creator = series.getCreator();

                                System.out.print("New Seasons? (" + (series.isNewSeasons() ? "y" : "n") + "): ");
                                String newSeasonsStr = scanner.nextLine();
                                boolean newSeasons = newSeasonsStr.isEmpty() ? series.isNewSeasons() : newSeasonsStr.equalsIgnoreCase("y");

                                updated = new Series(name, releaseDate, category, genre, creator, newSeasons);

                            } else if (existing instanceof Documentary) {
                                Documentary doc = (Documentary) existing;
                                System.out.print("Creator (" + doc.getCreator() + "): ");
                                String creator = scanner.nextLine();
                                if (creator.isEmpty()) creator = doc.getCreator();

                                System.out.print("Duration (" + doc.getDuration() + "): ");
                                String durationStr = scanner.nextLine();
                                int duration = durationStr.isEmpty() ? doc.getDuration() : Integer.parseInt(durationStr);

                                updated = new Documentary(name, releaseDate, category, genre, creator, duration);
                            }

                            if (updated != null) {
                                boolean updateSuccess = setNewTitle.update(id, updated);
                                if (!updateSuccess) {
                                    System.out.println("Failed to update title.");
                                }
                            }
                        }
                        break;

                        case 6: {
                            System.out.print("Enter the title id you want to delete: ");
                            Integer id = Integer.parseInt(scanner.nextLine());
                            setNewTitle.delete(id);
                        }

                        break;

                        case 7: {
                            System.out.print("Search by name: ");
                            String query = scanner.nextLine();
                            setNewTitle.searchByName(query);

                        }
                        break;

                        case 8: {
                            System.out.print("Title id (watched): ");
                            int titleId = Integer.parseInt(scanner.nextLine());

                            System.out.print("Watched date (yyyy-MM-dd): ");
                            LocalDate date = LocalDate.parse(scanner.nextLine());

                            System.out.print("Rating (0-10): ");
                            double rating = Double.parseDouble(scanner.nextLine());

                            System.out.print("Comment: ");
                            String comment = scanner.nextLine();

                            boolean success = watchedService.save(titleId, date, rating, comment);
                            if (!success) {
                                System.out.println("Failed to save watched entry.");
                            }

                        }

                        break;

                        case 9: {
                            System.out.print("Name: ");
                            String name = scanner.nextLine();

                            System.out.print("Release: ");
                            int releaseDate = Integer.parseInt(scanner.nextLine());

                            System.out.print("Category: ");
                            String category = scanner.nextLine();

                            System.out.print("Genre: ");
                            String genre = scanner.nextLine();

                            System.out.print("Creator: ");
                            String creator = scanner.nextLine();

                            System.out.print("Duration: ");
                            int duration = Integer.parseInt(scanner.nextLine());

                            boolean success = setNewTitle.save(new Documentary(name, releaseDate, category, genre, creator, duration));
                            if (!success) {
                                System.out.println("Failed to save documentary.");
                            }

                        }

                        break;

                        case 10: {
                            watchedService.getAll();
                        }

                        break;

                        case 11: {
                            System.out.print("Watched entry id to delete: ");
                            int id = Integer.parseInt(scanner.nextLine());
                            watchedService.delete(id);
                        }

                        break;

                        case 12: {
                            System.out.print("Enter the name: ");
                            String query = scanner.nextLine();
                            List<TmdbResult> results = setNewTitle.searchOnlineAll(query);
                            tmdbService.displaySearchResults(results);
                        }
                        break;

                        case 13: {
                            System.out.print("Search for movies: ");
                            String query = scanner.nextLine();
                            List<TmdbResult> results = setNewTitle.searchOnlineMovies(query);
                            tmdbService.displaySearchResults(results);
                        }
                        break;

                        case 14: {
                            System.out.print("Search for TV series: ");
                            String query = scanner.nextLine();
                            List<TmdbResult> results = setNewTitle.searchOnlineTV(query);
                            tmdbService.displaySearchResults(results);
                        }
                        break;

                        case 15: {
                            System.out.print("Search for documentaries: ");
                            String query = scanner.nextLine();
                            List<TmdbResult> results = setNewTitle.searchOnlineDocumentaries(query);
                            tmdbService.displaySearchResults(results);
                        }
                        break;

                        case 16: {
                            System.out.print("Search query to import: ");
                            String query = scanner.nextLine();
                            List<TmdbResult> results = setNewTitle.searchOnlineAll(query);

                            if (results.isEmpty()) {
                                System.out.println("No results found to import.");
                                break;
                            }

                            tmdbService.displaySearchResults(results);

                            System.out.print("Enter the number of the result to import (0 to cancel): ");
                            int choice = Integer.parseInt(scanner.nextLine());

                            if (choice > 0 && choice <= results.size()) {
                                TmdbResult selected = results.get(choice - 1);

                                System.out.println("\nImporting: " + selected.getDisplayName());
                                System.out.println("Type: " + selected.getMediaType());
                                System.out.print("Confirm import? (y/n): ");
                                String confirm = scanner.nextLine();

                                if (confirm.equalsIgnoreCase("y")) {
                                    boolean success = setNewTitle.importFromTmdb(selected);
                                    if (success) {
                                        System.out.println("✓ Title imported successfully from TMDB!");
                                    } else {
                                        System.out.println("✗ Failed to import title.");
                                    }
                                } else {
                                    System.out.println("Import cancelled.");
                                }
                            } else {
                                System.out.println("Import cancelled.");
                            }
                        }
                        break;

                        default:
                            System.out.println("Invalid");

                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter valid numbers.");
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                }

            } while (option != 0);

        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());

        } finally {
            scanner.close();
        }
    }


}

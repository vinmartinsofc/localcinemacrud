package com.localcinemacrud;

import com.localcinemacrud.model.*;
import com.localcinemacrud.service.TitleService;
import com.localcinemacrud.service.WatchedService;

import java.time.LocalDate;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        var setNewTitle = new TitleService();
        var watchedService = new WatchedService();
        int option;

        try {

            do {
                setNewTitle.menu();
                option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 0: {
                        System.out.print("Leaving...");
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

                        setNewTitle.save(new Movie(name, releaseDate, director, duration, category, genre));
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


                        setNewTitle.save(new Series(name, releaseDate, category, genre, creator, end));

                    }

                    break;

                    case 3: {
                        setNewTitle.getAll();
                    }
                    break;

                    case 4: {
                        System.out.print("id? ");
                        Integer id = Integer.parseInt(scanner.nextLine());
                        setNewTitle.getById(id);
                    }

                    break;

                    case 5: {
                        System.out.print("id? ");
                        Integer id = Integer.parseInt(scanner.nextLine());
                        setNewTitle.update(id);

                    }

                    break;

                    case 6: {
                        System.out.print("id? ");
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
                        System.out.print("Title id (assistido): ");
                        int titleId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Watched date (yyyy-MM-dd): ");
                        LocalDate date = LocalDate.parse(scanner.nextLine());

                        System.out.print("Rating (0-10): ");
                        double rating = Double.parseDouble(scanner.nextLine());

                        System.out.print("Comment: ");
                        String comment = scanner.nextLine();

                        watchedService.save(titleId, date, rating, comment);

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

                        setNewTitle.save(new Documentary(name, releaseDate, category, genre, creator,duration));

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

                    default:
                        System.out.println("Invalid");

                }


            } while (option != 0);

        } catch (RuntimeException ex) {
            System.out.println("You entered an invalid input");
            System.out.println("(" + ex + ")");

        }

        scanner.close();
    }
}

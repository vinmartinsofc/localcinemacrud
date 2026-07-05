package com.localcinemacrud;

import com.localcinemacrud.model.*;

import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        var setNewTitle = new Title();
        int option;

        try {

            do {
                setNewTitle.menu();
                option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1: {
                        System.out.print("Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Release Date: ");
                        int releaseDate = Integer.parseInt(scanner.nextLine());

                        System.out.print("Director: ");
                        String director = scanner.nextLine();

                        System.out.print("Duration: ");
                        int duration = Integer.parseInt(scanner.nextLine());

                        Category[] categories = Category.values();

                        System.out.println("Category:");
                        for (int i = 0; i < categories.length; i++) {
                            System.out.println(i + " - " + categories[i]);
                        }

                        int index = Integer.parseInt(scanner.nextLine());
                        Category category = categories[index];

                        Genres[] genres = Genres.values();

                        System.out.println("Genres:");
                        for (int i = 0; i < genres.length; i++) {
                            System.out.println(i + " - " + genres[i]);
                        }

                        int genresIndex = Integer.parseInt(scanner.nextLine());
                        Genres genre = genres[genresIndex];


                        setNewTitle.save(new Movie(name, releaseDate, director, duration, category, genre));
                    }

                    break;

                    case 2: {

                        System.out.print("Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Release Date: ");
                        int releaseDate = Integer.parseInt(scanner.nextLine());

                        Category[] categories = Category.values();

                        System.out.println("Category:");
                        for (int i = 0; i < categories.length; i++) {
                            System.out.println(i + " - " + categories[i]);
                        }

                        int index = Integer.parseInt(scanner.nextLine());
                        Category category = categories[index];

                        Genres[] genres = Genres.values();

                        System.out.println("Genres:");
                        for (int i = 0; i < genres.length; i++) {
                            System.out.println(i + " - " + genres[i]);
                        }

                        int genresIndex = Integer.parseInt(scanner.nextLine());
                        Genres genre = genres[genresIndex];


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

                    }
                    break;

                    case 8: {


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

package com.localcinemacrud;

import com.localcinemacrud.model.Movie;
import com.localcinemacrud.model.Series;
import com.localcinemacrud.model.Title;

import java.time.Year;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        var setNewTitle = new Title();
        Movie movie = new Movie();
        Series series = new Series();

        setNewTitle.menu();
        int option = Integer.parseInt(scanner.nextLine());

        while (option != 0) {

            switch (option) {
                case 1: {

                    System.out.print("Movie or Series? ");
                    String internalOption = scanner.nextLine();

                    if (internalOption.equalsIgnoreCase("Series")) {
                        System.out.print("Creator: ");
                        String creator = scanner.nextLine();

                        System.out.print("Has it ended? ");
                        boolean isFinished = Boolean.parseBoolean(scanner.nextLine());

                        System.out.print("Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Release date: ");
                        Year releaseDate = Year.of(Integer.parseInt(scanner.nextLine()));

                        System.out.print("Category: ");
                        String category = scanner.nextLine();

                        series.save(new Series(name, releaseDate, category, creator,isFinished));

                        continue;

                    }

                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Release date: ");
                    Year releaseDate = Year.of(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Director: ");
                    String director = scanner.nextLine();

                    System.out.print("Duration: ");
                    int duration = Integer.parseInt(scanner.nextLine());

                    System.out.print("Category: ");
                    String category = scanner.nextLine();

                    movie.save(new Movie(name, releaseDate, director, duration, category));

                }

                case 2: {
                    setNewTitle.getAll();
                }



            }

            setNewTitle.menu();
            option = Integer.parseInt(scanner.nextLine());

        }








    }
}

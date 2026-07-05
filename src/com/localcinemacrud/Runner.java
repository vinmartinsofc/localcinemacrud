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
        int option;


        do  {
            setNewTitle.menu();
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1: {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Release Date: ");
                    Year year = Year.of(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Director: ");
                    String director = scanner.nextLine();

                    System.out.print("Duration: ");
                    int duration = Integer.parseInt(scanner.nextLine());

                    System.out.print("Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();

                    setNewTitle.save(new Movie(name, year, director, duration, category, genre));
                }

                break;

                case 2:{

                    System.out.print("Creator: ");
                    String creator = scanner.nextLine();

                    System.out.print("Ended? (y/n) ");
                    String seriesEnded = scanner.nextLine();
                    boolean end = seriesEnded.equalsIgnoreCase("y");

                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Release Date: ");
                    Year year = Year.of(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();

                    setNewTitle.save(new Series(creator, end, name,year, category, genre));
                }

                break;

                case 3: {
                    setNewTitle.getAllMovies();
                }
                break;

                case 4:{
                    setNewTitle.getAllSeries();
                }

                break;

                case 5: {
                    System.out.print("id? ");
                    Long id = Long.parseLong(scanner.nextLine());
                    setNewTitle.getMovieById(id);
                }

                break;

                case 6: {
                    System.out.print("id? ");
                    Long id = Long.parseLong(scanner.nextLine());
                    setNewTitle.getSeriesById(id);
                }


            }


        } while (option != 0);








    }
}

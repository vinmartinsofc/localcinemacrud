package com.localcinemacrud.Tests;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

public class DateTest {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        Year year = Year.of(1980);

        System.out.println(year);

    }
}

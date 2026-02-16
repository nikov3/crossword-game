package com.example.crossword.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UrlBuilder {

    private static final String BASE =
            "https://www.funland.bg/crosswords/crossword-%d-%s/";

    public static String build(int id, LocalDate date) {
        String formattedDate =
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return String.format(BASE, id, formattedDate);
    }
}

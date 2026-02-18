package com.example.crossword.data.repository;

import com.example.crossword.data.remote.CrosswordScraper;
import com.example.crossword.domain.model.CrosswordModel;

import java.io.IOException;

public class CrosswordRepository {

    private final CrosswordScraper scraper = new CrosswordScraper();

    public CrosswordModel getCrossword(String url) throws IOException {
        return scraper.fetch(url);
    }
}
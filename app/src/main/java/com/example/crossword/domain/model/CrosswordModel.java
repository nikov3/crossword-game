package com.example.crossword.domain.model;

import java.util.List;

public class CrosswordModel {

    private final String title;
    private final int width;
    private final int height;
    private final List<CellModel> cells;
    private final List<WordModel> words;

    public CrosswordModel(String title,
                          int width,
                          int height,
                          List<CellModel> cells,
                          List<WordModel> words) {

        this.title = title;
        this.width = width;
        this.height = height;
        this.cells = cells;
        this.words = words;
    }

    public String getTitle() { return title; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<CellModel> getCells() { return cells; }
    public List<WordModel> getWords() { return words; }
}
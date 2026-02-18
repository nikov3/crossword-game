package com.example.crossword.domain.model;

public class WordModel {

    private String type;
    private int id;
    private String clue;

    // ✅ Constructor you need
    public WordModel(int id, String clue) {
        this.type = "word";
        this.id = id;
        this.clue = clue;
    }

    public String getType() { return type; }
    public int getId() { return id; }
    public String getClue() { return clue; }
}

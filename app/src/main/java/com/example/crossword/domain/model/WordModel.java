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

//{
//type: 'word',
//id: 1406,
//clue: 'апарат за пренасяне на информация'
//        },


//type is used to differentiate the cell and the word/clue
//id is used in the cell class so it knows what clue should it display for this line or column
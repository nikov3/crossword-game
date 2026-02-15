package com.example.crossword.model;

public class CellModel {
    private char letter;
    private boolean isBlocked;
    private boolean isRevealed;

    public CellModel(char letter, boolean isBlocked) {
        this.letter = letter;
        this.isBlocked = isBlocked;
        this.isRevealed = false;
    }
}

//{
//type: 'cell',
//x: 3,
//y: 6,
//chr: 'a2e2d2e0a3e9470a6b444365ab00925f',
//hwid: 4504,
//vwid: 4702,
//svc: ''
//        }

//type -> used to differentiate the cell and the word/clue
// x -> x coordinate of the cell starts from 1
// y -> y coordinate of the cell starts from 1
//hwid -> horizontal word(clue) id, in the clue it is just id
//vwid ->same for vertical vwid
//chr -> hashed char, if we are going with the approach of 10 crosswords saved,
//  we don't need to add a decoding method
//  it needs to have a hashmap for every char and the value they are using i think,
//  not sure of the hashing/encripting method
// svc -> either '' or 'none', it has value none if there is no char in this square and it is a black square
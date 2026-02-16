package com.example.crossword.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "crosswordSolved")
public class CrosswordSolvedEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    public CrosswordSolvedEntity(String date) {
        this.date = date;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
}
package com.example.crossword.usecase;

import com.example.crossword.data.repository.CrosswordRepository;
import com.example.crossword.domain.model.CrosswordModel;
import com.example.crossword.utils.UrlBuilder;

import java.time.LocalDate;

public class GetDailyCrosswordUseCase {

    private final CrosswordRepository repository = new CrosswordRepository();

    public CrosswordModel execute(int id, LocalDate date) throws Exception {
        String url = UrlBuilder.build(id, date);
        return repository.getCrossword(url);
    }
}

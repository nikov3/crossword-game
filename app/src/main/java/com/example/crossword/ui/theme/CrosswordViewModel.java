package com.example.crossword.ui.theme;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.time.LocalDate;
import com.example.crossword.domain.model.CrosswordModel;
import com.example.crossword.usecase.GetDailyCrosswordUseCase;

public class CrosswordViewModel extends ViewModel {

    private MutableLiveData<CrosswordModel> crosswordLiveData =
            new MutableLiveData<>();

    public LiveData<CrosswordModel> getCrossword() {
        return crosswordLiveData;
    }

    public void load(int id, LocalDate date) {

        new Thread(() -> {
            try {
                GetDailyCrosswordUseCase useCase =
                        new GetDailyCrosswordUseCase();

                CrosswordModel crossword =
                        useCase.execute(id, date);



                crosswordLiveData.postValue(crossword);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

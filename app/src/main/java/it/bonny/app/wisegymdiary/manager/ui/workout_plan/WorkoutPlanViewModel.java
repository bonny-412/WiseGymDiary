package it.bonny.app.wisegymdiary.manager.ui.workout_plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkoutPlanViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WorkoutPlanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
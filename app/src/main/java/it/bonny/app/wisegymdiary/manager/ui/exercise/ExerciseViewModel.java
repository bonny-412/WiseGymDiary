package it.bonny.app.wisegymdiary.manager.ui.exercise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.database.ExerciseRepository;

public class ExerciseViewModel extends AndroidViewModel {

    private final ExerciseRepository exerciseRepository;
    private final LiveData<List<ExerciseBean>> exerciseList;

    public ExerciseViewModel(Application application) {
        super(application);

        exerciseRepository = new ExerciseRepository(application);
        exerciseList = exerciseRepository.getExerciseList();
    }

    public void insert(ExerciseBean exerciseBean) { exerciseRepository.insert(exerciseBean); }
    public void update(ExerciseBean exerciseBean) { exerciseRepository.update(exerciseBean); }
    public void delete(ExerciseBean exerciseBean) { exerciseRepository.delete(exerciseBean); }

    public LiveData<List<ExerciseBean>> getExerciseList() {
        return exerciseList;
    }

}
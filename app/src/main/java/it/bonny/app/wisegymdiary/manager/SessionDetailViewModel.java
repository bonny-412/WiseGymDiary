package it.bonny.app.wisegymdiary.manager;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.database.ExerciseRepository;
import it.bonny.app.wisegymdiary.database.WorkoutDayRepository;

public class SessionDetailViewModel extends AndroidViewModel {
    private final ExerciseRepository exerciseRepository;

    public SessionDetailViewModel(Application application) {
        super(application);

        WorkoutDayRepository workoutDayRepository = new WorkoutDayRepository(application);
        exerciseRepository = new ExerciseRepository(application);


    }

    public void insert(ExerciseBean exerciseBean) { exerciseRepository.insert(exerciseBean); }
    public void update(ExerciseBean exerciseBean) { exerciseRepository.update(exerciseBean); }
    public void delete(ExerciseBean exerciseBean) { exerciseRepository.delete(exerciseBean); }
    /*public LiveData<List<ExerciseBean>> getExerciseList(long idSession) {
        return exerciseRepository.getExerciseList(idSession);
    }*/

}

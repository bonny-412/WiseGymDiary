package it.bonny.app.wisegymdiary.manager;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.Session;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.database.ExerciseRepository;
import it.bonny.app.wisegymdiary.database.WorkoutDayRepository;

public class SessionDetailViewModel extends AndroidViewModel {
    private final ExerciseRepository exerciseRepository;

    public SessionDetailViewModel(Application application) {
        super(application);

        WorkoutDayRepository workoutDayRepository = new WorkoutDayRepository(application);
        exerciseRepository = new ExerciseRepository(application);


    }

    public void insert(Exercise exercise) { exerciseRepository.insert(exercise); }
    public void update(Exercise exercise) { exerciseRepository.update(exercise); }
    public void delete(Exercise exercise) { exerciseRepository.delete(exercise); }
    public LiveData<List<Exercise>> getExerciseList(long idSession) {
        return exerciseRepository.getExerciseList(idSession);
    }

}

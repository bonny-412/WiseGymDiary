package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.dao.ExerciseDAO;

public class ExerciseRepository {

    private final ExerciseDAO exerciseDAO;

    public ExerciseRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        exerciseDAO = appDatabase.exerciseDAO();
    }

    public LiveData<List<Exercise>> getExerciseList(long idWorkoutDay) {
        return exerciseDAO.getAllExercisesByIdWorkoutDay(idWorkoutDay);
    }

    public void insert(Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDAO.insert(exercise);
        });
    }

    public void update(Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDAO.update(exercise);
        });
    }

    public void delete(Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDAO.delete(exercise);
        });
    }

}

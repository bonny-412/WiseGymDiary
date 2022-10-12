package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.dao.ExerciseDAO;

public class ExerciseRepository {

    private final ExerciseDAO exerciseDAO;
    private final LiveData<List<ExerciseBean>> exerciseList;

    public ExerciseRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        exerciseDAO = appDatabase.exerciseDAO();

        exerciseList = exerciseDAO.findAllExercise();
    }

    public void insert(ExerciseBean exerciseBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> exerciseDAO.insert(exerciseBean));
    }

    public void update(ExerciseBean exerciseBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> exerciseDAO.update(exerciseBean));
    }

    public void delete(ExerciseBean exerciseBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> exerciseDAO.delete(exerciseBean));
    }

    public LiveData<List<ExerciseBean>> getExerciseList() {
        return exerciseList;
    }

}

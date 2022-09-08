package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;

public class WorkoutPlanRepository {
    private final WorkoutPlanDAO workoutPlanDAO;
    private final LiveData<WorkoutPlan> workoutPlan;

    public WorkoutPlanRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutPlanDAO = db.workoutPlanDAO();
        workoutPlan = workoutPlanDAO.loadWorkoutPlanOpen();
    }

    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlan;
    }

    public void insert(WorkoutPlan workoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutPlanDAO.insert(workoutPlan));
    }

    public void update(WorkoutPlan workoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutPlanDAO.update(workoutPlan));
    }

    public void delete(WorkoutPlan workoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutPlanDAO.delete(workoutPlan));
    }

}

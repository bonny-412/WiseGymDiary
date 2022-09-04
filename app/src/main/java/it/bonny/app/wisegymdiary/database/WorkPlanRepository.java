package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;

public class WorkPlanRepository {
    private WorkoutPlanDAO workoutPlanDAO;
    private LiveData<WorkoutPlan> workoutPlan;

    public WorkPlanRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutPlanDAO = db.workoutPlanDAO();
        workoutPlan = workoutPlanDAO.loadWorkoutPlanOpen();
    }

    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlan;
    }

    public void insert(WorkoutPlan workoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutPlanDAO.insert(workoutPlan);
        });
    }
}

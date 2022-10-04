package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;

public class WorkoutPlanRepository {
    private final WorkoutPlanDAO workoutPlanDAO;
    private final LiveData<WorkoutPlanBean> workoutPlan;

    public WorkoutPlanRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutPlanDAO = db.workoutPlanDAO();
        workoutPlan = workoutPlanDAO.loadWorkoutPlanOpen();
    }

    public LiveData<WorkoutPlanBean> getWorkoutPlan() {
        return workoutPlan;
    }

    public void insert(WorkoutPlanBean workoutPlanBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutPlanDAO.insert(workoutPlanBean));
    }

    public void update(WorkoutPlanBean workoutPlanBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutPlanDAO.update(workoutPlanBean));
    }

    public void delete(WorkoutPlanBean workoutPlanBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutPlanDAO.delete(workoutPlanBean));
    }

}

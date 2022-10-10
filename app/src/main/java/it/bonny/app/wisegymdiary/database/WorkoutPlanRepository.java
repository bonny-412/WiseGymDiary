package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;
import it.bonny.app.wisegymdiary.util.ValueFlagBean;

public class WorkoutPlanRepository {
    private final WorkoutPlanDAO workoutPlanDAO;
    private final LiveData<WorkoutPlanBean> workoutPlan;
    private final LiveData<List<WorkoutPlanBean>> workoutPlanNotEndList;
    private final LiveData<List<WorkoutPlanBean>> workoutPlanEndList;
    private final LiveData<Integer> countWorkoutPlanByIsEnd;

    public WorkoutPlanRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutPlanDAO = db.workoutPlanDAO();
        workoutPlan = workoutPlanDAO.loadWorkoutPlanOpen();

        workoutPlanNotEndList = workoutPlanDAO.findAllWorkoutPlanByIsEnd(ValueFlagBean.WORKOUT_PLAN_IS_NOT_END);
        workoutPlanEndList = workoutPlanDAO.findAllWorkoutPlanByIsEnd(ValueFlagBean.WORKOUT_PLAN_IS_END);
        countWorkoutPlanByIsEnd = workoutPlanDAO.countWorkoutPlanByIsEnd();
    }

    public LiveData<WorkoutPlanBean> getWorkoutPlan() {
        return workoutPlan;
    }

    public LiveData<List<WorkoutPlanBean>> getWorkoutPlanNotEndList() {
        return workoutPlanNotEndList;
    }

    public LiveData<List<WorkoutPlanBean>> getWorkoutPlanEndList() {
        return workoutPlanEndList;
    }

    public LiveData<Integer> getCountWorkoutPlanByIsEnd() {
        return countWorkoutPlanByIsEnd;
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

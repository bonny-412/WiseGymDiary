package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutBean;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;

public class WorkoutDayRepository {
    private final WorkoutDayDAO workoutDayDAO;
    private LiveData<List<WorkoutBean>> workoutDayList;
    private LiveData<WorkoutBean> sessionLiveData;

    public WorkoutDayRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDayDAO = db.workoutDayDAO();
    }

    public LiveData<List<WorkoutBean>> getWorkoutDayList(long idWorkoutPlan) {
        return workoutDayDAO.getAllWorkoutByIdWorkPlan(idWorkoutPlan);
    }

    public LiveData<WorkoutBean> getSessionLiveData(long idSession) {
        return workoutDayDAO.findWorkoutByPrimaryKeyLiveData(idSession);
    }

    public void insert(WorkoutBean workoutBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.insert(workoutBean);
        });
    }
    public void update(WorkoutBean workoutBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.update(workoutBean);
        });
    }
    public void delete(WorkoutBean workoutBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.delete(workoutBean);
        });
    }

}

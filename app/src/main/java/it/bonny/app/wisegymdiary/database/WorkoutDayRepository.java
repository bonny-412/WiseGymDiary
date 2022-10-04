package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.SessionBean;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;

public class WorkoutDayRepository {
    private final WorkoutDayDAO workoutDayDAO;
    private LiveData<List<SessionBean>> workoutDayList;
    private LiveData<SessionBean> sessionLiveData;

    public WorkoutDayRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDayDAO = db.workoutDayDAO();
    }

    public LiveData<List<SessionBean>> getWorkoutDayList(long idWorkoutPlan) {
        return workoutDayDAO.getAllRoutineByIdWorkPlan(idWorkoutPlan);
    }

    public LiveData<SessionBean> getSessionLiveData(long idSession) {
        return workoutDayDAO.findWorkoutDayByPrimaryKeyLiveData(idSession);
    }

    public void insert(SessionBean sessionBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.insert(sessionBean);
        });
    }
    public void update(SessionBean sessionBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.update(sessionBean);
        });
    }
    public void delete(SessionBean sessionBean) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.delete(sessionBean);
        });
    }

}

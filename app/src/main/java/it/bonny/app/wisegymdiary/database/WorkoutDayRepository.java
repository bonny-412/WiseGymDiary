package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Session;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;

public class WorkoutDayRepository {
    private final WorkoutDayDAO workoutDayDAO;
    private LiveData<List<Session>> workoutDayList;
    private LiveData<Session> sessionLiveData;

    public WorkoutDayRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDayDAO = db.workoutDayDAO();
    }

    public LiveData<List<Session>> getWorkoutDayList(long idWorkoutPlan) {
        return workoutDayDAO.getAllRoutineByIdWorkPlan(idWorkoutPlan);
    }

    public LiveData<Session> getSessionLiveData(long idSession) {
        return workoutDayDAO.findWorkoutDayByPrimaryKeyLiveData(idSession);
    }

    public void insert(Session session) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.insert(session);
        });
    }
    public void update(Session session) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.update(session);
        });
    }
    public void delete(Session session) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.delete(session);
        });
    }

}

package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;

public class WorkoutDayRepository {
    private final WorkoutDayDAO workoutDayDAO;
    private LiveData<List<WorkoutDay>> routineList;

    public WorkoutDayRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDayDAO = db.workoutDayDAO();
    }

    public LiveData<List<WorkoutDay>> getRoutineList(long idWorkPlan) {
        return workoutDayDAO.getAllRoutineByIdWorkPlan(idWorkPlan);
    }

    public void insert(WorkoutDay workoutDay) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.insert(workoutDay);
        });
    }
}

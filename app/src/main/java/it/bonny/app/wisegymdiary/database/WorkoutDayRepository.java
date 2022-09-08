package it.bonny.app.wisegymdiary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;

public class WorkoutDayRepository {
    private final WorkoutDayDAO workoutDayDAO;
    private LiveData<List<WorkoutDay>> workoutDayList;

    public WorkoutDayRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDayDAO = db.workoutDayDAO();
    }

    public LiveData<List<WorkoutDay>> getWorkoutDayList(long idWorkoutPlan) {
        return workoutDayDAO.getAllRoutineByIdWorkPlan(idWorkoutPlan);
    }

    public void insert(WorkoutDay workoutDay) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.insert(workoutDay);
        });
    }
    public void update(WorkoutDay workoutDay) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.update(workoutDay);
        });
    }
    public void delete(WorkoutDay workoutDay) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDayDAO.delete(workoutDay);
        });
    }

}

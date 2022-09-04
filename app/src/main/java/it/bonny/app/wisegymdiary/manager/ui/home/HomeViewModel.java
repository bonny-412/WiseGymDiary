package it.bonny.app.wisegymdiary.manager.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;
import it.bonny.app.wisegymdiary.database.AppDatabase;

public class HomeViewModel extends AndroidViewModel {

    private final WorkoutPlanDAO workoutPlanDAO;
    private final WorkoutDayDAO workoutDayDAO;

    public HomeViewModel(Application application) {
        super(application);
        workoutPlanDAO = AppDatabase.getInstance(application).workoutPlanDAO();
        workoutDayDAO = AppDatabase.getInstance(application).workoutDayDAO();
    }

    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlanDAO.loadWorkoutPlanOpen();
    }

    public LiveData<List<WorkoutDay>> getWorkoutDay(long idWorkoutPlan) {
        return workoutDayDAO.getAllRoutineByIdWorkPlan(idWorkoutPlan);
    }

    public void insert(WorkoutPlan workoutPlan) { workoutPlanDAO.insert(workoutPlan); }

    public void insert(WorkoutDay workoutDay) { workoutDayDAO.insert(workoutDay); }

}
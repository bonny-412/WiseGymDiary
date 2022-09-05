package it.bonny.app.wisegymdiary.manager.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.dao.ExerciseDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;
import it.bonny.app.wisegymdiary.database.AppDatabase;

public class HomeViewModel extends AndroidViewModel {

    private final WorkoutPlanDAO workoutPlanDAO;
    private final WorkoutDayDAO workoutDayDAO;
    private final ExerciseDAO exerciseDAO;


    public HomeViewModel(Application application) {
        super(application);
        workoutPlanDAO = AppDatabase.getInstance(application).workoutPlanDAO();
        workoutDayDAO = AppDatabase.getInstance(application).workoutDayDAO();
        exerciseDAO = AppDatabase.getInstance(application).exerciseDAO();
    }

    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlanDAO.loadWorkoutPlanOpen();
    }

    public LiveData<List<WorkoutDay>> getWorkoutDay(long idWorkoutPlan) {
        return workoutDayDAO.getAllRoutineByIdWorkPlan(idWorkoutPlan);
    }

    public LiveData<List<Exercise>> getExercise(long idWorkoutDay) {
        return exerciseDAO.getAllExercisesByIdWorkoutDay(idWorkoutDay);
    }

    public void insert(WorkoutPlan workoutPlan) { workoutPlanDAO.insert(workoutPlan); }

    public void insert(WorkoutDay workoutDay) { workoutDayDAO.insert(workoutDay); }

    public void insert(Exercise exercise) { exerciseDAO.insert(exercise); }

}
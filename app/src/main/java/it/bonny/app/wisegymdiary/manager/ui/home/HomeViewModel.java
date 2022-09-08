package it.bonny.app.wisegymdiary.manager.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.dao.ExerciseDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.database.ExerciseRepository;
import it.bonny.app.wisegymdiary.database.WorkoutDayRepository;
import it.bonny.app.wisegymdiary.database.WorkoutPlanRepository;

public class HomeViewModel extends AndroidViewModel {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final LiveData<WorkoutPlan> workoutPlanLiveData;

    private final WorkoutDayRepository workoutDayRepository;
    private final MutableLiveData<WorkoutDay> workoutDaySelected;

    private final ExerciseRepository exerciseRepository;


    public HomeViewModel(Application application) {
        super(application);

        workoutPlanRepository = new WorkoutPlanRepository(application);
        workoutPlanLiveData = workoutPlanRepository.getWorkoutPlan();

        workoutDayRepository = new WorkoutDayRepository(application);
        workoutDaySelected = new MutableLiveData<>();

        exerciseRepository = new ExerciseRepository(application);
    }

    public void insert(WorkoutPlan workoutPlan) { workoutPlanRepository.insert(workoutPlan); }
    public void update(WorkoutPlan workoutPlan) { workoutPlanRepository.update(workoutPlan); }
    public void delete(WorkoutPlan workoutPlan) { workoutPlanRepository.delete(workoutPlan); }
    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlanLiveData;
    }

    public void insert(WorkoutDay workoutDay) { workoutDayRepository.insert(workoutDay); }
    public void update(WorkoutDay workoutDay) { workoutDayRepository.update(workoutDay); }
    public void delete(WorkoutDay workoutDay) { workoutDayRepository.delete(workoutDay); }
    public LiveData<List<WorkoutDay>> getWorkoutDayList(long idWorkoutPlan) {
        return workoutDayRepository.getWorkoutDayList(idWorkoutPlan);
    }

    public MutableLiveData<WorkoutDay> setWorkoutDaySelected() {
        return workoutDaySelected;
    }
    public LiveData<WorkoutDay> getWorkoutDaySelected() {
        return workoutDaySelected;
    }

    public void insert(Exercise exercise) { exerciseRepository.insert(exercise); }
    public void update(Exercise exercise) { exerciseRepository.update(exercise); }
    public void delete(Exercise exercise) { exerciseRepository.delete(exercise); }
    public LiveData<List<Exercise>> getExerciseList(long idWorkoutDay) {
        return exerciseRepository.getExerciseList(idWorkoutDay);
    }

}
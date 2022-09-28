package it.bonny.app.wisegymdiary.manager.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Session;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.database.WorkoutDayRepository;
import it.bonny.app.wisegymdiary.database.WorkoutPlanRepository;

public class HomeViewModel extends AndroidViewModel {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final LiveData<WorkoutPlan> workoutPlanLiveData;

    private final WorkoutDayRepository workoutDayRepository;

    public HomeViewModel(Application application) {
        super(application);

        workoutPlanRepository = new WorkoutPlanRepository(application);
        workoutPlanLiveData = workoutPlanRepository.getWorkoutPlan();

        workoutDayRepository = new WorkoutDayRepository(application);
    }

    public void insert(WorkoutPlan workoutPlan) { workoutPlanRepository.insert(workoutPlan); }
    public void update(WorkoutPlan workoutPlan) { workoutPlanRepository.update(workoutPlan); }
    public void delete(WorkoutPlan workoutPlan) { workoutPlanRepository.delete(workoutPlan); }
    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlanLiveData;
    }

    public void insert(Session session) { workoutDayRepository.insert(session); }
    public void update(Session session) { workoutDayRepository.update(session); }
    public void delete(Session session) { workoutDayRepository.delete(session); }
    public LiveData<List<Session>> getWorkoutDayList(long idWorkoutPlan) {
        return workoutDayRepository.getWorkoutDayList(idWorkoutPlan);
    }

}
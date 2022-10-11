package it.bonny.app.wisegymdiary.manager.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutBean;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.database.WorkoutDayRepository;
import it.bonny.app.wisegymdiary.database.WorkoutPlanRepository;

public class HomeViewModel extends AndroidViewModel {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final LiveData<WorkoutPlanBean> workoutPlanLiveData;

    private final WorkoutDayRepository workoutDayRepository;

    public HomeViewModel(Application application) {
        super(application);

        workoutPlanRepository = new WorkoutPlanRepository(application);
        workoutPlanLiveData = workoutPlanRepository.getWorkoutPlan();

        workoutDayRepository = new WorkoutDayRepository(application);
    }

    public void insert(WorkoutPlanBean workoutPlanBean) { workoutPlanRepository.insert(workoutPlanBean); }
    public void update(WorkoutPlanBean workoutPlanBean) { workoutPlanRepository.update(workoutPlanBean); }
    public void delete(WorkoutPlanBean workoutPlanBean) { workoutPlanRepository.delete(workoutPlanBean); }
    public LiveData<WorkoutPlanBean> getWorkoutPlan() {
        return workoutPlanLiveData;
    }

    public void insert(WorkoutBean workoutBean) { workoutDayRepository.insert(workoutBean); }
    public void update(WorkoutBean workoutBean) { workoutDayRepository.update(workoutBean); }
    public void delete(WorkoutBean workoutBean) { workoutDayRepository.delete(workoutBean); }
    public LiveData<List<WorkoutBean>> getWorkoutDayList(long idWorkoutPlan) {
        return workoutDayRepository.getWorkoutDayList(idWorkoutPlan);
    }

}
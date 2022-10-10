package it.bonny.app.wisegymdiary.manager.ui.workout_plan;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.database.WorkoutPlanRepository;

public class WorkoutPlanViewModel extends AndroidViewModel {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final LiveData<List<WorkoutPlanBean>> workoutPlanNotEndList;
    private final LiveData<List<WorkoutPlanBean>> workoutPlanEndList;
    private final LiveData<Integer> countWorkoutPlanByIsEnd;

    public WorkoutPlanViewModel(Application application) {
        super(application);
        workoutPlanRepository = new WorkoutPlanRepository(application);

        workoutPlanNotEndList = workoutPlanRepository.getWorkoutPlanNotEndList();
        workoutPlanEndList = workoutPlanRepository.getWorkoutPlanEndList();
        countWorkoutPlanByIsEnd = workoutPlanRepository.getCountWorkoutPlanByIsEnd();
    }

    public void insert(WorkoutPlanBean workoutPlanBean) { workoutPlanRepository.insert(workoutPlanBean); }
    public void update(WorkoutPlanBean workoutPlanBean) { workoutPlanRepository.update(workoutPlanBean); }
    public void delete(WorkoutPlanBean workoutPlanBean) { workoutPlanRepository.delete(workoutPlanBean); }

    public LiveData<List<WorkoutPlanBean>> getWorkoutPlanNotEndList() {
        return workoutPlanNotEndList;
    }
    public LiveData<List<WorkoutPlanBean>> getWorkoutPlanEndList() {
        return workoutPlanEndList;
    }

    public LiveData<Integer> getCountWorkoutPlanByIsEnd() {
        return countWorkoutPlanByIsEnd;
    }

}
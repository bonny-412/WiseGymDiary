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
    private final MutableLiveData<Integer> countWorkoutPlanByIsEnd;

    public WorkoutPlanViewModel(Application application) {
        super(application);
        workoutPlanRepository = new WorkoutPlanRepository(application);
        countWorkoutPlanByIsEnd = new MutableLiveData<>();

        workoutPlanNotEndList = workoutPlanRepository.getWorkoutPlanNotEndList();
        workoutPlanEndList = workoutPlanRepository.getWorkoutPlanEndList();

        if(workoutPlanNotEndList != null && workoutPlanNotEndList.getValue() != null && workoutPlanNotEndList.getValue().size() > 0) {
            countWorkoutPlanByIsEnd.setValue(workoutPlanNotEndList.getValue().size());
        }else {
            countWorkoutPlanByIsEnd.setValue(0);
        }


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

    public MutableLiveData<Integer> getCountWorkoutPlanByIsEnd() {
        return countWorkoutPlanByIsEnd;
    }

}
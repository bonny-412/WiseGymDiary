package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import it.bonny.app.wisegymdiary.bean.WorkoutPlan;

@Dao
public interface WorkoutPlanDAO {

    @Insert
    long insert(WorkoutPlan workoutPlanBean);

    @Update
    void update(WorkoutPlan workoutPlanBean);

    @Delete
    void delete(WorkoutPlan workoutPlanBean);

    @Query("SELECT * FROM workout_plan WHERE isEnd = 0")
    LiveData<WorkoutPlan> loadWorkoutPlanOpen();

}


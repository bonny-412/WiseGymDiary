package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;

@Dao
public interface WorkoutPlanDAO {

    @Insert
    long insert(WorkoutPlanBean workoutPlanBean);

    @Update
    void update(WorkoutPlanBean workoutPlanBean);

    @Delete
    void delete(WorkoutPlanBean workoutPlanBean);

    @Query("SELECT * FROM workout_plan WHERE isEnd = 0 AND isSelected = 1")
    LiveData<WorkoutPlanBean> loadWorkoutPlanOpen();

    @Query("SELECT * FROM workout_plan WHERE id = :id")
    WorkoutPlanBean findWorkoutPlanByPrimaryKey(long id);

    @Query("SELECT COUNT(*) FROM workout_plan WHERE isSelected = :isSelected")
    int countWorkoutPlanIsSelected(int isSelected);

}


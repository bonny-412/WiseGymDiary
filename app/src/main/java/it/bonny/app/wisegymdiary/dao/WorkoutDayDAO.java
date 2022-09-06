package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutDay;

@Dao
public interface WorkoutDayDAO {

    @Insert
    Long insert(WorkoutDay workoutDay);

    @Update
    void update(WorkoutDay workoutDay);

    @Delete
    void delete(WorkoutDay workoutDay);

    @Query("SELECT * FROM workout_day WHERE id_work_plan = :idWorkPlan")
    LiveData<List<WorkoutDay>> getAllRoutineByIdWorkPlan(long idWorkPlan);

    @Query("SELECT * FROM workout_day WHERE id_work_plan = :idWorkPlan")
    List<WorkoutDay> getAllRoutineByIdWorkPlanNoLiveData(long idWorkPlan);

}


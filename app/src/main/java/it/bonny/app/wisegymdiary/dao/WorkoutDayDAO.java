package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutBean;

@Dao
public interface WorkoutDayDAO {

    @Insert
    Long insert(WorkoutBean workoutBean);

    @Update
    void update(WorkoutBean workoutBean);

    @Delete
    void delete(WorkoutBean workoutBean);

    @Query("SELECT * FROM workout WHERE id_work_plan = :idWorkPlan")
    LiveData<List<WorkoutBean>> getAllWorkoutByIdWorkPlan(long idWorkPlan);

    @Query("SELECT * FROM workout WHERE id_work_plan = :idWorkPlan")
    List<WorkoutBean> getAllWorkoutByIdWorkPlanNoLiveData(long idWorkPlan);

    @Query("SELECT * FROM workout WHERE id = :id")
    WorkoutBean findWorkoutByPrimaryKey(long id);

    @Query("SELECT * FROM  workout WHERE id = :id")
    LiveData<WorkoutBean> findWorkoutByPrimaryKeyLiveData(long id);

    @Query("SELECT COUNT(id) FROM workout WHERE id_work_plan = :idWorkoutPlan")
    int getCountWorkoutByIdWorkoutPlan(long idWorkoutPlan);

    @Query("SELECT * FROM  workout ORDER BY id ASC LIMIT 1")
    WorkoutBean getWorkoutByIdMin();

}


package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Session;

@Dao
public interface WorkoutDayDAO {

    @Insert
    Long insert(Session session);

    @Update
    void update(Session session);

    @Delete
    void delete(Session session);

    @Query("SELECT * FROM Session WHERE id_work_plan = :idWorkPlan")
    LiveData<List<Session>> getAllRoutineByIdWorkPlan(long idWorkPlan);

    @Query("SELECT * FROM Session WHERE id_work_plan = :idWorkPlan")
    List<Session> getAllRoutineByIdWorkPlanNoLiveData(long idWorkPlan);

    @Query("SELECT * FROM Session WHERE id = :id")
    Session findWorkoutDayByPrimaryKey(long id);

    @Query("SELECT * FROM  Session WHERE id = :id")
    LiveData<Session> findWorkoutDayByPrimaryKeyLiveData(long id);

    @Query("SELECT COUNT(id) FROM Session")
    int getCount();

    @Query("SELECT * FROM  Session ORDER BY id ASC LIMIT 1")
    Session getWorkoutDayByIdMin();

}


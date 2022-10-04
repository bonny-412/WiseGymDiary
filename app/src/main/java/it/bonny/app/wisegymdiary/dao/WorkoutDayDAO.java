package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.SessionBean;

@Dao
public interface WorkoutDayDAO {

    @Insert
    Long insert(SessionBean sessionBean);

    @Update
    void update(SessionBean sessionBean);

    @Delete
    void delete(SessionBean sessionBean);

    @Query("SELECT * FROM session WHERE id_work_plan = :idWorkPlan")
    LiveData<List<SessionBean>> getAllRoutineByIdWorkPlan(long idWorkPlan);

    @Query("SELECT * FROM session WHERE id_work_plan = :idWorkPlan")
    List<SessionBean> getAllRoutineByIdWorkPlanNoLiveData(long idWorkPlan);

    @Query("SELECT * FROM session WHERE id = :id")
    SessionBean findWorkoutDayByPrimaryKey(long id);

    @Query("SELECT * FROM  session WHERE id = :id")
    LiveData<SessionBean> findWorkoutDayByPrimaryKeyLiveData(long id);

    @Query("SELECT COUNT(id) FROM session")
    int getCount();

    @Query("SELECT * FROM  session ORDER BY id ASC LIMIT 1")
    SessionBean getWorkoutDayByIdMin();

}


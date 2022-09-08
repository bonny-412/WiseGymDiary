package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.MuscleBean;

@Dao
public interface MuscleDAO {

    @Insert
    void insert(MuscleBean muscleBean);

    @Update
    void update(MuscleBean muscleBean);

    @Delete
    void delete(MuscleBean muscleBean);

    @Query("SELECT * FROM muscle ORDER BY name_muscle ASC")
    List<MuscleBean> findAllMuscles();

}

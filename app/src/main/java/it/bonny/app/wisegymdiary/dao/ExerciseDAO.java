package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.ExerciseBean;

@Dao
public interface ExerciseDAO {

    @Insert
    Long insert(ExerciseBean exerciseBean);

    @Update
    void update(ExerciseBean exerciseBean);

    @Delete
    void delete(ExerciseBean exerciseBean);

    @Query("SELECT * FROM exercise WHERE id = :id")
    ExerciseBean findExerciseById(long id);

    @Query("SELECT * FROM exercise ORDER BY UPPER(TRIM(name)) ASC")
    LiveData<List<ExerciseBean>> findAllExercise();

}

package it.bonny.app.wisegymdiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Exercise;

@Dao
public interface ExerciseDAO {

    @Insert
    Long insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("SELECT * FROM exercise WHERE id_work_day = :idWorkoutDay")
    LiveData<List<Exercise>> getAllExercisesByIdWorkoutDay(long idWorkoutDay);

}

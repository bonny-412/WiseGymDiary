package it.bonny.app.wisegymdiary.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.CategoryExerciseBean;

@Dao
public interface CategoryExerciseDAO {
    @Insert
    void insert(CategoryExerciseBean categoryExerciseBean);

    @Update
    void update(CategoryExerciseBean categoryExerciseBean);

    @Delete
    void delete(CategoryExerciseBean categoryExerciseBean);

    @Query("SELECT * FROM category_exercise ORDER BY name_category_exercise ASC")
    List<CategoryExerciseBean> findAllCategoryExercise();

}

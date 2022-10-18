package it.bonny.app.wisegymdiary.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.CategoryMuscleBean;

@Dao
public interface CategoryMuscleDAO {

    @Insert
    void insert(CategoryMuscleBean categoryMuscleBean);

    @Update
    void update(CategoryMuscleBean categoryMuscleBean);

    @Delete
    void delete(CategoryMuscleBean categoryMuscleBean);

    @Query("SELECT * FROM category_muscle ORDER BY order_muscle, name_muscle ASC")
    List<CategoryMuscleBean> findAllMuscles();

    @Query("SELECT * FROM category_muscle WHERE name_muscle = :nameMuscle")
    CategoryMuscleBean findCategoryMuscleById(String nameMuscle);

}

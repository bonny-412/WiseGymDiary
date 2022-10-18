package it.bonny.app.wisegymdiary.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_exercise")
public class CategoryExerciseBean {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "name_category_exercise")
    private String name;

    public CategoryExerciseBean(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }

}

package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_exercise")
public class CategoryExerciseBean {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name_category_exercise")
    private String name;

    public CategoryExerciseBean(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}

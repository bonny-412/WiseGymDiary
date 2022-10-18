package it.bonny.app.wisegymdiary.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_muscle")
public class CategoryMuscleBean {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "name_muscle")
    private String name;

    @ColumnInfo(name = "order_muscle")
    private int order;

    public CategoryMuscleBean(@NonNull String name, int order) {
        this.name = name;
        this.order = order;
    }

    @NonNull
    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

}

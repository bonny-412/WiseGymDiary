package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_muscle")
public class CategoryMuscleBean {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name_muscle")
    private String name;

    @ColumnInfo(name = "order_muscle")
    private int order;

    public CategoryMuscleBean(long id, String name, int order) {
        this.id = id;
        this.name = name;
        this.order = order;
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

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

}

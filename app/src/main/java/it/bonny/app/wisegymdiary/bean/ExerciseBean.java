package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise")
public class ExerciseBean {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "id_category_muscle")
    private String idCategoryMuscle;

    @ColumnInfo(name = "id_category_exercise")
    private String idCategoryExercise;

    @ColumnInfo(name = "icon")
    private int icon;

    public ExerciseBean(long id, String name, String note, String idCategoryMuscle, String idCategoryExercise, int icon) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.idCategoryMuscle = idCategoryMuscle;
        this.idCategoryExercise = idCategoryExercise;
        this.icon = icon;
    }

    @Ignore
    public ExerciseBean() {}

    @Ignore
    public ExerciseBean(String name, String note, String idCategoryMuscle, String idCategoryExercise, int icon) {
        this.name = name;
        this.note = note;
        this.idCategoryMuscle = idCategoryMuscle;
        this.idCategoryExercise = idCategoryExercise;
        this.icon = icon;
    }

    @Ignore
    public ExerciseBean(String name, String idCategoryMuscle, String idCategoryExercise, int icon) {
        this.name = name;
        this.idCategoryMuscle = idCategoryMuscle;
        this.idCategoryExercise = idCategoryExercise;
        this.icon = icon;
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

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getIdCategoryMuscle() {
        return idCategoryMuscle;
    }
    public void setIdCategoryMuscle(String idCategoryMuscle) {
        this.idCategoryMuscle = idCategoryMuscle;
    }

    public String getIdCategoryExercise() {
        return idCategoryExercise;
    }
    public void setIdCategoryExercise(String idCategoryExercise) {
        this.idCategoryExercise = idCategoryExercise;
    }

    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }

}

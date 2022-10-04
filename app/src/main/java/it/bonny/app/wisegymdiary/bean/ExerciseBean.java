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
    private long idCategoryMuscle;

    @ColumnInfo(name = "id_category_exercise")
    private long idCategoryExercise;

    public ExerciseBean(long id, String name, String note, long idCategoryMuscle, long idCategoryExercise) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.idCategoryMuscle = idCategoryMuscle;
        this.idCategoryExercise = idCategoryExercise;
    }

    @Ignore
    public ExerciseBean() {}

    @Ignore
    public ExerciseBean(String name, String note, long idCategoryMuscle, long idCategoryExercise) {
        this.name = name;
        this.note = note;
        this.idCategoryMuscle = idCategoryMuscle;
        this.idCategoryExercise = idCategoryExercise;
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

    public long getIdCategoryMuscle() {
        return idCategoryMuscle;
    }
    public void setIdCategoryMuscle(long idCategoryMuscle) {
        this.idCategoryMuscle = idCategoryMuscle;
    }

    public long getIdCategoryExercise() {
        return idCategoryExercise;
    }
    public void setIdCategoryExercise(long idCategoryExercise) {
        this.idCategoryExercise = idCategoryExercise;
    }

}

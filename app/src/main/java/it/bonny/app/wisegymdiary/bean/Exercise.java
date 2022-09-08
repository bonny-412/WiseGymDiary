package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "id_work_day")
    private long idWorkDay;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "rest_time")
    private String restTime;

    @ColumnInfo(name = "num_sets_reps")
    private String numSetsReps;

    @ColumnInfo(name = "worked_muscle")
    private String workedMuscle;

    public Exercise(long id, String name, long idWorkDay, String note, String restTime, String numSetsReps, String workedMuscle) {
        this.id = id;
        this.name = name;
        this.idWorkDay = idWorkDay;
        this.note = note;
        this.restTime = restTime;
        this.numSetsReps = numSetsReps;
        this.workedMuscle = workedMuscle;
    }

    @Ignore
    public Exercise() {}

    @Ignore
    public Exercise(String name, long idWorkDay, String note, String restTime, String numSetsReps, String workedMuscle) {
        this.name = name;
        this.idWorkDay = idWorkDay;
        this.note = note;
        this.restTime = restTime;
        this.numSetsReps = numSetsReps;
        this.workedMuscle = workedMuscle;
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

    public long getIdWorkDay() {
        return idWorkDay;
    }
    public void setIdWorkDay(long idWorkDay) {
        this.idWorkDay = idWorkDay;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getRestTime() {
        return restTime;
    }
    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public String getNumSetsReps() {
        return numSetsReps;
    }
    public void setNumSetsReps(String numSetsReps) {
        this.numSetsReps = numSetsReps;
    }

    public String getWorkedMuscle() {
        return workedMuscle;
    }
    public void setWorkedMuscle(String workedMuscle) {
        this.workedMuscle = workedMuscle;
    }


}

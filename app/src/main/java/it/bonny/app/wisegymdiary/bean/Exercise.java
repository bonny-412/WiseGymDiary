package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
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

    @ColumnInfo(name = "weight")
    private String weight;

    @ColumnInfo(name = "done_date")
    private String doneDate;

    public Exercise() {}

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

    public String getDoneDate() {
        return doneDate;
    }
    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

}

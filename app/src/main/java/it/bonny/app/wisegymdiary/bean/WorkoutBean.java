package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "workout"
)
public class WorkoutBean {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "num_time_done")
    private Integer numTimeDone;

    @ColumnInfo(name = "id_work_plan")
    private long idWorkPlan;

    @ColumnInfo(name = "worked_muscle")
    private String workedMuscle;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "label_workout")
    private String label;

    public WorkoutBean() {}

    @Ignore
    public WorkoutBean(String name, long idWorkPlan) {
        this.name = name;
        this.idWorkPlan = idWorkPlan;
    }

    @Ignore
    public WorkoutBean(String name, Integer numTimeDone, long idWorkPlan, String workedMuscle, String note, String label) {
        this.name = name;
        this.numTimeDone = numTimeDone;
        this.idWorkPlan = idWorkPlan;
        this.workedMuscle = workedMuscle;
        this.note = note;
        this.label = label;
    }

    public WorkoutBean(long id, String name, Integer numTimeDone, long idWorkPlan, String workedMuscle, String note, String label) {
        this.id = id;
        this.name = name;
        this.numTimeDone = numTimeDone;
        this.idWorkPlan = idWorkPlan;
        this.workedMuscle = workedMuscle;
        this.note = note;
        this.label = label;
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

    public long getIdWorkPlan() {
        return idWorkPlan;
    }
    public void setIdWorkPlan(long idWorkPlan) {
        this.idWorkPlan = idWorkPlan;
    }

    public Integer getNumTimeDone() {
        return numTimeDone;
    }
    public void setNumTimeDone(Integer numTimeDone) {
        this.numTimeDone = numTimeDone;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getWorkedMuscle() {
        return workedMuscle;
    }
    public void setWorkedMuscle(String workedMuscle) {
        this.workedMuscle = workedMuscle;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    @Ignore
    public void copy(WorkoutBean obj) {
        setId(obj.getId());
        setName(obj.getName());
        setIdWorkPlan(obj.getIdWorkPlan());
        setNumTimeDone(obj.getNumTimeDone());
        setNote(obj.getNote());
        setWorkedMuscle(obj.getWorkedMuscle());
        setLabel(obj.getLabel());
    }

}


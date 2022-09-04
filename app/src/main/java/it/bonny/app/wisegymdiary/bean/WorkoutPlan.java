package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_plan")
public class WorkoutPlan {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "num_week")
    private int numWeek;
    @ColumnInfo(name = "startDate")
    private String startDate;
    @ColumnInfo(name = "endDate")
    private String endDate;
    @ColumnInfo(name = "note")
    private String note;
    @ColumnInfo(name = "isEnd", defaultValue = "0")
    private int isEnd;

    public WorkoutPlan() {}

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

    public int getNumWeek() {
        return numWeek;
    }
    public void setNumWeek(int numWeek) {
        this.numWeek = numWeek;
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public int getIsEnd() {
        return isEnd;
    }
    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    @Ignore
    public void copy(WorkoutPlan obj) {
        setId(obj.getId());
        setName(obj.getName());
        setNumWeek(obj.numWeek);
        setStartDate(obj.getStartDate());
        setEndDate(obj.getEndDate());
        setNote(obj.getNote());
        setIsEnd(obj.getIsEnd());
    }

}

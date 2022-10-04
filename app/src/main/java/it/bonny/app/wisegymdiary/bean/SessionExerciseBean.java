package it.bonny.app.wisegymdiary.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "session_exercise")
public class SessionExerciseBean {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "id_session")
    private long idSession;

    @ColumnInfo(name = "id_exercise")
    private long idExercise;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "rest_time")
    private String restTime;

    @ColumnInfo(name = "num_sets_reps")
    private String numSetsReps;

    public SessionExerciseBean(long id, long idSession, long idExercise, String note, String restTime, String numSetsReps) {
        this.id = id;
        this.idSession = idSession;
        this.idExercise = idExercise;
        this.note = note;
        this.restTime = restTime;
        this.numSetsReps = numSetsReps;
    }

    @Ignore
    public SessionExerciseBean() {}

    @Ignore
    public SessionExerciseBean(long idSession, long idExercise, String note, String restTime, String numSetsReps) {
        this.idSession = idSession;
        this.idExercise = idExercise;
        this.note = note;
        this.restTime = restTime;
        this.numSetsReps = numSetsReps;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getIdSession() {
        return idSession;
    }
    public void setIdSession(long idSession) {
        this.idSession = idSession;
    }

    public long getIdExercise() {
        return idExercise;
    }
    public void setIdExercise(long idExercise) {
        this.idExercise = idExercise;
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

}

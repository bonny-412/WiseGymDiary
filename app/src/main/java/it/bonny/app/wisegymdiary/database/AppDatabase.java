package it.bonny.app.wisegymdiary.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.dao.ExerciseDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;

@Database(entities = {WorkoutPlan.class, WorkoutDay.class, Exercise.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "gymdiary_db";
    private static AppDatabase instance;
    private static final Object LOCK = new Object();
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(callback)
                        .build();
            }
        }
        return instance;
    }

    public abstract WorkoutPlanDAO workoutPlanDAO();
    public abstract WorkoutDayDAO workoutDayDAO();
    public abstract ExerciseDAO exerciseDAO();

    private static final RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                WorkoutPlan workoutPlan = new WorkoutPlan();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();

                workoutPlan.setName("Workout 1");
                workoutPlan.setIsEnd(0);
                workoutPlan.setNumWeek(8);
                workoutPlan.setStartDate(simpleDateFormat.format(calendar.getTime()));
                calendar.add(Calendar.WEEK_OF_YEAR, 8);
                workoutPlan.setEndDate(simpleDateFormat.format(calendar.getTime()));

                instance.workoutPlanDAO().insert(workoutPlan);
            });
        }
    };

}


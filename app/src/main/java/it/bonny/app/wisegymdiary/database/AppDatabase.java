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

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.MuscleBean;
import it.bonny.app.wisegymdiary.bean.Session;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.dao.ExerciseDAO;
import it.bonny.app.wisegymdiary.dao.MuscleDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;
import it.bonny.app.wisegymdiary.util.App;

@Database(entities = {WorkoutPlan.class, Session.class, Exercise.class, MuscleBean.class}, exportSchema = false, version = 1)
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
    public abstract MuscleDAO muscleDAO();

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

                instance.muscleDAO().insert(new MuscleBean(1, App.getContext().getString(R.string.muscle_abductor)));
                instance.muscleDAO().insert(new MuscleBean(2, App.getContext().getString(R.string.muscle_abs)));
                instance.muscleDAO().insert(new MuscleBean(3, App.getContext().getString(R.string.muscle_adductors)));
                instance.muscleDAO().insert(new MuscleBean(4, App.getContext().getString(R.string.muscle_backs)));
                instance.muscleDAO().insert(new MuscleBean(5, App.getContext().getString(R.string.muscle_bicep)));
                instance.muscleDAO().insert(new MuscleBean(6, App.getContext().getString(R.string.muscle_calf)));
                instance.muscleDAO().insert(new MuscleBean(7, App.getContext().getString(R.string.muscle_chest)));
                instance.muscleDAO().insert(new MuscleBean(8, App.getContext().getString(R.string.muscle_femoral)));
                instance.muscleDAO().insert(new MuscleBean(9, App.getContext().getString(R.string.muscle_forearm)));
                instance.muscleDAO().insert(new MuscleBean(10, App.getContext().getString(R.string.muscle_glute)));
                instance.muscleDAO().insert(new MuscleBean(11, App.getContext().getString(R.string.muscle_quadricep)));
                instance.muscleDAO().insert(new MuscleBean(12, App.getContext().getString(R.string.muscle_shoulders)));
                instance.muscleDAO().insert(new MuscleBean(13, App.getContext().getString(R.string.muscle_triceps)));
                instance.muscleDAO().insert(new MuscleBean(14, App.getContext().getString(R.string.muscle_full_body)));
                instance.muscleDAO().insert(new MuscleBean(15, App.getContext().getString(R.string.muscle_upper)));
                instance.muscleDAO().insert(new MuscleBean(16, App.getContext().getString(R.string.muscle_lower)));

            });
        }
    };

}


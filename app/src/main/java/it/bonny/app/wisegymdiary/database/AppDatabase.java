package it.bonny.app.wisegymdiary.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.CategoryExerciseBean;
import it.bonny.app.wisegymdiary.bean.CategoryMuscleBean;
import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.bean.WorkoutBean;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.dao.CategoryExerciseDAO;
import it.bonny.app.wisegymdiary.dao.CategoryMuscleDAO;
import it.bonny.app.wisegymdiary.dao.ExerciseDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutDayDAO;
import it.bonny.app.wisegymdiary.dao.WorkoutPlanDAO;
import it.bonny.app.wisegymdiary.util.App;

@Database(entities = {WorkoutPlanBean.class, WorkoutBean.class, ExerciseBean.class, CategoryMuscleBean.class, CategoryExerciseBean.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "gym_diary_db";
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
    public abstract CategoryMuscleDAO categoryMuscleDAO();
    public abstract CategoryExerciseDAO categoryExerciseDAO();

    private static final RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                /*WorkoutPlanBean workoutPlanBean = new WorkoutPlanBean();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();

                workoutPlanBean.setName("Workout 1");
                workoutPlanBean.setIsEnd(0);
                workoutPlanBean.setNumWeek(8);
                workoutPlanBean.setStartDate(simpleDateFormat.format(calendar.getTime()));
                calendar.add(Calendar.WEEK_OF_YEAR, 8);
                workoutPlanBean.setEndDate(simpleDateFormat.format(calendar.getTime()));

                instance.workoutPlanDAO().insert(workoutPlanBean);*/

                //Create Category Muscle
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(1, App.getContext().getString(R.string.category_muscle_abs), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(2, App.getContext().getString(R.string.category_muscle_back), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(3, App.getContext().getString(R.string.category_muscle_biceps), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(4, App.getContext().getString(R.string.category_muscle_cardio), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(5, App.getContext().getString(R.string.category_muscle_chest), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(6, App.getContext().getString(R.string.category_muscle_leg), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(7, App.getContext().getString(R.string.category_muscle_others), 1));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(8, App.getContext().getString(R.string.category_muscle_shoulder), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean(9, App.getContext().getString(R.string.category_muscle_triceps), 0));

                //Create Category ExerciseBean
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean(1, App.getContext().getString(R.string.category_exercise_weight_reps)));
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean(2, App.getContext().getString(R.string.category_exercise_reps)));
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean(3, App.getContext().getString(R.string.category_exercise_distance)));
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean(4, App.getContext().getString(R.string.category_exercise_time)));

            });
        }
    };

}


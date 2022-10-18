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
                //Create Category Muscle
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_abs), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_back), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_biceps), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_cardio), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_chest), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_leg), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_others), 1));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_shoulder), 0));
                instance.categoryMuscleDAO().insert(new CategoryMuscleBean( App.getContext().getString(R.string.category_muscle_triceps), 0));

                //Create Category ExerciseBean
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean( App.getContext().getString(R.string.category_exercise_weight_reps)));
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean( App.getContext().getString(R.string.category_exercise_reps)));
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean( App.getContext().getString(R.string.category_exercise_distance)));
                instance.categoryExerciseDAO().insert(new CategoryExerciseBean( App.getContext().getString(R.string.category_exercise_time)));

                //Create Default Exercise
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_crunch), App.getContext().getString(R.string.category_muscle_abs), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_cable_crunch), App.getContext().getString(R.string.category_muscle_abs), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_planck), App.getContext().getString(R.string.category_muscle_abs), App.getContext().getString(R.string.category_exercise_weight_reps), 0));

                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_lat_machine), App.getContext().getString(R.string.category_muscle_back), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_pulley), App.getContext().getString(R.string.category_muscle_back), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_barbell_row), App.getContext().getString(R.string.category_muscle_back), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_pull_up), App.getContext().getString(R.string.category_muscle_back), App.getContext().getString(R.string.category_exercise_weight_reps), 0));

                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_barbell_curl), App.getContext().getString(R.string.category_muscle_biceps), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_dumbbell_curl), App.getContext().getString(R.string.category_muscle_biceps), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_dumbbell_hammer_curl), App.getContext().getString(R.string.category_muscle_biceps), App.getContext().getString(R.string.category_exercise_weight_reps), 0));

                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_walkin), App.getContext().getString(R.string.category_muscle_cardio), App.getContext().getString(R.string.category_exercise_time), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_elliptical_training), App.getContext().getString(R.string.category_muscle_cardio), App.getContext().getString(R.string.category_exercise_time), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_cyclette), App.getContext().getString(R.string.category_muscle_cardio), App.getContext().getString(R.string.category_exercise_time), 0));

                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_bench_press_flat), App.getContext().getString(R.string.category_muscle_chest), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_bench_press_flat_dumbbell), App.getContext().getString(R.string.category_muscle_chest), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_bench_press_incline), App.getContext().getString(R.string.category_muscle_chest), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_bench_press_incline_dumbbell), App.getContext().getString(R.string.category_muscle_chest), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_machine_chest_press), App.getContext().getString(R.string.category_muscle_chest), App.getContext().getString(R.string.category_exercise_weight_reps), 0));

                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_squat), App.getContext().getString(R.string.category_muscle_leg), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_leg_ext_machine), App.getContext().getString(R.string.category_muscle_leg), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_leg_press), App.getContext().getString(R.string.category_muscle_leg), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_deadlift), App.getContext().getString(R.string.category_muscle_leg), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_leg_curl), App.getContext().getString(R.string.category_muscle_leg), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_leg_curl_seated), App.getContext().getString(R.string.category_muscle_leg), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_seated_calf_raise_machine), App.getContext().getString(R.string.category_muscle_leg), App.getContext().getString(R.string.category_exercise_weight_reps), 0));

                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_military_press), App.getContext().getString(R.string.category_muscle_shoulder), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_arnold_dumbbell_press), App.getContext().getString(R.string.category_muscle_shoulder), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_face_pull), App.getContext().getString(R.string.category_muscle_shoulder), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_lateral_dumbbell_raise), App.getContext().getString(R.string.category_muscle_shoulder), App.getContext().getString(R.string.category_exercise_weight_reps), 0));

                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_kick_back), App.getContext().getString(R.string.category_muscle_triceps), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_dips), App.getContext().getString(R.string.category_muscle_triceps), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_v_bar_push_down), App.getContext().getString(R.string.category_muscle_triceps), App.getContext().getString(R.string.category_exercise_weight_reps), 0));
                instance.exerciseDAO().insert(new ExerciseBean(App.getContext().getString(R.string.name_exercise_rope_push_down), App.getContext().getString(R.string.category_muscle_triceps), App.getContext().getString(R.string.category_exercise_weight_reps), 0));

            });
        }
    };

}


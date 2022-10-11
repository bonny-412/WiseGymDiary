package it.bonny.app.wisegymdiary.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.Utility;

public class DetailWorkoutPlanActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private WorkoutPlanBean workoutPlanBean;
    private final Utility utility = new Utility();
    private ProgressBar progressBar;
    private ConstraintLayout containerSessions;
    private TextView textSessionsIsEmpty;
    private FloatingActionButton btnAddWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.bonny.app.wisegymdiary.R.layout.activity_detail_workout_plan);

        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);
        initElement();
        retrieveRoutine(idWorkoutPlan);

        btnAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void initElement() {
        textSessionsIsEmpty = findViewById(R.id.textSessionsIsEmpty);
        materialToolbar = findViewById(R.id.materialToolbar);
        progressBar = findViewById(R.id.progressBar);
        containerSessions = findViewById(R.id.containerSessions);
        btnAddWorkout = findViewById(R.id.btnAddWorkout);
        showHideProgressBar(true, true);
    }

    private void showHideProgressBar(boolean show, boolean sessionsIsEmpty) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            textSessionsIsEmpty.setVisibility(View.GONE);
            containerSessions.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            if(sessionsIsEmpty) {
                containerSessions.setVisibility(View.GONE);
                textSessionsIsEmpty.setVisibility(View.VISIBLE);
            }else {
                textSessionsIsEmpty.setVisibility(View.GONE);
                containerSessions.setVisibility(View.VISIBLE);
            }
        }
    }

    private void retrieveRoutine(long idWorkoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutPlanBean = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().findWorkoutPlanByPrimaryKey(idWorkoutPlan);
            int countSessions = AppDatabase.getInstance(getApplicationContext()).workoutDayDAO().getCountWorkoutByIdWorkoutPlan(idWorkoutPlan);
            runOnUiThread(() -> {
                materialToolbar.setTitle(workoutPlanBean.getName());

                showHideProgressBar(false, countSessions <= 0);
            });
        });
    }

}
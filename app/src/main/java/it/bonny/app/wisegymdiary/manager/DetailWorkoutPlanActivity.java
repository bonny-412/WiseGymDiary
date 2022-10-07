package it.bonny.app.wisegymdiary.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.Utility;

public class DetailWorkoutPlanActivity extends AppCompatActivity {

    private TextView subTitlePage, textSessionsIsEmpty;
    private MaterialButton btnReturnBack;
    private WorkoutPlanBean workoutPlanBean;
    private final Utility utility = new Utility();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.bonny.app.wisegymdiary.R.layout.activity_detail_workout_plan);

        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);

        initElement();

        retrieveRoutine(idWorkoutPlan);

    }

    private void initElement() {
        textSessionsIsEmpty = findViewById(R.id.textSessionsIsEmpty);
        subTitlePage = findViewById(R.id.subTitlePage);
        btnReturnBack = findViewById(R.id.btnReturnBack);
        progressBar = findViewById(R.id.progressBar);
        showHideProgressBar(true, true);
    }

    private void showHideProgressBar(boolean show, boolean sessionsIsEmpty) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            textSessionsIsEmpty.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            if(sessionsIsEmpty) {
                textSessionsIsEmpty.setVisibility(View.VISIBLE);
            }else {
                textSessionsIsEmpty.setVisibility(View.GONE);
            }
        }
    }

    private void retrieveRoutine(long idWorkoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutPlanBean = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().findWorkoutPlanByPrimaryKey(idWorkoutPlan);
            int countSessions = AppDatabase.getInstance(getApplicationContext()).workoutDayDAO().getCountSessionByWorkoutPlan(idWorkoutPlan);
            runOnUiThread(() -> {
                String subTitle = workoutPlanBean.getName() + " " + getString(R.string.from) + " "
                        + utility.convertStringDateToStringDateView(workoutPlanBean.getStartDate())
                        + " " + getString(R.string.to) + " "
                        + utility.convertStringDateToStringDateView(workoutPlanBean.getEndDate());
                subTitlePage.setText(subTitle);

                showHideProgressBar(false, countSessions <= 0);
            });
        });
    }

}
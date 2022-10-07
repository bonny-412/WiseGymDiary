package it.bonny.app.wisegymdiary.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.manager.ui.dashboard.DashboardFragment;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.util.ValueFlagBean;

public class NewEditWorkoutPlanActivity extends AppCompatActivity {

    private TextView titlePage, subTitlePage;
    private MaterialButton btnReturnBack, btnSave;
    private EditText name, startEndDate, note;
    private TextInputLayout nameLayout;
    private TextInputLayout startEndDateLayout;
    private WorkoutPlanBean workoutPlanBean;
    private ProgressBar progressBar;
    private NestedScrollView containerForm;
    private MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    private final Utility utility = new Utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_workout_plan);

        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);

        initElement();

        btnReturnBack.setOnClickListener(view -> finish());

        startEndDate.setOnClickListener(view -> {
            materialDatePicker.show(getSupportFragmentManager(), "Date Picker");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                Long startDate = selection.first;
                Long endDate = selection.second;
                Calendar myCal = Calendar.getInstance(Locale.getDefault());

                myCal.setTimeInMillis(startDate);
                String startDateStr = Utility.getDateFormView().format(myCal.getTime());
                workoutPlanBean.setStartDate(Utility.getDateForm().format(myCal.getTime()));

                myCal.setTimeInMillis(endDate);
                String endDateStr = Utility.getDateFormView().format(myCal.getTime());
                workoutPlanBean.setEndDate(Utility.getDateForm().format(myCal.getTime()));

                String startEndDateStr = startDateStr + " / " + endDateStr;
                startEndDate.setText(startEndDateStr);
            });
        });

        btnSave.setOnClickListener(view -> {
            boolean isError = false;

            if(name.getText() == null || "".equals(name.getText().toString().trim())) {
                isError = true;
                nameLayout.setError(getText(R.string.required_field));
            }else {
                nameLayout.setError(null);
            }

            if(workoutPlanBean.getStartDate() == null || "".equals(workoutPlanBean.getStartDate().trim()) ||
                    workoutPlanBean.getEndDate() == null || "".equals(workoutPlanBean.getEndDate().trim())) {
                isError = true;
                startEndDateLayout.setError(getText(R.string.required_field));
            }else
                startEndDateLayout.setError(null);

            if(!isError) {
                workoutPlanBean.setName(name.getText().toString());
                workoutPlanBean.setNote(note.getText().toString());
                workoutPlanBean.setIsEnd(ValueFlagBean.WORKOUT_PLAN_IS_NOT_END);

                workoutPlanBean.setNumWeek(utility.getCountWeeks(workoutPlanBean.getStartDate(), workoutPlanBean.getEndDate()));

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    int countWorkoutPlanIsSelected = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().countWorkoutPlanIsSelected(ValueFlagBean.WORKOUT_PLAN_IS_SELECTED);
                    if(countWorkoutPlanIsSelected == 0)
                        workoutPlanBean.setIsSelected(ValueFlagBean.WORKOUT_PLAN_IS_SELECTED);
                    else
                        workoutPlanBean.setIsSelected(ValueFlagBean.WORKOUT_PLAN_IS_NOT_SELECTED);
                    AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().insert(workoutPlanBean);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO: Add detail workout plan
                            finish();
                        }
                    });
                });
            }else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                btnSave.startAnimation(shake);
            }

        });

        if(idWorkoutPlan == 0) {
            titlePage.setText(getString(R.string.title_page_new_edit_workout_plan));
            subTitlePage.setText(getString(R.string.sub_title_page_new_edit_workout_plan));

            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText(getString(R.string.choose_start_end_date))
                    .setTheme(R.style.ThemeOverlay_App_DatePicker)
                    .setSelection(new Pair<>(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                    .build();
            workoutPlanBean = new WorkoutPlanBean();


            showHideProgressBar(false);
        }else {
            titlePage.setText(getString(R.string.title_page_new_edit_workout_plan_edit));
            subTitlePage.setText(getString(R.string.sub_title_page_new_edit_workout_plan_edit));

            retrieveRoutine(idWorkoutPlan);
        }

    }

    private void initElement() {
        titlePage = findViewById(R.id.titlePage);
        subTitlePage = findViewById(R.id.subTitlePage);
        btnReturnBack = findViewById(R.id.btnReturnBack);
        btnSave = findViewById(R.id.btnSave);
        name = findViewById(R.id.name);
        startEndDate = findViewById(R.id.startEndDate);
        note = findViewById(R.id.note);
        nameLayout = findViewById(R.id.nameLayout);
        startEndDateLayout = findViewById(R.id.startEndDateLayout);
        progressBar = findViewById(R.id.progressBar);
        containerForm = findViewById(R.id.containerForm);
        showHideProgressBar(true);
    }

    private void retrieveRoutine(long idWorkoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutPlanBean = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().findWorkoutPlanByPrimaryKey(idWorkoutPlan);
            runOnUiThread(() -> {
                name.setText(workoutPlanBean.getName());
                note.setText(workoutPlanBean.getNote());

                Calendar startDateCal = Calendar.getInstance(Locale.getDefault());
                Calendar endDateCal = Calendar.getInstance(Locale.getDefault());
                try {
                    Date startDate = Utility.getDateForm().parse(workoutPlanBean.getStartDate());
                    Date endDate = Utility.getDateForm().parse(workoutPlanBean.getEndDate());
                    if(startDate != null)
                        startDateCal.setTime(startDate);
                    if(endDate != null)
                        endDateCal.setTime(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String startEndDateStr = Utility.getDateFormView().format(startDateCal.getTime()) + " / "
                        + Utility.getDateFormView().format(endDateCal.getTime());
                startEndDate.setText(startEndDateStr);

                materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText(getString(R.string.choose_start_end_date))
                        .setTheme(R.style.ThemeOverlay_App_DatePicker)
                        .setSelection(new Pair<>(startDateCal.getTimeInMillis(), endDateCal.getTimeInMillis()))
                        .build();

                showHideProgressBar(false);
            });
        });
    }

    private void showHideProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            containerForm.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            containerForm.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
        }
    }

}
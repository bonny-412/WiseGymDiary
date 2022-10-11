package it.bonny.app.wisegymdiary.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
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
import it.bonny.app.wisegymdiary.util.Utility;

public class NewEditWorkoutPlanActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private TextView titlePage, subTitlePage;
    private EditText name, note;
    private TextView startEndDate;
    private TextInputLayout nameLayout, noteLayout;
    private WorkoutPlanBean workoutPlanBean;
    private ProgressBar progressBar;
    private MaterialButton btnAddWorkout;
    private MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    private final Utility utility = new Utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_workout_plan);

        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);

        initElement();

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

                String startEndDateStr = getString(R.string.from) + " "
                        + startDateStr + " "
                        + getString(R.string.to) + " "
                        + endDateStr;
                startEndDate.setText(startEndDateStr);
            });
        });

        /*btnSave.setOnClickListener(view -> {
            boolean isError = false;

            if (name.getText() == null || "".equals(name.getText().toString().trim())) {
                isError = true;
                nameLayout.setError(getText(R.string.required_field));
            } else {
                nameLayout.setError(null);
            }

            if (workoutPlanBean.getStartDate() == null || "".equals(workoutPlanBean.getStartDate().trim()) ||
                    workoutPlanBean.getEndDate() == null || "".equals(workoutPlanBean.getEndDate().trim())) {
                isError = true;
            }

            if (!isError) {
                workoutPlanBean.setName(name.getText().toString());
                workoutPlanBean.setNote(note.getText().toString());
                workoutPlanBean.setIsEnd(ValueFlagBean.WORKOUT_PLAN_IS_NOT_END);

                workoutPlanBean.setNumWeek(utility.getCountWeeks(workoutPlanBean.getStartDate(), workoutPlanBean.getEndDate()));

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    int countWorkoutPlanIsSelected = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().countWorkoutPlanIsSelected(ValueFlagBean.WORKOUT_PLAN_IS_SELECTED);
                    long idWorkoutPlanApp = 0;
                    if (workoutPlanBean.getId() <= 0) {
                        if (countWorkoutPlanIsSelected == 0)
                            workoutPlanBean.setIsSelected(ValueFlagBean.WORKOUT_PLAN_IS_SELECTED);
                        else
                            workoutPlanBean.setIsSelected(ValueFlagBean.WORKOUT_PLAN_IS_NOT_SELECTED);
                        idWorkoutPlanApp = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().insert(workoutPlanBean);
                    } else {
                        AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().update(workoutPlanBean);
                    }
                    long finalIdWorkoutPlanApp = idWorkoutPlanApp;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(idWorkoutPlan == 0) {
                                Intent intent = new Intent(getApplicationContext(), DetailWorkoutPlanActivity.class);
                                intent.putExtra("idWorkoutPlan", finalIdWorkoutPlanApp);
                                startActivity(intent);
                            }
                            finish();
                        }
                    });
                });
            } else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                btnSave.startAnimation(shake);
            }

        });*/

        if (idWorkoutPlan == 0) {
            titlePage.setText(getString(R.string.title_page_new_edit_workout_plan));
            subTitlePage.setText(getString(R.string.sub_title_page_new_edit_workout_plan));
            materialToolbar.setTitle(getString(R.string.title_page_new_edit_workout_plan));

            workoutPlanBean = new WorkoutPlanBean();

            Calendar myCal = Calendar.getInstance(Locale.getDefault());
            myCal.set(Calendar.DAY_OF_MONTH, myCal.getActualMinimum(Calendar.DAY_OF_MONTH));
            workoutPlanBean.setStartDate(Utility.getDateForm().format(myCal.getTime()));
            myCal.set(Calendar.DAY_OF_MONTH, myCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            workoutPlanBean.setEndDate(Utility.getDateForm().format(myCal.getTime()));

            countWorkoutPlan();
        } else {
            titlePage.setText(getString(R.string.title_page_new_edit_workout_plan_edit));
            subTitlePage.setText(getString(R.string.sub_title_page_new_edit_workout_plan_edit));
            materialToolbar.setTitle(getString(R.string.title_page_new_edit_workout_plan_edit));

            retrieveWorkoutPlan(idWorkoutPlan);
        }

    }


    private void initElement() {
        titlePage = findViewById(R.id.titlePage);
        subTitlePage = findViewById(R.id.subTitlePage);
        name = findViewById(R.id.name);
        startEndDate = findViewById(R.id.startEndDate);
        note = findViewById(R.id.note);
        nameLayout = findViewById(R.id.nameLayout);
        progressBar = findViewById(R.id.progressBar);
        materialToolbar = findViewById(R.id.materialToolbar);
        noteLayout = findViewById(R.id.noteLayout);
        btnAddWorkout = findViewById(R.id.btnAddWorkout);

        showHideProgressBar(true);
    }

    private void retrieveWorkoutPlan(long idWorkoutPlan) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutPlanBean = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().findWorkoutPlanByPrimaryKey(idWorkoutPlan);
            runOnUiThread(() -> {
                name.setText(workoutPlanBean.getName());
                note.setText(workoutPlanBean.getNote());
                getTimeInMillisCustom();
                showHideProgressBar(false);
            });
        });
    }

    private void countWorkoutPlan() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int count = AppDatabase.getInstance(getApplicationContext()).workoutPlanDAO().countWorkoutPlanNotEnd();
            runOnUiThread(() -> {
                String nameApp = getString(R.string.day);
                if(count == 0)
                    nameApp += " " + 1;
                else
                    nameApp += " " + count;
                name.setText(nameApp);
                getTimeInMillisCustom();
                showHideProgressBar(false);
            });
        });
    }

    private void showHideProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.GONE);
            noteLayout.setVisibility(View.GONE);
            startEndDate.setVisibility(View.GONE);
            btnAddWorkout.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            nameLayout.setVisibility(View.VISIBLE);
            noteLayout.setVisibility(View.VISIBLE);
            startEndDate.setVisibility(View.VISIBLE);
            btnAddWorkout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getTimeInMillisCustom() {
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

        String startEndDateStr = getString(R.string.from) + " "
                + Utility.getDateFormView().format(startDateCal.getTime()) + " "
                + getString(R.string.to) + " "
                + Utility.getDateFormView().format(endDateCal.getTime());
        startEndDate.setText(startEndDateStr);

        materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.choose_start_end_date))
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .setSelection(new Pair<>(startDateCal.getTimeInMillis(), endDateCal.getTimeInMillis()))
                .build();
    }

}
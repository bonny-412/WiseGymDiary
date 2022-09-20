package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.bonny.app.wisegymdiary.bean.MuscleBean;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.component.ChipMuscleAdapter;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.App;
import it.bonny.app.wisegymdiary.util.Utility;

public class NewEditWorkoutDay extends AppCompatActivity {

    private MaterialButton btnSave;
    private TextInputLayout nameWorkoutDay, noteWorkoutDay;
    private WorkoutDay workoutDay;
    private MaterialButton btnReturn;
    private ProgressBar progressBar, progressBar1;
    private final List<String> chipsSelectedList = new ArrayList<>();
    private ChipGroup chipGroup;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_workout_day);

        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);
        long idWorkoutDay = getIntent().getLongExtra("idWorkoutDay", 0);

        initElements();

        if(idWorkoutDay == 0) {
            workoutDay = new WorkoutDay();
            progressBar1.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            populateChipMuscle();
        }else {
            progressBar1.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);

            retrieveRoutine(idWorkoutDay);
        }

        btnReturn.setOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            boolean isError = false;
            if(nameWorkoutDay.getEditText() == null || "".equals(nameWorkoutDay.getEditText().getText().toString())) {
                isError = true;
                nameWorkoutDay.setError(getText(R.string.required_field));
            }else {
                nameWorkoutDay.setError(null);
                workoutDay.setName(nameWorkoutDay.getEditText().getText().toString());
            }

            workoutDay.setNumTimeDone(0);
            workoutDay.setIdWorkPlan(idWorkoutPlan);
            workoutDay.setNote(noteWorkoutDay.getEditText() != null ? noteWorkoutDay.getEditText().getText().toString(): "");

            TextView titleChooseIconNewAccount = findViewById(R.id.titleChooseIconNewAccount);
            if(chipsSelectedList == null || chipsSelectedList.size() == 0) {
                isError = true;
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
            }else {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
                StringBuilder app = new StringBuilder();
                String[] strings = new String[chipsSelectedList.size()];
                chipsSelectedList.toArray(strings);
                Arrays.sort(strings);
                for(int i = 0; i < strings.length; i ++) {
                    app.append(strings[i]);
                    if(i < strings.length - 1)
                        app.append(", ");
                }
                workoutDay.setWorkedMuscle(app.toString());
            }

            if(!isError) {
                Intent intent = new Intent();
                intent.putExtra("page", Utility.ADD_WORKOUT_DAY);
                if(workoutDay.getId() > 0) {
                    intent.putExtra(Utility.EXTRA_WORKOUT_DAY_ID, workoutDay.getId());
                }
                intent.putExtra(Utility.EXTRA_WORKOUT_DAY_NAME, workoutDay.getName());
                intent.putExtra(Utility.EXTRA_WORKOUT_DAY_NUM_TIME_DONE, workoutDay.getNumTimeDone());
                intent.putExtra(Utility.EXTRA_WORKOUT_DAY_ID_WORK_PLAIN, workoutDay.getIdWorkPlan());
                intent.putExtra(Utility.EXTRA_WORKOUT_DAY_WORKED_MUSCLE, workoutDay.getWorkedMuscle());
                intent.putExtra(Utility.EXTRA_WORKOUT_DAY_NOTE, workoutDay.getNote());
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                btnSave.startAnimation(shake);
            }
        });

    }

    private void initElements() {
        btnReturn = findViewById(R.id.btnReturn);
        btnSave = findViewById(R.id.btnSave);
        nameWorkoutDay = findViewById(R.id.nameWorkoutDay);
        noteWorkoutDay = findViewById(R.id.noteWorkoutDay);
        chipGroup = findViewById(R.id.chipGroup);
        TextInputEditText textInputNameWorkoutDay = findViewById(R.id.textInputNameWorkoutDay);
        TextInputEditText textInputNoteWorkoutDay = findViewById(R.id.textInputNoteWorkoutDay);
        textInputNameWorkoutDay.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textInputNoteWorkoutDay.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar1);
        scrollView = findViewById(R.id.scrollView);
    }

    private void populateChipMuscle() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<MuscleBean> muscleBeanList = AppDatabase.getInstance(getApplicationContext()).muscleDAO().findAllMuscles();

            runOnUiThread(() -> {
                for(MuscleBean muscleBean: muscleBeanList) {
                    addChip(muscleBean.getName());
                }
                progressBar.setVisibility(View.GONE);
                chipGroup.setVisibility(View.VISIBLE);
                chipGroup.setSelectionRequired(true);
            });
        });
    }

    private void addChip(String nameChip) {
        Chip chip = new Chip(this);
        chip.setText(nameChip);

        if(workoutDay.getWorkedMuscle() != null && workoutDay.getWorkedMuscle().contains(nameChip)) {
            chip.setChipBackgroundColor(getColorStateList(R.color.blue_text));
            chip.setTextColor(getColor(R.color.blue_background));
            chip.setChecked(true);
            chipsSelectedList.add(chip.getText().toString());
        }else {
            chip.setChipBackgroundColor(getColorStateList(R.color.blue_background));
            chip.setTextColor(getColor(R.color.blue_text));
            chip.setChecked(false);
        }

        chip.setOnClickListener(view -> {
            if(!chipsSelectedList.contains(chip.getText().toString())) {
                chip.setChecked(true);
                chip.setChipBackgroundColor(getColorStateList(R.color.blue_text));
                chip.setTextColor(getColor(R.color.blue_background));
                chipsSelectedList.add(chip.getText().toString());
            }else {
                chip.setChecked(false);
                chip.setChipBackgroundColor(getColorStateList(R.color.blue_background));
                chip.setTextColor(getColor(R.color.blue_text));
                chipsSelectedList.remove(chip.getText().toString());
            }
        });

        chipGroup.addView(chip);
    }

    private void retrieveRoutine(long idWorkoutDay) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutDay = AppDatabase.getInstance(getApplicationContext()).workoutDayDAO().getWorkoutDayById(idWorkoutDay);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       if(nameWorkoutDay.getEditText() != null)
                           nameWorkoutDay.getEditText().setText(workoutDay.getName());
                       if(noteWorkoutDay.getEditText() != null)
                           noteWorkoutDay.getEditText().setText(workoutDay.getNote());

                        populateChipMuscle();
                        progressBar1.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        btnSave.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

}
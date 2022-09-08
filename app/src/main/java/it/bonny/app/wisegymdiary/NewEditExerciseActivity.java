package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.MuscleBean;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.Utility;

public class NewEditExerciseActivity extends AppCompatActivity {

    private MaterialButton btnReturn, btnSave, btnAddNewSetsReps;
    private TextInputLayout nameExercise, noteExercise;
    private String workedMuscleSelected;
    private Exercise exercise;
    private ChipGroup chipGroup;
    private LinearLayout linearLayout;
    private EditText editTextSets, editTextReps;
    private NumberPicker numPickerMin, numPickerSec;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_exercise);

        boolean newFlag = getIntent().getBooleanExtra("newFlag", false);
        long idWorkoutDay = getIntent().getLongExtra("idWorkoutDay", 0);

        if(newFlag) {
            workedMuscleSelected = "";
            exercise = new Exercise();
        }

        initElements();

        btnReturn.setOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            if(nameExercise.getEditText() != null) {
                boolean isError = false;

                if(nameExercise.getEditText() == null || "".equals(nameExercise.getEditText().getText().toString())) {
                    isError = true;
                    nameExercise.setError(getText(R.string.required_field));
                }else {
                    exercise.setName(nameExercise.getEditText().getText().toString());
                    nameExercise.setError(null);
                }

                TextView titleChooseIconMuscle = findViewById(R.id.titleChooseIconMuscle);
                if("".equals(workedMuscleSelected)) {
                    isError = true;
                    titleChooseIconMuscle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
                }else {
                    titleChooseIconMuscle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
                    exercise.setWorkedMuscle(workedMuscleSelected);
                }

                TextView titleSetsReps = findViewById(R.id.titleSetsReps);
                String totSetsReps = readAllSetsReps();
                if("".equals(totSetsReps)) {
                    isError = true;
                    titleSetsReps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
                }else {
                    titleSetsReps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
                    exercise.setNumSetsReps(totSetsReps);
                }

                exercise.setIdWorkDay(idWorkoutDay);
                exercise.setNote(noteExercise.getEditText() != null ? noteExercise.getEditText().getText().toString(): "");
                String timeRest = numPickerMin.getValue() + Utility.SYMBOL_SPLIT + numPickerSec.getValue();
                exercise.setRestTime(timeRest);
                if(!isError) {
                    Intent intent = new Intent();
                    intent.putExtra("page", Utility.ADD_EXERCISE);
                    intent.putExtra(Utility.EXTRA_EXERCISE_NAME, exercise.getName());
                    intent.putExtra(Utility.EXTRA_EXERCISE_ID_WORK_DAY, exercise.getIdWorkDay());
                    intent.putExtra(Utility.EXTRA_EXERCISE_NOTE, exercise.getNote());
                    intent.putExtra(Utility.EXTRA_EXERCISE_REST_TIME, exercise.getRestTime());
                    intent.putExtra(Utility.EXTRA_EXERCISE_NUM_SETS_REPS, exercise.getNumSetsReps());
                    intent.putExtra(Utility.EXTRA_EXERCISE_WORKED_MUSCLE, exercise.getWorkedMuscle());
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    btnSave.startAnimation(shake);
                }
            }
        });

        btnAddNewSetsReps.setOnClickListener(v -> addView());

        populateChipMuscle();

    }

    private void initElements() {
        btnReturn = findViewById(R.id.btnReturn);
        btnSave = findViewById(R.id.btnSave);
        nameExercise = findViewById(R.id.nameExercise);
        noteExercise = findViewById(R.id.noteExercise);
        chipGroup = findViewById(R.id.chipGroup);
        btnAddNewSetsReps = findViewById(R.id.btnAddNewSetsReps);
        linearLayout = findViewById(R.id.layout_list);
        editTextReps = findViewById(R.id.editTextReps);
        editTextSets = findViewById(R.id.editTextSets);
        numPickerMin = findViewById(R.id.numPickerMin);
        numPickerSec = findViewById(R.id.numPickerSec);
        progressBar = findViewById(R.id.progressBar);

        TextInputEditText textInputNameExercise = findViewById(R.id.textInputNameExercise);
        TextInputEditText textInputNoteExercise = findViewById(R.id.textInputNoteExercise);
        textInputNameExercise.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textInputNoteExercise.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        numPickerMin.setMinValue(0);
        numPickerMin.setMaxValue(59);
        numPickerSec.setMinValue(0);
        numPickerSec.setMaxValue(59);

    }

    private void registerFilterChipChanged() {
        int id = chipGroup.getCheckedChipId();
        workedMuscleSelected = "";
        Chip chip = chipGroup.findViewById(id);
        workedMuscleSelected = chip.getText().toString();
    }

    @SuppressLint("InflateParams")
    private void addView() {
        final View setsRepsView = getLayoutInflater().inflate(R.layout.row_sets_reps, null, false);

        MaterialButton btnDelete = setsRepsView.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(v -> removeView(setsRepsView));

        linearLayout.addView(setsRepsView);
    }

    private void removeView(View view) {
        linearLayout.removeView(view);
    }

    private String readAllSetsReps() {
        StringBuilder result;

        if(!"".equals(editTextSets.getText().toString()) && !"".equals(editTextReps.getText().toString())) {
            result = new StringBuilder(editTextSets.getText().toString() + ":" + editTextReps.getText().toString() + Utility.SYMBOL_SPLIT);
        }else {
            result = new StringBuilder();
        }

        for(int i=0; i < linearLayout.getChildCount(); i++){
            View view = linearLayout.getChildAt(i);

            EditText editTextSetsApp = view.findViewById(R.id.editTextSets);
            EditText editTextRepsApp = view.findViewById(R.id.editTextReps);

            if(!"".equals(editTextSetsApp.getText().toString()) && !"".equals(editTextRepsApp.getText().toString())) {
                result.append(editTextSetsApp.getText().toString()).append(":").append(editTextRepsApp.getText().toString()).append(Utility.SYMBOL_SPLIT);
            }

        }
        return result.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> registerFilterChipChanged());
            });
        });
    }

    private void addChip(String nameChip) {
        final View chipView = getLayoutInflater().inflate(R.layout.merge_chip, null, false);

        Chip chip = chipView.findViewById(R.id.chip);
        chip.setText(nameChip);

        chipGroup.addView(chipView);
    }

}
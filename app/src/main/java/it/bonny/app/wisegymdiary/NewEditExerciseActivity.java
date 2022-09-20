package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
    private ToggleButton btnMaxReps;
    private TextInputLayout nameExercise, noteExercise;
    private Exercise exercise;
    private ChipGroup chipGroup;
    private LinearLayout linearLayout;
    private EditText editTextSets, editTextReps, editTextReps1;
    private TextView slash;
    private NumberPicker numPickerMin, numPickerSec;
    private ProgressBar progressBar, progressBar1;
    private Chip chipSelected = null;
    private ScrollView scrollView;
    private long idWorkoutDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_exercise);

        idWorkoutDay = getIntent().getLongExtra("idWorkoutDay", 0);
        long idExercise = getIntent().getLongExtra("idExercise", 0);

        initElements();

        if(idExercise == 0) {
            exercise = new Exercise();
            progressBar1.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            populateChipMuscle();
        }else {
            progressBar1.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);

            retrieveExercise(idExercise);
        }


        btnReturn.setOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            boolean isError = controlForm();
            if(!isError) {
                Intent intent = new Intent();
                intent.putExtra("page", Utility.ADD_EXERCISE);
                if(exercise.getId() > 0) {
                    intent.putExtra(Utility.EXTRA_EXERCISE_ID, exercise.getId());
                }
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
        });

        btnAddNewSetsReps.setOnClickListener(v -> addView(null, null, null));

        btnMaxReps.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                btnMaxReps.setTextColor(getColor(R.color.secondary));
                editTextReps.setText("");
                editTextReps1.setText("");
                editTextReps.setVisibility(View.INVISIBLE);
                editTextReps1.setVisibility(View.INVISIBLE);
                slash.setVisibility(View.INVISIBLE);
            }else {
                btnMaxReps.setTextColor(getColor(R.color.secondary_text));
                editTextReps.setVisibility(View.VISIBLE);
                editTextReps1.setVisibility(View.VISIBLE);
                slash.setVisibility(View.VISIBLE);
            }
        });

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
        editTextReps1 = findViewById(R.id.editTextReps1);
        editTextSets = findViewById(R.id.editTextSets);
        numPickerMin = findViewById(R.id.numPickerMin);
        numPickerSec = findViewById(R.id.numPickerSec);
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar1);
        scrollView = findViewById(R.id.scrollView);
        btnMaxReps = findViewById(R.id.btnMaxReps);
        slash = findViewById(R.id.slash);

        TextInputEditText textInputNameExercise = findViewById(R.id.textInputNameExercise);
        TextInputEditText textInputNoteExercise = findViewById(R.id.textInputNoteExercise);
        textInputNameExercise.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textInputNoteExercise.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        numPickerMin.setMinValue(0);
        numPickerMin.setMaxValue(59);
        numPickerSec.setMinValue(0);
        numPickerSec.setMaxValue(59);

    }

    @SuppressLint("InflateParams")
    private void addView(String numSets, String numReps, String numReps1) {
        final View setsRepsView = getLayoutInflater().inflate(R.layout.row_sets_reps, null, false);

        MaterialButton btnDelete = setsRepsView.findViewById(R.id.btnDelete);
        ToggleButton btnMaxReps = setsRepsView.findViewById(R.id.btnMaxReps);
        EditText sets = setsRepsView.findViewById(R.id.editTextSets);
        EditText reps = setsRepsView.findViewById(R.id.editTextReps);
        EditText reps1 = setsRepsView.findViewById(R.id.editTextReps1);
        TextView slash = setsRepsView.findViewById(R.id.slash);

        btnDelete.setOnClickListener(v -> removeView(setsRepsView));

        btnMaxReps.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                btnMaxReps.setTextColor(getColor(R.color.secondary));
                reps.setText("");
                reps1.setText("");
                reps.setVisibility(View.INVISIBLE);
                reps1.setVisibility(View.INVISIBLE);
                slash.setVisibility(View.INVISIBLE);
            }else {
                btnMaxReps.setTextColor(getColor(R.color.secondary_text));
                reps.setVisibility(View.VISIBLE);
                reps1.setVisibility(View.VISIBLE);
                slash.setVisibility(View.VISIBLE);
            }
        });

        if(numSets != null && numReps != null && numReps.contains(Utility.SYMBOL_MAX)) {
            btnMaxReps.setChecked(true);
            sets.setText(numSets);
            reps.setText("");
            reps1.setText("");
        }else if(numSets != null && numReps != null && numReps1 == null) {
            sets.setText(numSets);
            reps.setText(numReps);
        }else if(numSets != null && numReps != null) {
            sets.setText(numSets);
            reps.setText(numReps);
            reps1.setText(numReps1);
        }

        linearLayout.addView(setsRepsView);
    }

    private void removeView(View view) {
        linearLayout.removeView(view);
    }

    private String readAllSetsReps() {
        StringBuilder result;

        if(btnMaxReps.isChecked() && !"".equals(editTextSets.getText().toString())) {
            result = new StringBuilder(editTextSets.getText().toString() + Utility.SYMBOL_SPLIT_BETWEEN_SET_REP + Utility.SYMBOL_MAX + Utility.SYMBOL_SPLIT);
        }else if(!btnMaxReps.isChecked() && !"".equals(editTextSets.getText().toString())
                && !"".equals(editTextReps.getText().toString())) {

            String reps = editTextReps.getText().toString();
            if(!"".equals(editTextReps1.getText().toString()))
                reps += Utility.SYMBOL_SPLIT_BETWEEN_REPS + editTextReps1.getText().toString();
            result = new StringBuilder(editTextSets.getText().toString() + Utility.SYMBOL_SPLIT_BETWEEN_SET_REP + reps + Utility.SYMBOL_SPLIT);
        }else {
            result = new StringBuilder();
        }

        for(int i=0; i < linearLayout.getChildCount(); i++){
            View view = linearLayout.getChildAt(i);

            EditText editTextSetsApp = view.findViewById(R.id.editTextSets);
            EditText editTextRepsApp = view.findViewById(R.id.editTextReps);
            EditText editTextRepsApp1 = view.findViewById(R.id.editTextReps1);
            ToggleButton btnMaxReps = view.findViewById(R.id.btnMaxReps);

            if(btnMaxReps.isChecked() && !"".equals(editTextSetsApp.getText().toString())) {
                result.append(editTextSetsApp.getText().toString()).append(Utility.SYMBOL_SPLIT_BETWEEN_SET_REP)
                        .append(Utility.SYMBOL_MAX).append(Utility.SYMBOL_SPLIT);
            }else if(!btnMaxReps.isChecked() && !"".equals(editTextSetsApp.getText().toString())
                    && !"".equals(editTextRepsApp.getText().toString())) {
                String repsInView = editTextRepsApp.getText().toString();
                if(!"".equals(editTextRepsApp1.getText().toString()))
                    repsInView += Utility.SYMBOL_SPLIT_BETWEEN_REPS + editTextRepsApp1.getText().toString();
                result.append(editTextSetsApp.getText().toString()).append(Utility.SYMBOL_SPLIT_BETWEEN_SET_REP)
                        .append(repsInView).append(Utility.SYMBOL_SPLIT);
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

    private boolean controlForm() {
        boolean isError = false;

        if(nameExercise.getEditText() == null || "".equals(nameExercise.getEditText().getText().toString())) {
            isError = true;
            nameExercise.setError(getText(R.string.required_field));
        }else {
            exercise.setName(nameExercise.getEditText().getText().toString());
            nameExercise.setError(null);
        }

        TextView titleChooseIconMuscle = findViewById(R.id.titleChooseIconMuscle);
        if(chipSelected == null) {
            isError = true;
            titleChooseIconMuscle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
        }else {
            titleChooseIconMuscle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
            exercise.setWorkedMuscle(chipSelected.getText().toString());
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

        return isError;
    }

    private void populateChipMuscle() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<MuscleBean> muscleBeanList = AppDatabase.getInstance(getApplicationContext()).muscleDAO().findAllMusclesToExercise();

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

        if(exercise.getWorkedMuscle() != null && nameChip.equals(exercise.getWorkedMuscle())) {
            chip.setChipBackgroundColor(getColorStateList(R.color.blue_text));
            chip.setTextColor(getColor(R.color.blue_background));
            chip.setChecked(true);
            chipSelected = chip;
        }else {
            chip.setChipBackgroundColor(getColorStateList(R.color.blue_background));
            chip.setTextColor(getColor(R.color.blue_text));
            chip.setChecked(false);
        }

        chip.setOnClickListener(view -> {
            if(chipSelected == null) {
                chip.setChecked(true);
                chip.setChipBackgroundColor(getColorStateList(R.color.blue_text));
                chip.setTextColor(getColor(R.color.blue_background));
                chipSelected = chip;
            }else {
                if(!chipSelected.equals(chip)) {
                    chipSelected.setChecked(true);
                    chipSelected.setChipBackgroundColor(getColorStateList(R.color.blue_background));
                    chipSelected.setTextColor(getColor(R.color.blue_text));
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(getColorStateList(R.color.blue_text));
                    chip.setTextColor(getColor(R.color.blue_background));
                    chipSelected = chip;
                }
            }
        });

        chipGroup.addView(chip);
    }

    private void retrieveExercise(long idExercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exercise = AppDatabase.getInstance(getApplicationContext()).exerciseDAO().findExerciseById(idExercise);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(nameExercise.getEditText() != null)
                        nameExercise.getEditText().setText(exercise.getName());
                    if(noteExercise.getEditText() != null)
                        noteExercise.getEditText().setText(exercise.getNote());

                    String[] splitRestTime = exercise.getRestTime().split(Utility.SYMBOL_SPLIT);
                    numPickerMin.setValue(Integer.parseInt(splitRestTime[0]));
                    numPickerSec.setValue(Integer.parseInt(splitRestTime[1]));

                    String[] splitNumSetsReps = exercise.getNumSetsReps().split(Utility.SYMBOL_SPLIT);
                    for(int i = 0; i < splitNumSetsReps.length; i ++) {
                        String[] singleNumSetsReps = splitNumSetsReps[i].split(Utility.SYMBOL_SPLIT_BETWEEN_SET_REP);
                        String numSet = singleNumSetsReps[0];
                        String numReps = singleNumSetsReps[1];

                        if(numReps.contains(Utility.SYMBOL_MAX)) {


                            if(i >= 1) {
                                addView(numSet, numReps, null);
                            }else {
                                btnMaxReps.setChecked(true);
                                editTextSets.setText(numSet);
                                editTextReps.setText("");
                            }
                        }else if(numReps.contains(Utility.SYMBOL_SPLIT_BETWEEN_REPS)) {
                            String[] numRepSplit = numReps.split(Utility.SYMBOL_SPLIT_BETWEEN_REPS);
                            String numRep = numRepSplit[0];
                            String numRep1 = numRepSplit[1];

                            if(i >= 1) {
                                addView(numSet, numRep, numRep1);
                            }else {
                                editTextSets.setText(numSet);
                                editTextReps.setText(numRep);
                                editTextReps1.setText(numRep1);
                            }
                        }else {
                            if(i >= 1) {
                                addView(numSet, numReps, null);
                            }else {
                                editTextSets.setText(numSet);
                                editTextReps.setText(numReps);
                            }
                        }

                    }

                    populateChipMuscle();
                    progressBar1.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                }
            });
        });
    }

}
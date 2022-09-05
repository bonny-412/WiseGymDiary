package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
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

        checkButtonSave();

        btnReturn.setOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            if(nameExercise.getEditText() != null) {
                exercise.setName(nameExercise.getEditText().getText().toString());
                exercise.setIdWorkDay(idWorkoutDay);
                exercise.setNote(noteExercise.getEditText() != null ? noteExercise.getEditText().getText().toString(): "");
                exercise.setWorkedMuscle(workedMuscleSelected);
                exercise.setNumSetsReps(readAllSetsReps());
                exercise.setRestTime("0");
                    /*AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                        appDatabase.workoutDayDAO().insert(workoutDay);
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.routine_added), Snackbar.LENGTH_SHORT).show();
                        finish();
                    });*/
            }
        });

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> registerFilterChipChanged());

        nameExercise.addOnEditTextAttachedListener(textInputLayout -> checkButtonSave());

        btnAddNewSetsReps.setOnClickListener(v -> addView());

        editTextSets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtonSave();
            }
        });

        editTextReps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtonSave();
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
        editTextSets = findViewById(R.id.editTextSets);

        TextInputEditText textInputNameExercise = findViewById(R.id.textInputNameExercise);
        TextInputEditText textInputNoteExercise = findViewById(R.id.textInputNoteExercise);
        textInputNameExercise.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textInputNoteExercise.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    private void checkButtonSave() {
        if(!"".equals(workedMuscleSelected) &&
                nameExercise.getEditText() != null &&
                nameExercise.getEditText().getText() != null &&
                nameExercise.getEditText().getText().length() > 0 &&
                !"".equals(editTextSets.getText().toString()) &&
                !"".equals(editTextReps.getText().toString()) ){
            btnSave.setVisibility(View.VISIBLE);
        }else {
            btnSave.setVisibility(View.INVISIBLE);
        }
    }

    private void registerFilterChipChanged() {
        Integer id = chipGroup.getCheckedChipId();
        workedMuscleSelected = "";
        Chip chip = chipGroup.findViewById(id);
        workedMuscleSelected = chip.getText().toString();

        checkButtonSave();
    }

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

        result = new StringBuilder(editTextSets.getText().toString() + ":" + editTextReps.getText().toString() + Utility.SYMBOL_SPLIT);

        for(int i=0; i < linearLayout.getChildCount(); i++){
            View view = linearLayout.getChildAt(i);

            EditText editTextSetsApp = view.findViewById(R.id.editTextSets);
            EditText editTextRepsApp = view.findViewById(R.id.editTextReps);

            result.append(editTextSetsApp.getText().toString()).append(":").append(editTextRepsApp.getText().toString()).append(Utility.SYMBOL_SPLIT);

        }
        return result.toString();
    }

}
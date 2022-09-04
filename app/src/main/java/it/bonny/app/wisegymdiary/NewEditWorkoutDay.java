package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.database.AppDatabase;

public class NewEditWorkoutDay extends AppCompatActivity {

    private MaterialButton btnSave;
    private TextInputLayout nameWorkoutDay, noteWorkoutDay;
    private List<String> workedMuscleListSelected;
    private WorkoutDay workoutDay;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_workout_day);

        boolean newFlag = getIntent().getBooleanExtra("newFlag", false);
        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);

        if(newFlag) {
            workedMuscleListSelected = new ArrayList<>();
            workoutDay = new WorkoutDay();
        }


        MaterialButton btnReturn = findViewById(R.id.btnReturn);
        btnSave = findViewById(R.id.btnSave);
        nameWorkoutDay = findViewById(R.id.nameWorkoutDay);
        noteWorkoutDay = findViewById(R.id.noteWorkoutDay);
        chipGroup = findViewById(R.id.chipGroup);
        TextInputEditText textInputNameWorkoutDay = findViewById(R.id.textInputNameWorkoutDay);
        TextInputEditText textInputNoteWorkoutDay = findViewById(R.id.textInputNoteWorkoutDay);
        textInputNameWorkoutDay.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textInputNoteWorkoutDay.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        checkButtonSave();

        btnReturn.setOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            if(nameWorkoutDay.getEditText() != null) {
                workoutDay.setName(nameWorkoutDay.getEditText().getText().toString());
                workoutDay.setNumTimeDone(0);
                workoutDay.setIdWorkPlan(idWorkoutPlan);
                workoutDay.setNote(noteWorkoutDay.getEditText() != null ? noteWorkoutDay.getEditText().getText().toString(): "");
                if(workedMuscleListSelected != null && workedMuscleListSelected.size() > 0) {
                    StringBuilder app = new StringBuilder();
                    for(int i = 0; i < workedMuscleListSelected.size(); i ++) {
                        app.append(workedMuscleListSelected.get(i));
                        if(i < workedMuscleListSelected.size() - 1)
                            app.append(", ");
                    }
                    workoutDay.setWorkedMuscle(app.toString());
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                        appDatabase.workoutDayDAO().insert(workoutDay);
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.routine_added), Snackbar.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }
        });

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> registerFilterChipChanged());

        nameWorkoutDay.addOnEditTextAttachedListener(textInputLayout -> checkButtonSave());

    }

    private void checkButtonSave() {
        if(workedMuscleListSelected.size() > 0 && nameWorkoutDay.getEditText() != null &&
                nameWorkoutDay.getEditText().getText() != null && nameWorkoutDay.getEditText().getText().length() > 0){
            btnSave.setVisibility(View.VISIBLE);
        }else {
            btnSave.setVisibility(View.INVISIBLE);
        }
    }

    private void registerFilterChipChanged() {
        List<Integer> ids = chipGroup.getCheckedChipIds();
        workedMuscleListSelected.clear();

        for(Integer id: ids) {
            Chip chip = chipGroup.findViewById(id);
            workedMuscleListSelected.add(chip.getText().toString());
        }

        checkButtonSave();
    }

}
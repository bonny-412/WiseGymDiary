package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.InputType;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

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
    private MaterialButton btnReturn;

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


        initElements();

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
            if(workedMuscleListSelected == null || workedMuscleListSelected.size() == 0) {
                isError = true;
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
            }else {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
                StringBuilder app = new StringBuilder();
                for(int i = 0; i < workedMuscleListSelected.size(); i ++) {
                    app.append(workedMuscleListSelected.get(i));
                    if(i < workedMuscleListSelected.size() - 1)
                        app.append(", ");
                }
                workoutDay.setWorkedMuscle(app.toString());
            }

            if(!isError) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                    appDatabase.workoutDayDAO().insert(workoutDay);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.routine_added), Snackbar.LENGTH_SHORT).show();
                    finish();
                });
            }else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                btnSave.startAnimation(shake);
            }
        });

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> registerFilterChipChanged());

    }

    private void registerFilterChipChanged() {
        List<Integer> ids = chipGroup.getCheckedChipIds();
        workedMuscleListSelected.clear();

        for(Integer id: ids) {
            Chip chip = chipGroup.findViewById(id);
            workedMuscleListSelected.add(chip.getText().toString());
        }
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
    }

}
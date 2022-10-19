package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.CategoryExerciseBean;
import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.bean.CategoryMuscleBean;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.Utility;

public class NewEditExerciseActivity extends AppCompatActivity {

    private TextView titlePage;
    private ProgressBar progressBar, progressBarMuscle, progressBarExerciseBased;
    private EditText name, note;
    private LinearLayout containerForm;
    private TextInputLayout nameLayout;
    private TextView titleChooseIconMuscle, titleChooseExerciseBased;
    private MaterialButton btnReturn, btnSave;

    private ExerciseBean exerciseBean;
    private ChipGroup chipGroup, chipGroupExerciseBased;
    private Chip chipMuscleSelected = null, chipCategoryExerciseSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_exercise);

        long idExercise = getIntent().getLongExtra("idExercise", 0);

        initElements();

        btnReturn.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            boolean isError = false;
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

            if(name.getText() == null || "".equals(name.getText().toString().trim())) {
                isError = true;
                nameLayout.setError(getString(R.string.required_field));
                nameLayout.startAnimation(shake);
            }else
                nameLayout.setError(null);

            if(chipMuscleSelected == null) {
                isError = true;
                titleChooseIconMuscle.setTextColor(getColor(R.color.primary));
                titleChooseIconMuscle.startAnimation(shake);
            }else
                titleChooseIconMuscle.setTextColor(getColor(R.color.secondary_text));

            if(chipCategoryExerciseSelected == null) {
                isError = true;
                titleChooseExerciseBased.setTextColor(getColor(R.color.primary));
                titleChooseExerciseBased.startAnimation(shake);
            }else
                titleChooseExerciseBased.setTextColor(getColor(R.color.secondary_text));

            if(!isError) {
                Intent intent = new Intent();
                intent.putExtra("id", idExercise);
                intent.putExtra("name", name.getText().toString().trim());
                intent.putExtra("idCategoryMuscle", chipMuscleSelected.getText().toString().trim());
                intent.putExtra("idCategoryExercise", chipCategoryExerciseSelected.getText().toString().trim());
                intent.putExtra("note", note.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        if(idExercise == 0) {
            exerciseBean = new ExerciseBean();
            exerciseBean.setIdCategoryExercise(getString(R.string.category_exercise_weight_reps));

            titlePage.setText(getString(R.string.title_page_new_edit_exercise));

            populateChipMuscle();
            populateChipCategoryExercise();
        }else {
            titlePage.setText(getString(R.string.title_page_new_edit_exercise_edit));

            retrieveExercise(idExercise);
        }

    }

    private void initElements() {
        titlePage = findViewById(R.id.titlePage);
        btnSave = findViewById(R.id.btnSave);
        btnReturn = findViewById(R.id.btnReturn);
        progressBar = findViewById(R.id.progressBar);
        name = findViewById(R.id.name);
        note = findViewById(R.id.note);
        containerForm = findViewById(R.id.containerForm);
        progressBarMuscle = findViewById(R.id.progressBarMuscle);
        progressBarExerciseBased = findViewById(R.id.progressBarExerciseBased);
        chipGroup = findViewById(R.id.chipGroup);
        chipGroupExerciseBased = findViewById(R.id.chipGroupExerciseBased);
        nameLayout = findViewById(R.id.nameLayout);
        titleChooseIconMuscle = findViewById(R.id.titleChooseIconMuscle);
        titleChooseExerciseBased = findViewById(R.id.titleChooseExerciseBased);
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
        progressBarMuscle.setVisibility(View.VISIBLE);
        chipGroup.setVisibility(View.GONE);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CategoryMuscleBean> categoryMuscleBeanList = AppDatabase.getInstance(getApplicationContext()).categoryMuscleDAO().findAllMuscles();

            runOnUiThread(() -> {
                for(CategoryMuscleBean categoryMuscleBean : categoryMuscleBeanList) {
                    addChip(categoryMuscleBean.getName(), false);
                }
                progressBarMuscle.setVisibility(View.GONE);
                chipGroup.setVisibility(View.VISIBLE);
                chipGroup.setSelectionRequired(true);
            });
        });
    }

    private void populateChipCategoryExercise() {
        progressBarExerciseBased.setVisibility(View.VISIBLE);
        chipGroupExerciseBased.setVisibility(View.GONE);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CategoryExerciseBean> categoryExerciseBeans = AppDatabase.getInstance(getApplicationContext()).categoryExerciseDAO().findAllCategoryExercise();

            runOnUiThread(() -> {
                for(CategoryExerciseBean bean: categoryExerciseBeans) {
                    addChip(bean.getName(), true);
                }

                progressBarExerciseBased.setVisibility(View.GONE);
                chipGroupExerciseBased.setVisibility(View.VISIBLE);
                chipGroupExerciseBased.setSelectionRequired(true);
            });
        });
    }

    private void addChip(String nameChip, boolean isCategoryExercise) {
        Chip chip = new Chip(this);
        chip.setText(nameChip);

        if(isCategoryExercise) {
            if(exerciseBean.getIdCategoryExercise() != null && nameChip.equals(exerciseBean.getIdCategoryExercise())) {
                chip.setChipBackgroundColor(getColorStateList(R.color.background));
                chip.setChipBackgroundColor(getColorStateList(R.color.primary));
                chip.setTextColor(getColor(R.color.white));
                chipCategoryExerciseSelected = chip;
            }else {
                chip.setChipBackgroundColor(getColorStateList(R.color.background));
                chip.setTextColor(getColor(R.color.secondary_text));
                chip.setChecked(false);
            }
        }else {
            if(exerciseBean.getIdCategoryMuscle() != null && nameChip.equals(exerciseBean.getIdCategoryMuscle())) {
                chip.setChipBackgroundColor(getColorStateList(R.color.background));
                chip.setChipBackgroundColor(getColorStateList(R.color.primary));
                chip.setTextColor(getColor(R.color.white));
                chipMuscleSelected = chip;
            }else {
                chip.setChipBackgroundColor(getColorStateList(R.color.background));
                chip.setTextColor(getColor(R.color.secondary_text));
                chip.setChecked(false);
            }
        }

        chip.setOnClickListener(view -> {
            if(isCategoryExercise) {
                if(chipCategoryExerciseSelected == null) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(getColorStateList(R.color.primary));
                    chip.setTextColor(getColor(R.color.white));
                    chipCategoryExerciseSelected = chip;
                }else {
                    if(!chipCategoryExerciseSelected.equals(chip)) {
                        chipCategoryExerciseSelected.setChecked(true);
                        chipCategoryExerciseSelected.setChipBackgroundColor(getColorStateList(R.color.background));
                        chipCategoryExerciseSelected.setTextColor(getColor(R.color.secondary_text));
                        chip.setChecked(true);
                        chip.setChipBackgroundColor(getColorStateList(R.color.primary));
                        chip.setTextColor(getColor(R.color.white));
                        chipCategoryExerciseSelected = chip;
                    }
                }
            }else {
                if(chipMuscleSelected == null) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(getColorStateList(R.color.primary));
                    chip.setTextColor(getColor(R.color.white));
                    chipMuscleSelected = chip;
                }else {
                    if(!chipMuscleSelected.equals(chip)) {
                        chipMuscleSelected.setChecked(true);
                        chipMuscleSelected.setChipBackgroundColor(getColorStateList(R.color.background));
                        chipMuscleSelected.setTextColor(getColor(R.color.secondary_text));
                        chip.setChecked(true);
                        chip.setChipBackgroundColor(getColorStateList(R.color.primary));
                        chip.setTextColor(getColor(R.color.white));
                        chipMuscleSelected = chip;
                    }
                }
            }
        });

        if(isCategoryExercise)
            chipGroupExerciseBased.addView(chip);
        else
            chipGroup.addView(chip);
    }

    private void retrieveExercise(long idExercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseBean = AppDatabase.getInstance(getApplicationContext()).exerciseDAO().findExerciseById(idExercise);
            runOnUiThread(() -> {
                name.setText(exerciseBean.getName());
                note.setText(exerciseBean.getNote());

                populateChipMuscle();
                populateChipCategoryExercise();
                showHideProgressBar(false);
            });
        });
    }

    private void showHideProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            containerForm.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            containerForm.setVisibility(View.VISIBLE);
        }
    }

}
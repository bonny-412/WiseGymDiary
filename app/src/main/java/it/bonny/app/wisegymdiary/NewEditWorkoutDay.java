package it.bonny.app.wisegymdiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.transition.TransitionInflater;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.bonny.app.wisegymdiary.bean.MuscleBean;
import it.bonny.app.wisegymdiary.bean.Session;
import it.bonny.app.wisegymdiary.component.GridViewChooseColorAdapter;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.Utility;

public class NewEditWorkoutDay extends AppCompatActivity {

    private final Utility utility = new Utility();
    private Button btnSave;
    private TextInputEditText nameWorkoutDay, noteWorkoutDay;
    private Session session;
    private Button btnReturn;
    private ProgressBar progressBar, progressBar1;
    private final List<String> chipsSelectedList = new ArrayList<>();
    private ChipGroup chipGroup;
    private NestedScrollView scrollView;
    private ConstraintLayout containerLabelColor;
    private LinearLayout colorSession;
    private TextView labelSession, titlePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_workout_day);

        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);
        long idWorkoutDay = getIntent().getLongExtra("idWorkoutDay", 0);

        initElements();

        if(idWorkoutDay == 0) {
            titlePage.setText(R.string.title_page_new_workout_day);
            session = new Session();
            session.setNumTimeDone(0);
            session.setIdWorkPlan(idWorkoutPlan);
            progressBar1.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            populateChipMuscle();
        }else {
            titlePage.setText(R.string.title_page_edit_workout_day);
            progressBar1.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);

            retrieveRoutine(idWorkoutDay);
        }

        btnReturn.setOnClickListener(view -> finish());

        nameWorkoutDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String lab = "";
                if(session != null && session.getLabel() != null) {
                    lab = session.getLabel();
                }else {
                    if (nameWorkoutDay.getText() != null && nameWorkoutDay.getText().toString().length() > 0)
                        lab = editable.toString().substring(0, 1).toUpperCase();
                    session.setLabel(lab);
                }
                labelSession.setText(lab);
            }
        });

        btnSave.setOnClickListener(view -> {
            boolean isError = false;
            if(nameWorkoutDay.getText() == null || "".equals(nameWorkoutDay.getText().toString())) {
                isError = true;
                nameWorkoutDay.setError(getText(R.string.required_field));
            }else {
                nameWorkoutDay.setError(null);
                session.setName(nameWorkoutDay.getText().toString());
            }

            session.setNote(noteWorkoutDay.getText() != null ? noteWorkoutDay.getText().toString(): "");

            TextView titleChooseIconNewAccount = findViewById(R.id.titleChooseIconNewAccount);
            if(chipsSelectedList == null || chipsSelectedList.size() == 0) {
                isError = true;
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
            }else {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.onSurface));
                StringBuilder app = new StringBuilder();
                String[] strings = new String[chipsSelectedList.size()];
                chipsSelectedList.toArray(strings);
                Arrays.sort(strings);
                for(int i = 0; i < strings.length; i ++) {
                    app.append(strings[i]);
                    if(i < strings.length - 1)
                        app.append(", ");
                }
                session.setWorkedMuscle(app.toString());
            }

            if(!isError) {
                Intent intent = new Intent();
                intent.putExtra("page", Utility.ADD_SESSION);
                if(session.getId() > 0) {
                    intent.putExtra(Utility.EXTRA_SESSION_ID, session.getId());
                }
                intent.putExtra(Utility.EXTRA_SESSION_NAME, session.getName());
                intent.putExtra(Utility.EXTRA_SESSION_NUM_TIME_DONE, session.getNumTimeDone());
                intent.putExtra(Utility.EXTRA_SESSION_ID_WORK_PLAIN, session.getIdWorkPlan());
                intent.putExtra(Utility.EXTRA_SESSION_WORKED_MUSCLE, session.getWorkedMuscle());
                intent.putExtra(Utility.EXTRA_SESSION_NOTE, session.getNote());
                intent.putExtra(Utility.EXTRA_SESSION_LABEL, session.getLabel());
                intent.putExtra(Utility.EXTRA_SESSION_COLOR, session.getColor());
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                btnSave.startAnimation(shake);
            }
        });

        containerLabelColor.setOnClickListener(view -> showAlertChooseLabelColor());

    }

    private void initElements() {
        btnReturn = findViewById(R.id.btnReturn);
        btnSave = findViewById(R.id.btnSave);
        nameWorkoutDay = findViewById(R.id.textInputNameWorkoutDay);
        noteWorkoutDay = findViewById(R.id.textInputNoteWorkoutDay);
        chipGroup = findViewById(R.id.chipGroup);
        titlePage = findViewById(R.id.titlePage);

        nameWorkoutDay.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        noteWorkoutDay.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar1);
        scrollView = findViewById(R.id.scrollView);
        containerLabelColor = findViewById(R.id.containerLabelColor);
        colorSession = findViewById(R.id.color);
        labelSession = findViewById(R.id.titleLabel);
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

        if(session.getWorkedMuscle() != null && session.getWorkedMuscle().contains(nameChip)) {
            chip.setChipBackgroundColor(getColorStateList(R.color.secondary));
            chip.setTextColor(getColor(R.color.onSecondary));
            chip.setChecked(true);
            chipsSelectedList.add(chip.getText().toString());
        }else {
            chip.setChipBackgroundColor(getColorStateList(R.color.secondaryContainer));
            chip.setTextColor(getColor(R.color.onSecondaryContainer));
            chip.setChecked(false);

        }

        chip.setOnClickListener(view -> {
            if(!chipsSelectedList.contains(chip.getText().toString())) {
                chip.setChecked(true);
                chip.setChipBackgroundColor(getColorStateList(R.color.secondary));
                chip.setTextColor(getColor(R.color.onSecondary));
                chipsSelectedList.add(chip.getText().toString());
            }else {
                chip.setChecked(false);
                chip.setChipBackgroundColor(getColorStateList(R.color.secondaryContainer));
                chip.setTextColor(getColor(R.color.onSecondaryContainer));
                chipsSelectedList.remove(chip.getText().toString());
            }
        });

        chipGroup.addView(chip);
    }

    private void retrieveRoutine(long idWorkoutDay) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                session = AppDatabase.getInstance(getApplicationContext()).workoutDayDAO().findWorkoutDayByPrimaryKey(idWorkoutDay);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       nameWorkoutDay.setText(session.getName());
                       noteWorkoutDay.setText(session.getNote());
                       colorSession.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), utility.getColorByPosition(session.getColor())));
                       labelSession.setText(session.getLabel());

                        populateChipMuscle();
                        progressBar1.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        btnSave.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void showAlertChooseLabelColor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInfoDialog = View.inflate(this, R.layout.alert_choose_label_color, null);
        builder.setCancelable(false);
        builder.setView(viewInfoDialog);
        MaterialButton btnCancel = viewInfoDialog.findViewById(R.id.btnCancel);
        MaterialButton btnDelete = viewInfoDialog.findViewById(R.id.btnDelete);
        GridView gridViewColor = viewInfoDialog.findViewById(R.id.containerChooseColor);
        TextInputEditText textLabel = viewInfoDialog.findViewById(R.id.textInputNameWorkoutDay);
        final int[] colorSelected = {0};

        GridViewChooseColorAdapter colorAdapter =new GridViewChooseColorAdapter(getApplicationContext());
        gridViewColor.setAdapter(colorAdapter);

        if(session.getColor() > 0) {
            colorSelected[0] = session.getColor();
        }

        textLabel.setText(session.getLabel());

        colorAdapter.makeAllUnselect(colorSelected[0]);
        colorAdapter.notifyDataSetChanged();

        gridViewColor.setOnItemClickListener((adapterView, view, i, l) -> {
            colorAdapter.makeAllUnselect(i);
            colorAdapter.notifyDataSetChanged();
            colorSelected[0] = i;
        });


        final AlertDialog dialog = builder.create();
        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnDelete.setOnClickListener(v -> {
            session.setColor(colorSelected[0]);
            if(textLabel.getText() != null)
                session.setLabel(textLabel.getText().toString());
            else
                session.setLabel("");
            colorSession.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), utility.getColorByPosition(colorSelected[0])));
            labelSession.setText(session.getLabel());

            dialog.dismiss();
        });
        dialog.show();
    }

}
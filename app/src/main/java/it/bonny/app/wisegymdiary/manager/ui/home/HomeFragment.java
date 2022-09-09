package it.bonny.app.wisegymdiary.manager.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.bonny.app.wisegymdiary.NewEditExerciseActivity;
import it.bonny.app.wisegymdiary.NewEditWorkoutDay;
import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.component.BottomSheetWorkoutDay;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.database.AppExecutors;
import it.bonny.app.wisegymdiary.manager.MainActivity;
import it.bonny.app.wisegymdiary.util.BottomSheetClickListener;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickBottomSheetInterface;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickInterface;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.component.ExerciseHomePageAdapter;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements BottomSheetClickListener {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private ConstraintLayout containerSettings, containerWorkoutPlanEmpty, containerWorkoutPlan, containerRoutineEmpty, containerExerciseEmpty;
    private TextView nameWorkoutPlan, infoWeekWorkoutPlan;
    private WorkoutPlan workoutPlanApp;
    private MaterialButton btnNewRoutine, btnOptionsWorkoutPlan, btnNewExerciseEmpty;
    private ExerciseHomePageAdapter exerciseHomePageAdapter;
    private RecyclerView recyclerViewExercise;
    private MaterialButton btnAddExercise;

    private MaterialCardView btnWorkoutDaySelected;
    private MaterialButton showTransactionListBtn;
    private ConstraintLayout containerRecyclerView;
    private TextView nameWorkoutDaySelected;

    private BottomSheetWorkoutDay bottomSheetWorkoutDay;

    Animation slide_down, slide_up;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showWelcomeAlert();

        final TextView textView = binding.nameWorkoutPlan;
        initElements(root.getContext());



        homeViewModel.getWorkoutPlan().observe(getViewLifecycleOwner(), workoutPlan -> {
            if(workoutPlan != null) {
                textView.setText(workoutPlan.getName());
                containerSettings.setVisibility(View.VISIBLE);
                containerWorkoutPlanEmpty.setVisibility(View.GONE);
                containerWorkoutPlan.setVisibility(View.VISIBLE);
                nameWorkoutPlan.setText(workoutPlan.getName());
                String numWeek = "2";
                String weekStrFinal = getString(R.string.week) + " " + numWeek + " " + getString(R.string.of) + " " + workoutPlan.getNumWeek();
                Spannable spannableString = new SpannableString(weekStrFinal);
                spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(binding.getRoot().getContext(), R.color.secondary)),
                        getString(R.string.week).length() + 1, getString(R.string.week).length() + 1 + numWeek.length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                infoWeekWorkoutPlan.setText(spannableString);

                workoutPlanApp.copy(workoutPlan);

                homeViewModel.getWorkoutDayList(workoutPlan.getId()).observe(getViewLifecycleOwner(), workoutDays -> {
                    if(workoutDays != null && workoutDays.size() > 0) {
                        customUIRoutineIsEmpty(false);
                        homeViewModel.setWorkoutDaySelected().setValue(workoutDays.get(0));
                        homeViewModel.getWorkoutDaySelected().observe(getViewLifecycleOwner(), this::changedWorkoutDay);
                    }else {
                        customUIRoutineIsEmpty(true);
                        btnNewRoutine.setOnClickListener(view -> callNewWorkoutDay());
                    }
                });
            }else {
                containerSettings.setVisibility(View.INVISIBLE);
                containerWorkoutPlanEmpty.setVisibility(View.VISIBLE);
                containerWorkoutPlan.setVisibility(View.GONE);
            }
        });

        btnOptionsWorkoutPlan.setOnClickListener(view -> {

        });

        btnAddExercise.setOnClickListener(v -> callNewExercise(0));

        btnNewExerciseEmpty.setOnClickListener(v -> callNewExercise(0));

        btnWorkoutDaySelected.setOnClickListener(v -> {
            if(homeViewModel.getWorkoutDaySelected() != null && homeViewModel.getWorkoutDaySelected().getValue() != null) {
                bottomSheetWorkoutDay = new BottomSheetWorkoutDay(workoutPlanApp.getId(), homeViewModel.getWorkoutDaySelected().getValue().getId(), getContext(), HomeFragment.this);
                bottomSheetWorkoutDay.show(getParentFragmentManager(), "CHANGE_ACCOUNT");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showWelcomeAlert() {
        boolean firstStart = false;
        SharedPreferences prefs;
        if(binding.getRoot().getContext() != null) {
            try {
                prefs = binding.getRoot().getContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE);
                firstStart = prefs.getBoolean("firstStart", true);
            }catch (Exception e) {
                //TODO: Firebase
                Log.e("HOME_FRAGMENT", e.toString());
            }
        }
        if(firstStart) {
            SharedPreferences.Editor editor = binding.getRoot().getContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE).edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }
    }

    private void initElements(Context context) {
        workoutPlanApp = new WorkoutPlan();

        slide_down = AnimationUtils.loadAnimation(context,
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);

        containerSettings = binding.containerSettings;
        containerWorkoutPlanEmpty = binding.containerWorkoutPlanEmpty;
        containerWorkoutPlan = binding.containerWorkoutPlan;
        nameWorkoutPlan = binding.nameWorkoutPlan;
        infoWeekWorkoutPlan = binding.infoWeekWorkoutPlan;
        containerRoutineEmpty = binding.containerRoutineEmpty;

        btnNewRoutine = binding.btnNewRoutine;
        btnOptionsWorkoutPlan = binding.btnOptionsWorkoutPlan;

        containerExerciseEmpty = binding.containerExerciseEmpty;
        btnAddExercise = binding.btnAddExercise;
        btnNewExerciseEmpty = binding.btnNewExerciseEmpty;

        btnWorkoutDaySelected = binding.btnWorkoutDaySelected;
        showTransactionListBtn = binding.showTransactionListBtn;
        containerRecyclerView = binding.containerRecyclerView;
        nameWorkoutDaySelected = binding.nameWorkoutDaySelected;

        recyclerViewExercise = binding.recyclerView;
        exerciseHomePageAdapter = new ExerciseHomePageAdapter(context, new RecyclerViewClickInterface() {
            @Override
            public void recyclerViewItemClick(long idElement) {
                callNewExercise(idElement);
            }
        });
        setAdapter();
    }

    private void customUIRoutineIsEmpty(boolean isEmpty) {
        if(isEmpty) {
            containerRoutineEmpty.setVisibility(View.VISIBLE);
            btnWorkoutDaySelected.setVisibility(View.GONE);
            showTransactionListBtn.setVisibility(View.GONE);
        }else {
            containerRoutineEmpty.setVisibility(View.GONE);
            btnWorkoutDaySelected.setVisibility(View.VISIBLE);
            showTransactionListBtn.setVisibility(View.VISIBLE);
        }
    }

    private void customUIExerciseIsEmpty(boolean isEmpty) {
        if(isEmpty) {
            containerExerciseEmpty.setVisibility(View.VISIBLE);
            containerRecyclerView.setVisibility(View.GONE);
            btnAddExercise.setVisibility(View.GONE);
        }else {
            containerExerciseEmpty.setVisibility(View.GONE);
            containerRecyclerView.setVisibility(View.VISIBLE);
            btnAddExercise.setVisibility(View.VISIBLE);
        }
    }

    void setAdapter() {
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewExercise.setHasFixedSize(true);
        recyclerViewExercise.setItemAnimator(new DefaultItemAnimator());
        recyclerViewExercise.setAdapter(exerciseHomePageAdapter);
    }

    private void callNewWorkoutDay() {
        Intent intent = new Intent(getActivity(), NewEditWorkoutDay.class);
        intent.putExtra("idWorkoutPlan", workoutPlanApp.getId());
        intent.putExtra("newFlag", true);
        someActivityResultLauncher.launch(intent);
    }

    private void callNewExercise(long idExercise) {
        if(homeViewModel.getWorkoutDaySelected() != null && homeViewModel.getWorkoutDaySelected().getValue() != null) {
            Intent intent = new Intent(getActivity(), NewEditExerciseActivity.class);
            intent.putExtra("idWorkoutDay", homeViewModel.getWorkoutDaySelected().getValue().getId());
            if(idExercise > 0)
                intent.putExtra("idExercise", idExercise);
            someActivityResultLauncher.launch(intent);
        }
    }

    //Bottom sheet
    @Override
    public void onItemClick(long idElement) {
        if(idElement > 0) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                WorkoutDay workoutDay = AppDatabase.getInstance(getContext()).workoutDayDAO().getWorkoutDayById(idElement);
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        homeViewModel.setWorkoutDaySelected().setValue(workoutDay);
                        homeViewModel.getWorkoutDaySelected().observe(getViewLifecycleOwner(), this::changedWorkoutDay);
                    });
                }
            });
        }else {
            callNewWorkoutDay();
        }
    }

    private void changedWorkoutDay(WorkoutDay workoutDay) {
        nameWorkoutDaySelected.setText(workoutDay.getName());
        homeViewModel.getExerciseList(workoutDay.getId()).observe(getViewLifecycleOwner(), exercises -> {
            if(exercises != null && exercises.size() > 0) {
                customUIExerciseIsEmpty(false);
                exerciseHomePageAdapter.updateExerciseList(exercises);
            }else {
                customUIExerciseIsEmpty(true);
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null) {
                            int page = data.getIntExtra("page", Utility.ADD_WORKOUT_DAY);
                            if(page == Utility.ADD_WORKOUT_DAY) {
                                String name = data.getStringExtra(Utility.EXTRA_WORKOUT_DAY_NAME);
                                Integer numTimeDone = data.getIntExtra(Utility.EXTRA_WORKOUT_DAY_NUM_TIME_DONE, 0);
                                long idWorkoutPlan = data.getLongExtra(Utility.EXTRA_WORKOUT_DAY_ID_WORK_PLAIN, 0);
                                String workedMuscle = data.getStringExtra(Utility.EXTRA_WORKOUT_DAY_WORKED_MUSCLE);
                                String note = data.getStringExtra(Utility.EXTRA_WORKOUT_DAY_NOTE);

                                WorkoutDay workoutDay = new WorkoutDay(name, numTimeDone, idWorkoutPlan, workedMuscle, note);
                                homeViewModel.insert(workoutDay);
                                if(getActivity() != null)
                                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.routine_added), Snackbar.LENGTH_SHORT).show();
                            }else if(page == Utility.ADD_EXERCISE) {
                                String name = data.getStringExtra(Utility.EXTRA_EXERCISE_NAME);
                                long idWorkoutPlan = data.getLongExtra(Utility.EXTRA_EXERCISE_ID_WORK_DAY,0);
                                String note = data.getStringExtra(Utility.EXTRA_EXERCISE_NOTE);
                                String restTime = data.getStringExtra(Utility.EXTRA_EXERCISE_REST_TIME);
                                String numSetsReps = data.getStringExtra(Utility.EXTRA_EXERCISE_NUM_SETS_REPS);
                                String workedMuscle = data.getStringExtra(Utility.EXTRA_EXERCISE_WORKED_MUSCLE);

                                Exercise exercise = new Exercise(name, idWorkoutPlan, note, restTime, numSetsReps, workedMuscle);
                                homeViewModel.insert(exercise);
                                if(getActivity() != null)
                                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.title_exercise_saved), Snackbar.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
        });

}
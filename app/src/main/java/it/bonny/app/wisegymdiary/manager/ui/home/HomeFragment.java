package it.bonny.app.wisegymdiary.manager.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisegymdiary.NewEditExerciseActivity;
import it.bonny.app.wisegymdiary.NewEditWorkoutDay;
import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.component.BottomSheetWorkoutDay;
import it.bonny.app.wisegymdiary.manager.MainActivity;
import it.bonny.app.wisegymdiary.util.BottomSheetClickListener;
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
    private MaterialButton btnAddWorkoutDay, btnAddExercise;

    private MaterialCardView btnWorkoutDaySelected;
    private MaterialButton showTransactionListBtn;
    private ConstraintLayout containerRecyclerView;
    private TextView nameWorkoutDaySelected;

    private BottomSheetWorkoutDay bottomSheetWorkoutDay;

    Animation slide_down, slide_up;

    private WorkoutDay workoutDaySelected = new WorkoutDay();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showWelcomeAlert();

        final TextView textView = binding.nameWorkoutPlan;
        initElements(root.getContext());

        Observer<List<Exercise>> exerciseObserver = new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                if(exercises != null && exercises.size() > 0) {
                    containerExerciseEmpty.setVisibility(View.GONE);
                    containerRecyclerView.setVisibility(View.VISIBLE);
                    btnAddExercise.setVisibility(View.VISIBLE);

                    exerciseHomePageAdapter.updateExerciseList(exercises);
                }else {
                    containerExerciseEmpty.setVisibility(View.VISIBLE);
                    containerRecyclerView.setVisibility(View.GONE);
                    btnAddExercise.setVisibility(View.GONE);
                }
            }
        };

        Observer<List<WorkoutDay>> routineObserver = routines -> {
            if(routines != null && routines.size() > 0) {
                containerRoutineEmpty.setVisibility(View.GONE);
                btnWorkoutDaySelected.setVisibility(View.VISIBLE);
                showTransactionListBtn.setVisibility(View.VISIBLE);
                //containerRecyclerView.setVisibility(View.VISIBLE);
                //btnAddWorkoutDay = binding.btnAddWorkoutDay;
                //btnAddWorkoutDay.setOnClickListener(view -> callNewWorkoutDay(root));
                //workoutDayAdapter.updateUserList(routines);
                workoutDaySelected.copy(routines.get(0));
                nameWorkoutDaySelected.setText(workoutDaySelected.getName());
                homeViewModel.getExercise(workoutDaySelected.getId()).observe(getViewLifecycleOwner(), exerciseObserver);
            }else {
                containerRoutineEmpty.setVisibility(View.VISIBLE);
                btnWorkoutDaySelected.setVisibility(View.GONE);
                showTransactionListBtn.setVisibility(View.GONE);
                //containerRecyclerView.setVisibility(View.GONE);
                btnNewRoutine.setOnClickListener(view -> callNewWorkoutDay(root));
            }
        };

        Observer<WorkoutPlan> workoutPlanObserver = workoutPlan -> {
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

                homeViewModel.getWorkoutDay(workoutPlan.getId()).observe(getViewLifecycleOwner(), routineObserver);
            }else {
                containerSettings.setVisibility(View.INVISIBLE);
                containerWorkoutPlanEmpty.setVisibility(View.VISIBLE);
                containerWorkoutPlan.setVisibility(View.GONE);
            }
        };

        homeViewModel.getWorkoutPlan().observe(getViewLifecycleOwner(), workoutPlanObserver);

        btnOptionsWorkoutPlan.setOnClickListener(view -> {

        });

        btnAddExercise.setOnClickListener(v -> callNewExercise(root));

        btnNewExerciseEmpty.setOnClickListener(v -> callNewExercise(root));

        btnWorkoutDaySelected.setOnClickListener(v -> {
            bottomSheetWorkoutDay = new BottomSheetWorkoutDay(workoutPlanApp.getId(), workoutDaySelected.getId(), getContext(), HomeFragment.this);
            bottomSheetWorkoutDay.show(getParentFragmentManager(), "CHANGE_ACCOUNT");
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
        exerciseHomePageAdapter = new ExerciseHomePageAdapter(context);
        setAdapter();
    }

    void setAdapter() {
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewExercise.setHasFixedSize(true);
        recyclerViewExercise.setItemAnimator(new DefaultItemAnimator());
        recyclerViewExercise.setAdapter(exerciseHomePageAdapter);
    }

    private void callNewWorkoutDay(View root) {
        Intent intent = new Intent(root.getContext(), NewEditWorkoutDay.class);
        intent.putExtra("idWorkoutPlan", workoutPlanApp.getId());
        intent.putExtra("newFlag", true);
        root.getContext().startActivity(intent);
    }

    private void callNewExercise(View root) {
        Intent intent = new Intent(root.getContext(), NewEditExerciseActivity.class);
        intent.putExtra("idWorkoutDay", workoutDaySelected.getId());
        intent.putExtra("newFlag", true);
        root.getContext().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

    }

    @Override
    public void onItemClick(long idElement) {
        Toast.makeText(getContext(), "" + idElement, Toast.LENGTH_SHORT).show();
    }
}
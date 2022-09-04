package it.bonny.app.wisegymdiary.manager.ui.home;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import it.bonny.app.wisegymdiary.NewEditWorkoutDay;
import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.component.WorkoutDayAdapter;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private ConstraintLayout containerSettings, containerWorkoutPlanEmpty, containerWorkoutPlan, containerRoutineEmpty;
    private TextView nameWorkoutPlan, infoWeekWorkoutPlan;
    private WorkoutPlan workoutPlanApp;
    private MaterialButton btnNewRoutine, btnOptionsWorkoutPlan;
    private WorkoutDayAdapter workoutDayAdapter;
    private RecyclerView recyclerViewWorkoutDay;
    private MaterialButton btnAddWorkoutDay;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showWelcomeAlert();

        final TextView textView = binding.nameWorkoutPlan;
        initElements(root.getContext());

        Observer<List<WorkoutDay>> routineObserver = routines -> {
            if(routines != null && routines.size() > 0) {
                containerRoutineEmpty.setVisibility(View.GONE);
                //btnAddWorkoutDay = binding.btnAddWorkoutDay;
                //btnAddWorkoutDay.setOnClickListener(view -> callNewWorkoutDay(root));
                workoutDayAdapter.updateUserList(routines);
            }else {
                containerRoutineEmpty.setVisibility(View.VISIBLE);
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

        containerSettings = binding.containerSettings;
        containerWorkoutPlanEmpty = binding.containerWorkoutPlanEmpty;
        containerWorkoutPlan = binding.containerWorkoutPlan;
        nameWorkoutPlan = binding.nameWorkoutPlan;
        infoWeekWorkoutPlan = binding.infoWeekWorkoutPlan;
        containerRoutineEmpty = binding.containerRoutineEmpty;

        btnNewRoutine = binding.btnNewRoutine;
        btnOptionsWorkoutPlan = binding.btnOptionsWorkoutPlan;

        recyclerViewWorkoutDay = binding.recyclerView;
        workoutDayAdapter = new WorkoutDayAdapter(context);
        setAdapter();
    }

    void setAdapter() {
        recyclerViewWorkoutDay.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewWorkoutDay.setHasFixedSize(true);
        recyclerViewWorkoutDay.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWorkoutDay.setAdapter(workoutDayAdapter);
    }

    private void callNewWorkoutDay(View root) {
        Intent intent = new Intent(root.getContext(), NewEditWorkoutDay.class);
        intent.putExtra("idWorkoutPlan", workoutPlanApp.getId());
        intent.putExtra("newFlag", true);
        root.getContext().startActivity(intent);
    }

}
package it.bonny.app.wisegymdiary.manager.ui.exercise;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.component.ExerciseRecyclerViewAdapter;
import it.bonny.app.wisegymdiary.databinding.FragmentExerciseBinding;
import it.bonny.app.wisegymdiary.manager.DetailWorkoutPlanActivity;
import it.bonny.app.wisegymdiary.util.WorkoutPlanOnCLickItemCheckbox;

public class ExerciseFragment extends Fragment {

    private FragmentExerciseBinding binding;
    private ExerciseViewModel viewModel;

    private EditText searchViewText;
    private ExerciseRecyclerViewAdapter exerciseRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initElement();

        searchViewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewModel.getExerciseList().observe(getViewLifecycleOwner(), new Observer<List<ExerciseBean>>() {
            @Override
            public void onChanged(List<ExerciseBean> exerciseBeans) {
                exerciseRecyclerViewAdapter.updateExerciseList(exerciseBeans);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initElement() {
        searchViewText = binding.searchViewText;

        WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox = new WorkoutPlanOnCLickItemCheckbox() {
            @Override
            public void recyclerViewItemClick(long idElement) {
                Toast.makeText(getContext(), "Id --> " + idElement, Toast.LENGTH_SHORT).show();
            }
        };

        RecyclerView recyclerViewExercise = binding.recyclerViewExercise;
        exerciseRecyclerViewAdapter = new ExerciseRecyclerViewAdapter(getActivity(), workoutPlanOnCLickItemCheckbox);
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewExercise.setHasFixedSize(true);
        recyclerViewExercise.setItemAnimator(new DefaultItemAnimator());
        recyclerViewExercise.setAdapter(exerciseRecyclerViewAdapter);
    }

}
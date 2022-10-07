package it.bonny.app.wisegymdiary.manager.ui.workout_plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.component.GridViewWorkoutPlanAdapter;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.databinding.FragmentWorkoutPlanBinding;
import it.bonny.app.wisegymdiary.manager.NewEditWorkoutPlanActivity;
import it.bonny.app.wisegymdiary.util.App;
import it.bonny.app.wisegymdiary.util.ValueFlagBean;
import it.bonny.app.wisegymdiary.util.WorkoutPlanOnCLickItemCheckbox;

public class WorkoutPlanFragment extends Fragment {

    private FragmentWorkoutPlanBinding binding;
    private WorkoutPlanViewModel workoutPlanViewModel;

    private TextView textWorkoutPlansListEmpty;
    private MaterialToolbar materialToolbar;
    private LinearLayout containerListEnd, containerListNotEnd;
    private GridViewWorkoutPlanAdapter workoutPlanEndAdapter, workoutPlanAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       workoutPlanViewModel = new ViewModelProvider(this).get(WorkoutPlanViewModel.class);

        binding = FragmentWorkoutPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        iniElement();

        materialToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.btn_add_generic_menu) {
                Intent intent = new Intent(getActivity(), NewEditWorkoutPlanActivity.class);
                intent.putExtra("idWorkoutPlan", 0);
                startActivity(intent);
                return true;
            }
            return false;
        });

        workoutPlanViewModel.getWorkoutPlanNotEndList().observe(getViewLifecycleOwner(), workoutPlanBeans -> {
            if(workoutPlanBeans == null || workoutPlanBeans.size() <= 0)
                containerListNotEnd.setVisibility(View.GONE);
            else
                containerListNotEnd.setVisibility(View.VISIBLE);
            workoutPlanAdapter.updateWorkoutPlanList(workoutPlanBeans);
        });

        workoutPlanViewModel.getWorkoutPlanEndList().observe(getViewLifecycleOwner(), workoutPlanBeans -> {
            if(workoutPlanBeans == null || workoutPlanBeans.size() <= 0)
                containerListEnd.setVisibility(View.GONE);
            else
                containerListEnd.setVisibility(View.VISIBLE);
            workoutPlanEndAdapter.updateWorkoutPlanList(workoutPlanBeans);
        });

        workoutPlanViewModel.getCountWorkoutPlanByIsEnd().observe(getViewLifecycleOwner(), count -> {
            if(count > 0) {
                textWorkoutPlansListEmpty.setVisibility(View.GONE);
            }else {
                textWorkoutPlansListEmpty.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    private void iniElement() {
        materialToolbar = binding.materialToolbar;
        containerListEnd = binding.containerListEnd;
        containerListNotEnd = binding.containerListNotEnd;
        textWorkoutPlansListEmpty = binding.textWorkoutPlansListEmpty;

        WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox = new WorkoutPlanOnCLickItemCheckbox() {
            @Override
            public void recyclerViewItemClick(long idElement) {
                Toast.makeText(getContext(), "Selected : " + idElement, Toast.LENGTH_SHORT).show();
            }
        };

        RecyclerView gridViewNotEnd = binding.gridViewWorkoutPlan;
        workoutPlanAdapter = new GridViewWorkoutPlanAdapter(getContext(), workoutPlanOnCLickItemCheckbox);
        gridViewNotEnd.setLayoutManager(new GridLayoutManager(getContext(),2));
        gridViewNotEnd.setHasFixedSize(true);
        gridViewNotEnd.setItemAnimator(new DefaultItemAnimator());
        gridViewNotEnd.setAdapter(workoutPlanAdapter);

        RecyclerView gridViewEnd = binding.gridViewWorkoutPlanEnd;
        workoutPlanEndAdapter = new GridViewWorkoutPlanAdapter(getContext(), workoutPlanOnCLickItemCheckbox);
        gridViewEnd.setLayoutManager(new GridLayoutManager(getContext(),2));
        gridViewEnd.setHasFixedSize(true);
        gridViewEnd.setItemAnimator(new DefaultItemAnimator());
        gridViewEnd.setAdapter(workoutPlanEndAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
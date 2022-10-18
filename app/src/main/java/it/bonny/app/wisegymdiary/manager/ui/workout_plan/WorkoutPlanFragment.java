package it.bonny.app.wisegymdiary.manager.ui.workout_plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.component.GridViewWorkoutPlanAdapter;
import it.bonny.app.wisegymdiary.databinding.FragmentWorkoutPlanBinding;
import it.bonny.app.wisegymdiary.manager.DetailWorkoutPlanActivity;
import it.bonny.app.wisegymdiary.manager.NewEditWorkoutPlanActivity;
import it.bonny.app.wisegymdiary.util.RecyclerViewOnClickItem;

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
                //if(getActivity() != null)
                    //getActivity().overridePendingTransition(R.anim.slide_up, R.anim.fade_out);

                return true;
            }
            return false;
        });

        workoutPlanViewModel.getWorkoutPlanNotEndList().observe(getViewLifecycleOwner(), workoutPlanBeans -> {
            if(workoutPlanBeans == null || workoutPlanBeans.size() <= 0)
                containerListNotEnd.setVisibility(View.GONE);
            else {
                containerListNotEnd.setVisibility(View.VISIBLE);
                textWorkoutPlansListEmpty.setVisibility(View.GONE);
            }
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

        RecyclerViewOnClickItem recyclerViewOnClickItem = new RecyclerViewOnClickItem() {
            @Override
            public void recyclerViewItemClick(long idElement) {
                Intent intent = new Intent(getActivity(), DetailWorkoutPlanActivity.class);
                intent.putExtra("idWorkoutPlan", idElement);
                startActivity(intent);
            }
        };

        RecyclerView gridViewNotEnd = binding.gridViewWorkoutPlan;
        workoutPlanAdapter = new GridViewWorkoutPlanAdapter(getContext(), recyclerViewOnClickItem);
        gridViewNotEnd.setLayoutManager(new GridLayoutManager(getContext(),2));
        gridViewNotEnd.setHasFixedSize(true);
        gridViewNotEnd.setItemAnimator(new DefaultItemAnimator());
        gridViewNotEnd.setAdapter(workoutPlanAdapter);

        RecyclerView gridViewEnd = binding.gridViewWorkoutPlanEnd;
        workoutPlanEndAdapter = new GridViewWorkoutPlanAdapter(getContext(), recyclerViewOnClickItem);
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
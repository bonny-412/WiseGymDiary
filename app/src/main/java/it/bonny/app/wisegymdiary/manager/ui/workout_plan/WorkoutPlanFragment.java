package it.bonny.app.wisegymdiary.manager.ui.workout_plan;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import it.bonny.app.wisegymdiary.databinding.FragmentDashboardBinding;
import it.bonny.app.wisegymdiary.databinding.FragmentWorkoutPlanBinding;
import it.bonny.app.wisegymdiary.manager.ui.dashboard.DashboardViewModel;

public class WorkoutPlanFragment extends Fragment {

    private FragmentWorkoutPlanBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WorkoutPlanViewModel workoutPlanViewModel = new ViewModelProvider(this).get(WorkoutPlanViewModel.class);

        binding = FragmentWorkoutPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package it.bonny.app.wisegymdiary.component;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import it.bonny.app.wisegymdiary.bean.WorkoutBean;

public class HomeFragmentRoutineAdapter extends FragmentStateAdapter {
    private final List<WorkoutBean> workoutBeanList;

    public HomeFragmentRoutineAdapter(AppCompatActivity appCompatActivity, List<WorkoutBean> workoutBeanList) {
        super(appCompatActivity);
        this.workoutBeanList = workoutBeanList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = new HomeFragmentRoutineObject();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putLong(HomeFragmentRoutineObject.ARG_OBJECT, workoutBeanList.get(position).getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return (workoutBeanList != null) ? workoutBeanList.size() : 0;
    }
}
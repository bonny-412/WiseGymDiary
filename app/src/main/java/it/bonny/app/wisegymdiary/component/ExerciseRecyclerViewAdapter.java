package it.bonny.app.wisegymdiary.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.CategoryMuscleBean;
import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.dao.CategoryMuscleDAO;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.util.WorkoutPlanOnCLickItemCheckbox;

public class ExerciseRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseRecyclerViewAdapter.RecyclerViewHolder> {

    private final Activity mActivity;
    private List<ExerciseBean> exerciseList;
    private final Utility utility = new Utility();
    private final WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox;

    public ExerciseRecyclerViewAdapter(Activity mActivity, WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox) {
        this.exerciseList = new ArrayList<>();
        this.mActivity = mActivity;
        this.workoutPlanOnCLickItemCheckbox = workoutPlanOnCLickItemCheckbox;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateExerciseList(final List<ExerciseBean> exerciseList) {
        this.exerciseList.clear();
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_recycler_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        ExerciseBean exerciseBean = exerciseList.get(holder.getAdapterPosition());

        holder.title.setText(exerciseBean.getName());

        AppDatabase.databaseWriteExecutor.execute(() -> {
            CategoryMuscleBean categoryMuscleBean = AppDatabase.getInstance(mActivity).categoryMuscleDAO().findCategoryMuscleById(exerciseBean.getIdCategoryMuscle());
            mActivity.runOnUiThread(() -> holder.categoryExercise.setText(categoryMuscleBean.getName()));
        });

        holder.constraintClicked.setOnClickListener(v -> workoutPlanOnCLickItemCheckbox.recyclerViewItemClick(exerciseBean.getId()));

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView categoryExercise;
        LinearLayout constraintClicked;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            categoryExercise = itemView.findViewById(R.id.categoryExercise);
            constraintClicked = itemView.findViewById(R.id.constraintClicked);
        }
    }

}

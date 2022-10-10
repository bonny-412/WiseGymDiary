package it.bonny.app.wisegymdiary.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.util.ValueFlagBean;
import it.bonny.app.wisegymdiary.util.WorkoutPlanOnCLickItemCheckbox;

public class GridViewWorkoutPlanAdapter extends RecyclerView.Adapter<GridViewWorkoutPlanAdapter.RecyclerViewHolder> {

    private final Context mContext;
    private List<WorkoutPlanBean> workoutPlanList;
    private final Utility utility = new Utility();
    int selectedPosition = -1;
    private final WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox;

    public GridViewWorkoutPlanAdapter(Context mContext, WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox) {
        this.workoutPlanList = new ArrayList<>();
        this.mContext = mContext;
        this.workoutPlanOnCLickItemCheckbox = workoutPlanOnCLickItemCheckbox;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateWorkoutPlanList(final List<WorkoutPlanBean> workoutPlanList) {
        this.workoutPlanList.clear();
        this.workoutPlanList = workoutPlanList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_plan_grid_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        WorkoutPlanBean workoutPlan = workoutPlanList.get(holder.getAdapterPosition());

        holder.titleWorkoutPlan.setText(workoutPlan.getName());

        String dateWorkoutPlan = mContext.getString(R.string.from) + " "
                + utility.convertStringDateToStringDateView(workoutPlan.getStartDate()) + " "
                + mContext.getString(R.string.to) + " "
                + utility.convertStringDateToStringDateView(workoutPlan.getEndDate());
        holder.dateWorkoutPlan.setText(dateWorkoutPlan);

        holder.constraintClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutPlanOnCLickItemCheckbox.recyclerViewItemClick(workoutPlan.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return workoutPlanList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView titleWorkoutPlan;
        TextView dateWorkoutPlan;
        TextView numSession;
        LinearLayout constraintClicked;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            titleWorkoutPlan = itemView.findViewById(R.id.titleWorkoutPlan);
            dateWorkoutPlan = itemView.findViewById(R.id.dateWorkoutPlan);
            constraintClicked = itemView.findViewById(R.id.constraintClicked);
            numSession = itemView.findViewById(R.id.numSession);
        }
    }

}

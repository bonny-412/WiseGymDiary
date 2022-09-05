package it.bonny.app.wisegymdiary.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;

public class ExerciseHomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Exercise> exerciseList;
    private Context mContext;

    public ExerciseHomePageAdapter(Context context) {
        this.mContext = context;
        this.exerciseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_day_recyclerview, parent, false);
        return new RecyclerViewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Exercise exercise = exerciseList.get(position);
        RecyclerViewViewHolder viewHolder = (RecyclerViewViewHolder) holder;

        /*if(viewHolder.textWorkedMuscle != null) {
            String numTimeDoneTxt = "" + workoutDay.getNumTimeDone();
            viewHolder.textWorkedMuscle.setText(numTimeDoneTxt);
        }*/
        viewHolder.titleWorkoutDay.setText(exercise.getName());
        viewHolder.nameWorkedMuscle.setText(exercise.getWorkedMuscle());

        /*viewHolder.btnOptionsWorkoutDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.btnOptionsWorkoutDay);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_workoutday, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                mContext,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateExerciseList(final List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView textWorkedMuscle;
        TextView titleWorkoutDay;
        TextView nameWorkedMuscle;
        ConstraintLayout mainLayout;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            textWorkedMuscle = itemView.findViewById(R.id.textWorkedMuscle);
            titleWorkoutDay = itemView.findViewById(R.id.titleWorkoutDay);
            nameWorkedMuscle = itemView.findViewById(R.id.nameWorkedMuscle);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_anim);
            mainLayout.setAnimation(animation);
        }
    }

}

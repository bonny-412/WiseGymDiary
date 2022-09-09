package it.bonny.app.wisegymdiary.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickBottomSheetInterface;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickInterface;
import it.bonny.app.wisegymdiary.util.Utility;

public class ExerciseHomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Exercise> exerciseList;
    private final Context mContext;
    private final RecyclerViewClickInterface listener;

    public ExerciseHomePageAdapter(Context context, RecyclerViewClickInterface listener) {
        this.mContext = context;
        this.listener = listener;
        this.exerciseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_recyclerview, parent, false);
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

        viewHolder.layoutList.removeAllViews();
        String[] splitNumSetsReps = exercise.getNumSetsReps().split(Utility.SYMBOL_SPLIT);
        if(splitNumSetsReps.length > 0) {
            for(String s: splitNumSetsReps) {
                String[] split = s.split(":");
                if(split.length > 0) {
                    addView(split[0], split[1], viewHolder.layoutList);
                }
            }
        }

        viewHolder.constraintClicked.setOnClickListener(view -> listener.recyclerViewItemClick(exercise.getId()));

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateExerciseList(final List<Exercise> exerciseList) {
        this.exerciseList.clear();
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView iconExercise;
        TextView titleWorkoutDay;
        TextView nameWorkedMuscle;
        ConstraintLayout mainLayout, constraintClicked;
        LinearLayout layoutList;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            iconExercise = itemView.findViewById(R.id.iconExercise);
            titleWorkoutDay = itemView.findViewById(R.id.titleWorkoutDay);
            nameWorkedMuscle = itemView.findViewById(R.id.nameWorkedMuscle);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            constraintClicked = itemView.findViewById(R.id.constraintClicked);
            layoutList = itemView.findViewById(R.id.layout_list);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Bonny412", "eccomi");
                    int position = getAdapterPosition();
                    listener.recyclerViewItemClick(exerciseList.get(position).getId());
                }
            });*/

            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_anim);
            mainLayout.setAnimation(animation);
        }

    }

    private void addView(String numSets, String numReps, LinearLayout layoutList) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View setsRepsView = inflater.inflate(R.layout.row_set_rep_item_exercise_recyclerview, null, false);

        TextView textViewNumSeries = setsRepsView.findViewById(R.id.numSeries);
        TextView textViewNumReps = setsRepsView.findViewById(R.id.numReps);

        textViewNumSeries.setText(numSets);
        textViewNumReps.setText(numReps);

        layoutList.addView(setsRepsView);
    }

    private void removeView(LinearLayout layoutList, View view) {
        layoutList.removeView(view);
    }


}

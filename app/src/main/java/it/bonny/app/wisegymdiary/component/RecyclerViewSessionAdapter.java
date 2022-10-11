package it.bonny.app.wisegymdiary.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.SessionBean;
import it.bonny.app.wisegymdiary.manager.SessionDetailActivity;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickInterface;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.util.WorkoutPlanOnCLickItemCheckbox;

public class RecyclerViewSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SessionBean> sessionBeanList;
    private final Context mContext;
    private final WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox;
    private final Utility utility = new Utility();

    public RecyclerViewSessionAdapter(Context context, WorkoutPlanOnCLickItemCheckbox workoutPlanOnCLickItemCheckbox) {
        this.mContext = context;
        this.workoutPlanOnCLickItemCheckbox = workoutPlanOnCLickItemCheckbox;
        this.sessionBeanList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session_recyclerview, parent, false);
        return new RecyclerViewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SessionBean sessionBean = sessionBeanList.get(position);
        RecyclerViewViewHolder viewHolder = (RecyclerViewViewHolder) holder;

        /*if(viewHolder.textWorkedMuscle != null) {
            String numTimeDoneTxt = "" + workoutDay.getNumTimeDone();
            viewHolder.textWorkedMuscle.setText(numTimeDoneTxt);
        }*/

        viewHolder.colorSession.setBackgroundTintList(ContextCompat.getColorStateList(mContext, utility.getColorByPosition(sessionBean.getColor())));
        viewHolder.labelSession.setText(sessionBean.getLabel());
        viewHolder.titleSession.setText(sessionBean.getName());
        viewHolder.nameWorkedMuscle.setText(sessionBean.getWorkedMuscle());

        /*String[] splitNumSetsReps = exercise.getNumSetsReps().split(Utility.SYMBOL_SPLIT);
        if(splitNumSetsReps.length > 0) {
            for(String s: splitNumSetsReps) {
                String[] split = s.split(Utility.SYMBOL_SPLIT_BETWEEN_SET_REP);
                if(split.length > 0) {
                    String numSet = split[0];
                    String numReps = split[1];

                    if(numReps.contains(Utility.SYMBOL_SPLIT_BETWEEN_REPS)) {
                        String[] splitNumReps = numReps.split(Utility.SYMBOL_SPLIT_BETWEEN_REPS);
                        addView(numSet, splitNumReps[0], splitNumReps[1], viewHolder.layoutList);
                    }else {
                        addView(numSet, numReps, null, viewHolder.layoutList);
                    }
                }
            }
        }*/

        viewHolder.constraintClicked.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SessionDetailActivity.class);
            intent.putExtra("idWorkoutDay", sessionBean.getId());
            mContext.startActivity(intent);
        });

        viewHolder.constraintClicked.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.colorSession);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_routine_options, popupMenu.getMenu());
            popupMenu.setForceShowIcon(true);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.edit) {
                    workoutPlanOnCLickItemCheckbox.recyclerViewItemClick(sessionBean.getId());
                }else if(menuItem.getItemId() == R.id.delete) {
                    workoutPlanOnCLickItemCheckbox.recyclerViewItemClick(sessionBean.getId());
                }
                popupMenu.dismiss();
                return true;
            });

            popupMenu.show();
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return sessionBeanList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSessionList(final List<SessionBean> sessionBeanList) {
        this.sessionBeanList.clear();
        this.sessionBeanList = sessionBeanList;
        notifyDataSetChanged();
    }



    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        LinearLayout colorSession;
        TextView labelSession;
        TextView titleSession;
        TextView nameWorkedMuscle;
        ConstraintLayout constraintClicked;
        MaterialCardView mainLayout;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            colorSession = itemView.findViewById(R.id.colorSession);
            labelSession = itemView.findViewById(R.id.labelSession);
            titleSession = itemView.findViewById(R.id.titleWorkoutDay);
            nameWorkedMuscle = itemView.findViewById(R.id.nameWorkedMuscle);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            constraintClicked = itemView.findViewById(R.id.constraintClicked);

            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_anim);
            mainLayout.setAnimation(animation);
        }

    }

    private void addView(String numSets, String numRep, String numRep1, LinearLayout layoutList) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View setsRepsView = inflater.inflate(R.layout.row_set_rep_item_exercise_recyclerview, null, false);

        TextView textViewNumSeries = setsRepsView.findViewById(R.id.numSeries);
        TextView textViewNumReps = setsRepsView.findViewById(R.id.numReps);

        String numReps = numRep;
        if(numRep1 != null)
            numReps += "/" + numRep1;

        textViewNumSeries.setText(numSets);
        textViewNumReps.setText(numReps);

        layoutList.addView(setsRepsView);
    }

    private void removeView(LinearLayout layoutList, View view) {
        layoutList.removeView(view);
    }

    public void removeItem(int position) {
        sessionBeanList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(SessionBean item, int position) {
        sessionBeanList.add(position, item);
        notifyItemInserted(position);
    }

    public List<SessionBean> getData() {
        return sessionBeanList;
    }

}

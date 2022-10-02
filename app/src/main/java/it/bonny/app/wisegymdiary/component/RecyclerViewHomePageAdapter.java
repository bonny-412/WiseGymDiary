package it.bonny.app.wisegymdiary.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.NewEditWorkoutDay;
import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.Session;
import it.bonny.app.wisegymdiary.manager.SessionDetailActivity;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickInterface;
import it.bonny.app.wisegymdiary.util.Utility;

public class RecyclerViewHomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Session> sessionList;
    private final Context mContext;
    private final RecyclerViewClickInterface listener;
    private final Utility utility = new Utility();

    public RecyclerViewHomePageAdapter(Context context, RecyclerViewClickInterface listener) {
        this.mContext = context;
        this.listener = listener;
        this.sessionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session_recyclerview, parent, false);
        return new RecyclerViewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Session session = sessionList.get(position);
        RecyclerViewViewHolder viewHolder = (RecyclerViewViewHolder) holder;

        /*if(viewHolder.textWorkedMuscle != null) {
            String numTimeDoneTxt = "" + workoutDay.getNumTimeDone();
            viewHolder.textWorkedMuscle.setText(numTimeDoneTxt);
        }*/

        viewHolder.colorSession.setBackgroundTintList(ContextCompat.getColorStateList(mContext, utility.getColorByPosition(session.getColor())));
        viewHolder.labelSession.setText(session.getLabel());
        viewHolder.titleSession.setText(session.getName());
        viewHolder.nameWorkedMuscle.setText(session.getWorkedMuscle());

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
            intent.putExtra("idWorkoutDay", session.getId());
            mContext.startActivity(intent);
        });

        viewHolder.constraintClicked.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.colorSession);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_routine_options, popupMenu.getMenu());
            popupMenu.setForceShowIcon(true);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.edit) {
                    listener.recyclerViewItemClick(session.getId(), 0);
                }else if(menuItem.getItemId() == R.id.delete) {
                    listener.recyclerViewItemClick(session.getId(), 1);
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
        return sessionList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSessionList(final List<Session> sessionList) {
        this.sessionList.clear();
        this.sessionList = sessionList;
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
        sessionList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Session item, int position) {
        sessionList.add(position, item);
        notifyItemInserted(position);
    }

    public List<Session> getData() {
        return sessionList;
    }

}

package it.bonny.app.wisegymdiary.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.util.RecyclerViewOnLongClickItem;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.util.RecyclerViewOnClickItem;

public class ExerciseRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseRecyclerViewAdapter.RecyclerViewHolder> {

    private final Activity mActivity;
    private List<ExerciseBean> exerciseList;
    private final Utility utility = new Utility();
    private final RecyclerViewOnClickItem recyclerViewOnClickItem;
    private final RecyclerViewOnLongClickItem recyclerViewOnLongClickItem;

    public ExerciseRecyclerViewAdapter(Activity mActivity, RecyclerViewOnClickItem recyclerViewOnClickItem, RecyclerViewOnLongClickItem recyclerViewOnLongClickItem) {
        this.exerciseList = new ArrayList<>();
        this.mActivity = mActivity;
        this.recyclerViewOnClickItem = recyclerViewOnClickItem;
        this.recyclerViewOnLongClickItem = recyclerViewOnLongClickItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateExerciseList(final List<ExerciseBean> exerciseList) {
        this.exerciseList.clear();
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ExerciseBean> filteredList) {
        exerciseList = filteredList;
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

        holder.categoryExercise.setText(exerciseBean.getIdCategoryMuscle());

        holder.constraintClicked.setOnClickListener(v -> recyclerViewOnClickItem.recyclerViewItemClick(exerciseBean.getId()));

        holder.constraintClicked.setOnLongClickListener(v -> {
            holder.mainLayout.setCardElevation(16f);
            PopupMenu popupMenu = new PopupMenu(mActivity, holder.constraintClicked);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_delete, popupMenu.getMenu());
            popupMenu.setForceShowIcon(true);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.delete) {
                    recyclerViewOnLongClickItem.recyclerViewItemLongClick(exerciseBean.getId());
                }
                popupMenu.dismiss();
                return true;
            });

            popupMenu.show();

            popupMenu.setOnDismissListener(menu -> holder.mainLayout.setCardElevation(0f));
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView categoryExercise;
        LinearLayout constraintClicked;
        MaterialCardView mainLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            categoryExercise = itemView.findViewById(R.id.categoryExercise);
            constraintClicked = itemView.findViewById(R.id.constraintClicked);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}

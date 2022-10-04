package it.bonny.app.wisegymdiary.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.SessionBean;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickBottomSheetInterface;

public class ListWorkoutDayBottomSheetAdapter extends RecyclerView.Adapter<ListWorkoutDayBottomSheetAdapter.ViewHolder> {

    private final List<SessionBean> sessionBeanList;
    private int selectedPosition = -1;
    private final long idElementSelected;
    private final RecyclerViewClickBottomSheetInterface recyclerViewClickBottomSheetInterface;

    public ListWorkoutDayBottomSheetAdapter(long idElementSelected, List<SessionBean> sessionBeanList,
                                            RecyclerViewClickBottomSheetInterface recyclerViewClickBottomSheetInterface) {
        this.sessionBeanList = sessionBeanList;
        this.idElementSelected = idElementSelected;
        this.recyclerViewClickBottomSheetInterface = recyclerViewClickBottomSheetInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_list_workout_day_bottom_sheet, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SessionBean sessionBean = sessionBeanList.get(position);
        holder.itemListWorkoutDayName.setText(sessionBean.getName());

        if(idElementSelected == sessionBean.getId()) {
            holder.radioButtonWorkoutDay.setVisibility(View.VISIBLE);
        }else {
            holder.radioButtonWorkoutDay.setVisibility(View.GONE);
        }

        if(sessionBeanList.size() > 0) {
            holder.itemListWorkoutDay.setOnClickListener(view -> {
                selectedPosition = holder.getAdapterPosition();
                recyclerViewClickBottomSheetInterface.onItemClick(sessionBeanList.get(selectedPosition).getId());
            });
        }
    }

    @Override
    public int getItemCount() {
        return sessionBeanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemListWorkoutDayName;
        private final ImageView radioButtonWorkoutDay;
        private final ConstraintLayout itemListWorkoutDay;

        public ViewHolder(View v) {
            super(v);
            this.radioButtonWorkoutDay = v.findViewById(R.id.radioButtonWorkoutDay);
            this.itemListWorkoutDayName = v.findViewById(R.id.itemListWorkoutDayName);
            this.itemListWorkoutDay = v.findViewById(R.id.itemListWorkoutDay);
        }
    }

    /*private View.OnClickListener onStateChangedListener(final ImageView radioButtonAccount, final int position) {
        return v -> {
            if (sessionBeanList.get(position).getIsSelected() == TypeObjectBean.NO_SELECTED) {
                notifyItemChanged(selectedPosition);
                accountBeanList.get(selectedPosition).setIsSelected(TypeObjectBean.NO_SELECTED);
                db.updateAccountBean(accountBeanList.get(selectedPosition));

                notifyItemChanged(position);
                selectedPosition = position;
                accountBeanList.get(position).setIsSelected(TypeObjectBean.SELECTED);
                radioButtonAccount.setVisibility(View.VISIBLE);
                db.updateAccountBean(accountBeanList.get(position));
            }
        };
    }*/

}

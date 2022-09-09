package it.bonny.app.wisegymdiary.component;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.MuscleBean;

public class ChipMuscleAdapter extends BaseAdapter {

    private final Context context;
    private List<MuscleBean> muscleBeanList;
    public HashMap<Integer, Integer> hashMapSelected;

    public ChipMuscleAdapter(List<MuscleBean> muscleBeanList, Context context) {
        this.context = context;
        this.muscleBeanList = muscleBeanList;
        hashMapSelected = new HashMap<>();
    }

    @Override
    public int getCount() {
        return muscleBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return muscleBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return muscleBeanList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gridview_chip_muscle, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) view.getTag();

        MuscleBean muscleBean = muscleBeanList.get(i);
        viewHolder.textView.setText(muscleBean.getName());

        if(hashMapSelected != null && hashMapSelected.size() > 0 && hashMapSelected.containsKey(i)) {
            viewHolder.textView.setTextColor(context.getColor(R.color.blue_text));
            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
            viewHolder.mainLayout.setBackgroundColor(context.getColor(R.color.blue_background));
        }

        return view;
    }

    public void setMuscleBeanList(List<MuscleBean> muscleBeanList1) {
        this.muscleBeanList = muscleBeanList1;
    }

    private static class ViewHolder {
        TextView textView;
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            textView = view.findViewById(it.bonny.app.wisegymdiary.R.id.nameChipMuscle);
            mainLayout = view.findViewById(R.id.mainLayout);
        }
    }

}

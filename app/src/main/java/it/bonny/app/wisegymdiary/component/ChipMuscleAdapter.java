package it.bonny.app.wisegymdiary.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.CategoryMuscleBean;

public class ChipMuscleAdapter extends BaseAdapter {

    private final Context context;
    private List<CategoryMuscleBean> categoryMuscleBeanList;
    public HashMap<Integer, Integer> hashMapSelected;

    public ChipMuscleAdapter(List<CategoryMuscleBean> categoryMuscleBeanList, Context context) {
        this.context = context;
        this.categoryMuscleBeanList = categoryMuscleBeanList;
        hashMapSelected = new HashMap<>();
    }

    @Override
    public int getCount() {
        return categoryMuscleBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryMuscleBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categoryMuscleBeanList.get(i).getId();
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

        CategoryMuscleBean categoryMuscleBean = categoryMuscleBeanList.get(i);
        viewHolder.textView.setText(categoryMuscleBean.getName());

        if(hashMapSelected != null && hashMapSelected.size() > 0 && hashMapSelected.containsKey(i)) {
            viewHolder.textView.setTextColor(context.getColor(R.color.blue_text));
            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
            viewHolder.mainLayout.setBackgroundColor(context.getColor(R.color.blue_background));
        }

        return view;
    }

    public void setMuscleBeanList(List<CategoryMuscleBean> categoryMuscleBeanList1) {
        this.categoryMuscleBeanList = categoryMuscleBeanList1;
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

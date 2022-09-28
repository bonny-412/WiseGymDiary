package it.bonny.app.wisegymdiary.component;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.util.Utility;

public class GridViewChooseColorAdapter extends BaseAdapter {

    private final Context mContext;
    public HashMap<Integer, Boolean> hashMapSelected;
    private final Utility utility = new Utility();

    public GridViewChooseColorAdapter(Context context) {
        this.mContext = context;
        hashMapSelected = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            hashMapSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void makeAllUnselect(int position) {
        hashMapSelected.put(position, true);
        for (int i = 0; i < hashMapSelected.size(); i++) {
            if (i != position)
                hashMapSelected.put(i, false);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_card_view_choose_color, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.itemColor.setBackgroundTintList(ContextCompat.getColorStateList(mContext, utility.getColorByPosition(i)));

        if (hashMapSelected != null && hashMapSelected.size() > 0 && Boolean.TRUE.equals(hashMapSelected.get(i))) {
            viewHolder.iconColorChosen.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iconColorChosen.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private static class ViewHolder {
        LinearLayout itemColor;
        ImageView iconColorChosen;

        public ViewHolder(View view) {
            itemColor = view.findViewById(R.id.itemColor);
            iconColorChosen = view.findViewById(R.id.iconColorChosen);
        }
    }

}


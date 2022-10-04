package it.bonny.app.wisegymdiary.component;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.SessionBean;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.BottomSheetClickListener;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickBottomSheetInterface;

public class BottomSheetWorkoutDay extends BottomSheetDialogFragment implements RecyclerViewClickBottomSheetInterface {

    private long idElementSelected;
    private ProgressBar progressBar;
    private RecyclerView listViewWorkoutDay;
    private final long idWorkoutPlan;
    private final Context context;

    private final BottomSheetClickListener bottomSheetClickListener;

    public BottomSheetWorkoutDay(long idWorkoutPlan, long idElementSelected, Context context, BottomSheetClickListener bottomSheetClickListener) {
        this.idWorkoutPlan = idWorkoutPlan;
        this.idElementSelected = idElementSelected;
        this.context = context;
        this.bottomSheetClickListener = bottomSheetClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_workout_day, container, false);
        listViewWorkoutDay = view.findViewById(R.id.listViewWorkoutDay);
        progressBar = view.findViewById(R.id.progressBar);
        MaterialButton btnNewWorkoutDay = view.findViewById(R.id.btnNewWorkoutDay);

        btnNewWorkoutDay.setOnClickListener(v -> {
            bottomSheetClickListener.onItemClick(-1);
            dismiss();

        });

        myTask();

        return view;
    }

    private void myTask() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        listViewWorkoutDay.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        service.execute(() -> {
            List<SessionBean> sessionBeans = AppDatabase.getInstance(context).workoutDayDAO().getAllRoutineByIdWorkPlanNoLiveData(idWorkoutPlan);
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    ListWorkoutDayBottomSheetAdapter listWorkoutDayBottomSheetAdapter = new ListWorkoutDayBottomSheetAdapter(idElementSelected, sessionBeans, BottomSheetWorkoutDay.this);
                    listViewWorkoutDay.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    listViewWorkoutDay.setLayoutManager(linearLayoutManager);
                    listViewWorkoutDay.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
                    listViewWorkoutDay.setAdapter(listWorkoutDayBottomSheetAdapter);
                    listViewWorkoutDay.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onItemClick(long idElement) {
        this.idElementSelected = idElement;
        bottomSheetClickListener.onItemClick(idElement);
        dismiss();
    }
}

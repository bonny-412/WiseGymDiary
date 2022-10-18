package it.bonny.app.wisegymdiary.manager.ui.exercise;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisegymdiary.NewEditExerciseActivity;
import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.ExerciseBean;
import it.bonny.app.wisegymdiary.component.ExerciseRecyclerViewAdapter;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.databinding.FragmentExerciseBinding;
import it.bonny.app.wisegymdiary.util.RecyclerViewOnLongClickItem;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.util.RecyclerViewOnClickItem;

public class ExerciseFragment extends Fragment {

    private FragmentExerciseBinding binding;
    private ExerciseViewModel viewModel;

    private EditText searchViewText;
    private ExerciseRecyclerViewAdapter exerciseRecyclerViewAdapter;
    private List<ExerciseBean> beanListApp;
    private TextInputLayout searchView;
    private MaterialToolbar materialToolbar;
    private RecyclerView recyclerViewExercise;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initElement();

        searchViewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        viewModel.getExerciseList().observe(getViewLifecycleOwner(), exerciseBeans -> {
            beanListApp = exerciseBeans;
            exerciseRecyclerViewAdapter.updateExerciseList(exerciseBeans);
        });

        materialToolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.btn_add_generic_menu) {
                callNewEditExercise(0);
                return true;
            }else if(item.getItemId() == R.id.btn_clear_generic_menu) {
                return true;
            }
            return false;
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initElement() {
        searchViewText = binding.searchViewText;

        RecyclerViewOnClickItem recyclerViewOnClickItem = this::callNewEditExercise;

        RecyclerViewOnLongClickItem recyclerViewOnLongClickItem = this::showAlertDelete;

        searchView = binding.searchView;
        materialToolbar = binding.materialToolbar;

        recyclerViewExercise = binding.recyclerViewExercise;
        exerciseRecyclerViewAdapter = new ExerciseRecyclerViewAdapter(getActivity(), recyclerViewOnClickItem, recyclerViewOnLongClickItem);
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewExercise.setHasFixedSize(true);
        recyclerViewExercise.setItemAnimator(new DefaultItemAnimator());
        recyclerViewExercise.setAdapter(exerciseRecyclerViewAdapter);
    }

    private void filter(String text) {
        ArrayList<ExerciseBean> filteredList = new ArrayList<>();

        if(beanListApp != null && beanListApp.size() > 0) {
            for (ExerciseBean item : beanListApp) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        exerciseRecyclerViewAdapter.filterList(filteredList);
    }

    private void callNewEditExercise(long id) {
        Intent intent = new Intent(getActivity(), NewEditExerciseActivity.class);
        intent.putExtra("idExercise", id);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data != null) {
                    long id = data.getLongExtra("id", 0);
                    String name = data.getStringExtra("name");
                    String idCategoryMuscle = data.getStringExtra("idCategoryMuscle");
                    String idCategoryExercise = data.getStringExtra("idCategoryExercise");
                    String note = data.getStringExtra("note");

                    ExerciseBean exerciseBean;
                    if(id == 0) {
                        exerciseBean = new ExerciseBean(name, note, idCategoryMuscle, idCategoryExercise, 0);
                        viewModel.insert(exerciseBean);
                    }else {
                        exerciseBean = new ExerciseBean(id, name, note, idCategoryMuscle, idCategoryExercise, 0);
                        viewModel.update(exerciseBean);
                    }

                    Utility utility = new Utility();
                    if(getActivity() != null && getContext() != null)
                        utility.createSnackbar(getString(R.string.saved), getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getContext());
                }
            }
        }
    });

    private void showAlertDelete(long id) {
        if(getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View viewInfoDialog = View.inflate(getActivity(), R.layout.alert_delete, null);
            builder.setCancelable(false);
            builder.setView(viewInfoDialog);
            MaterialButton btnCancel = viewInfoDialog.findViewById(R.id.btnCancel);
            MaterialButton btnDelete = viewInfoDialog.findViewById(R.id.btnDelete);
            TextView textAlert = viewInfoDialog.findViewById(R.id.textAlert);

            textAlert.setText(R.string.text_delete_exercise);
            final AlertDialog dialog = builder.create();
            if(dialog.getWindow() != null && getContext() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.transparent)));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            btnCancel.setOnClickListener(v -> dialog.dismiss());
            btnDelete.setOnClickListener(v -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    ExerciseBean exerciseBeanDelete = AppDatabase.getInstance(getContext()).exerciseDAO().findExerciseById(id);
                    if(getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            viewModel.delete(exerciseBeanDelete);
                            dialog.dismiss();
                            if(getContext() != null)
                                Utility.createSnackbar(getString(R.string.deleted), binding.getRoot(), getContext());
                        });
                    }
                });
            });
            dialog.show();
        }
    }

}
package it.bonny.app.wisegymdiary.manager.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.atomic.AtomicBoolean;

import it.bonny.app.wisegymdiary.NewEditExerciseActivity;
import it.bonny.app.wisegymdiary.NewEditWorkoutDay;
import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.Exercise;
import it.bonny.app.wisegymdiary.bean.WorkoutDay;
import it.bonny.app.wisegymdiary.component.BottomSheetWorkoutDay;
import it.bonny.app.wisegymdiary.database.AppDatabase;
import it.bonny.app.wisegymdiary.util.BottomSheetClickListener;
import it.bonny.app.wisegymdiary.util.SwipeToDeleteCallback;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.component.ExerciseHomePageAdapter;
import it.bonny.app.wisegymdiary.bean.WorkoutPlan;
import it.bonny.app.wisegymdiary.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements BottomSheetClickListener {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private ConstraintLayout containerSettings, containerWorkoutPlanEmpty, containerWorkoutPlan,
            containerRoutineEmpty, containerExerciseEmpty, containerButtonActionExerciseRoutine;
    private TextView nameWorkoutPlan, infoWeekWorkoutPlan;
    private WorkoutPlan workoutPlanApp;
    private MaterialButton btnNewRoutine, btnOptionsWorkoutPlan, btnNewExerciseEmpty, btnOptionsRoutine, btnPlay;
    private ExerciseHomePageAdapter exerciseHomePageAdapter;
    private RecyclerView recyclerViewExercise;
    private MaterialButton btnAddExercise;
    private MaterialCardView btnWorkoutDaySelected;
    private MaterialButton showTransactionListBtn;
    private ConstraintLayout containerRecyclerView;
    private TextView nameWorkoutDaySelected;
    private BottomSheetWorkoutDay bottomSheetWorkoutDay;
    Animation slide_down, slide_up;
    private final Utility utility = new Utility();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showWelcomeAlert();

        final TextView textView = binding.nameWorkoutPlan;
        initElements(root.getContext());

        homeViewModel.getWorkoutPlan().observe(getViewLifecycleOwner(), workoutPlan -> {
            if(workoutPlan != null) {
                textView.setText(workoutPlan.getName());
                containerSettings.setVisibility(View.VISIBLE);
                containerWorkoutPlanEmpty.setVisibility(View.GONE);
                containerWorkoutPlan.setVisibility(View.VISIBLE);
                nameWorkoutPlan.setText(workoutPlan.getName());
                String numWeek = "2";
                String weekStrFinal = getString(R.string.week) + " " + numWeek + " " + getString(R.string.of) + " " + workoutPlan.getNumWeek();
                Spannable spannableString = new SpannableString(weekStrFinal);
                spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(binding.getRoot().getContext(), R.color.secondary)),
                        getString(R.string.week).length() + 1, getString(R.string.week).length() + 1 + numWeek.length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                infoWeekWorkoutPlan.setText(spannableString);

                workoutPlanApp.copy(workoutPlan);

                homeViewModel.getWorkoutDayList(workoutPlan.getId()).observe(getViewLifecycleOwner(), workoutDays -> {
                    if(workoutDays != null && workoutDays.size() > 0) {
                        customUIRoutineIsEmpty(false);
                        homeViewModel.setWorkoutDaySelected().setValue(workoutDays.get(0));
                        homeViewModel.getWorkoutDaySelected().observe(getViewLifecycleOwner(), this::changedWorkoutDay);
                    }else {
                        customUIRoutineIsEmpty(true);
                        btnNewRoutine.setOnClickListener(view -> callNewWorkoutDay(0));
                    }
                });
            }else {
                containerSettings.setVisibility(View.INVISIBLE);
                containerWorkoutPlanEmpty.setVisibility(View.VISIBLE);
                containerWorkoutPlan.setVisibility(View.GONE);
            }
        });

        btnOptionsWorkoutPlan.setOnClickListener(view -> {

        });

        btnAddExercise.setOnClickListener(v -> callNewExercise(0));

        //btnNewExerciseEmpty.setOnClickListener(v -> callNewExercise(0));

        btnWorkoutDaySelected.setOnClickListener(v -> {
            if(homeViewModel.getWorkoutDaySelected() != null && homeViewModel.getWorkoutDaySelected().getValue() != null) {
                bottomSheetWorkoutDay = new BottomSheetWorkoutDay(workoutPlanApp.getId(), homeViewModel.getWorkoutDaySelected().getValue().getId(), getContext(), HomeFragment.this);
                bottomSheetWorkoutDay.show(getParentFragmentManager(), "CHANGE_ACCOUNT");
            }
        });

        recyclerViewExercise.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && containerButtonActionExerciseRoutine.getVisibility() == View.VISIBLE) {
                    containerButtonActionExerciseRoutine.animate()
                            .translationY(containerButtonActionExerciseRoutine.getHeight())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    containerButtonActionExerciseRoutine.setVisibility(View.GONE);
                                }
                            });
                } else if (dy < 0 && containerButtonActionExerciseRoutine.getVisibility() != View.VISIBLE) {
                    containerButtonActionExerciseRoutine.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    containerButtonActionExerciseRoutine.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        });

        btnOptionsRoutine.setOnClickListener(v -> {
            if(getContext() != null) {
                PopupMenu popupMenu = new PopupMenu(getContext(), btnOptionsRoutine);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_routine_options, popupMenu.getMenu());
                popupMenu.setForceShowIcon(true);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if(menuItem.getItemId() == R.id.edit) {
                        if(homeViewModel.getWorkoutDaySelected() != null && homeViewModel.getWorkoutDaySelected().getValue() != null)
                            callNewWorkoutDay(homeViewModel.getWorkoutDaySelected().getValue().getId());
                        else
                            callNewWorkoutDay(0);
                    }else if(menuItem.getItemId() == R.id.delete) {
                        showAlertDeleteWorkoutDay();
                    }
                    return true;
                });

                popupMenu.show();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        enableSwipeToDeleteAndUndo();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showWelcomeAlert() {
        boolean firstStart = false;
        SharedPreferences prefs;
        if(binding.getRoot().getContext() != null) {
            try {
                prefs = binding.getRoot().getContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE);
                firstStart = prefs.getBoolean("firstStart", true);
            }catch (Exception e) {
                //TODO: Firebase
                Log.e("HOME_FRAGMENT", e.toString());
            }
        }
        if(firstStart) {
            SharedPreferences.Editor editor = binding.getRoot().getContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE).edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }
    }

    private void initElements(Context context) {
        workoutPlanApp = new WorkoutPlan();

        slide_down = AnimationUtils.loadAnimation(context,
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);

        containerSettings = binding.containerSettings;
        containerWorkoutPlanEmpty = binding.containerWorkoutPlanEmpty;
        containerWorkoutPlan = binding.containerWorkoutPlan;
        nameWorkoutPlan = binding.nameWorkoutPlan;
        infoWeekWorkoutPlan = binding.infoWeekWorkoutPlan;
        containerRoutineEmpty = binding.containerRoutineEmpty;

        btnNewRoutine = binding.btnNewRoutine;
        btnOptionsWorkoutPlan = binding.btnOptionsWorkoutPlan;

        containerExerciseEmpty = binding.containerExerciseEmpty;
        btnAddExercise = binding.btnAddExercise;
        //btnNewExerciseEmpty = binding.btnNewExerciseEmpty;

        btnWorkoutDaySelected = binding.btnWorkoutDaySelected;
        showTransactionListBtn = binding.showTransactionListBtn;
        containerRecyclerView = binding.containerRecyclerView;
        nameWorkoutDaySelected = binding.nameWorkoutDaySelected;
        containerButtonActionExerciseRoutine = binding.containerButtonActionExerciseRoutine;
        btnOptionsRoutine = binding.btnOptionsRoutine;
        btnPlay = binding.btnPlay;

        recyclerViewExercise = binding.recyclerView;
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerViewExercise.setLayoutManager(manager);
        exerciseHomePageAdapter = new ExerciseHomePageAdapter(context, this::callNewExercise);
        setAdapter();
    }

    private void customUIRoutineIsEmpty(boolean isEmpty) {
        if(isEmpty) {
            containerRoutineEmpty.setVisibility(View.VISIBLE);
            btnWorkoutDaySelected.setVisibility(View.GONE);
            showTransactionListBtn.setVisibility(View.GONE);
        }else {
            containerRoutineEmpty.setVisibility(View.GONE);
            btnWorkoutDaySelected.setVisibility(View.VISIBLE);
            showTransactionListBtn.setVisibility(View.VISIBLE);
        }
    }

    private void customUIExerciseIsEmpty(boolean isEmpty) {
        if(isEmpty) {
            containerExerciseEmpty.setVisibility(View.VISIBLE);
            containerRecyclerView.setVisibility(View.GONE);
            btnPlay.setClickable(false);
            if(getContext() != null)
                btnPlay.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.secondary_text)));
        }else {
            containerExerciseEmpty.setVisibility(View.GONE);
            containerRecyclerView.setVisibility(View.VISIBLE);
            btnPlay.setClickable(true);
            if(getContext() != null)
                btnPlay.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.primary)));
        }
    }

    void setAdapter() {
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewExercise.setHasFixedSize(true);
        recyclerViewExercise.setItemAnimator(new DefaultItemAnimator());
        recyclerViewExercise.setAdapter(exerciseHomePageAdapter);
    }

    private void callNewWorkoutDay(long idWorkoutDay) {
        Intent intent = new Intent(getActivity(), NewEditWorkoutDay.class);
        intent.putExtra("idWorkoutPlan", workoutPlanApp.getId());
        if(idWorkoutDay > 0)
            intent.putExtra("idWorkoutDay", idWorkoutDay);
        someActivityResultLauncher.launch(intent);
    }

    private void callNewExercise(long idExercise) {
        if(homeViewModel.getWorkoutDaySelected() != null && homeViewModel.getWorkoutDaySelected().getValue() != null) {
            Intent intent = new Intent(getActivity(), NewEditExerciseActivity.class);
            intent.putExtra("idWorkoutDay", homeViewModel.getWorkoutDaySelected().getValue().getId());
            if(idExercise > 0)
                intent.putExtra("idExercise", idExercise);
            someActivityResultLauncher.launch(intent);
        }
    }

    //Bottom sheet
    @Override
    public void onItemClick(long idElement) {
        if(idElement > 0) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                WorkoutDay workoutDay = AppDatabase.getInstance(getContext()).workoutDayDAO().getWorkoutDayById(idElement);
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        homeViewModel.setWorkoutDaySelected().setValue(workoutDay);
                        homeViewModel.getWorkoutDaySelected().observe(getViewLifecycleOwner(), this::changedWorkoutDay);
                    });
                }
            });
        }else {
            callNewWorkoutDay(0);
        }
    }

    public void changedWorkoutDay(WorkoutDay workoutDay) {
        nameWorkoutDaySelected.setText(workoutDay.getName());
        homeViewModel.getExerciseList(workoutDay.getId()).observe(getViewLifecycleOwner(), exercises -> {
            if(exercises != null && exercises.size() > 0) {
                customUIExerciseIsEmpty(false);
                exerciseHomePageAdapter.updateExerciseList(exercises);
            }else {
                customUIExerciseIsEmpty(true);
            }
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        if(getContext() != null) {
            SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                    final int position = viewHolder.getAdapterPosition();
                    final Exercise item = exerciseHomePageAdapter.getData().get(position);

                    AtomicBoolean clickedButtonAction = new AtomicBoolean(false);

                    exerciseHomePageAdapter.removeItem(position);


                    Snackbar snackbar = Snackbar.make(containerRecyclerView, getString(R.string.snackbar_text_exercise_removed), Snackbar.LENGTH_LONG);
                    snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            if(!clickedButtonAction.get())
                                homeViewModel.delete(item);
                        }
                    });
                    snackbar.setAction(getString(R.string.snackbar_text_button_undo), view -> {
                        clickedButtonAction.set(true);
                        exerciseHomePageAdapter.restoreItem(item, position);
                        recyclerViewExercise.scrollToPosition(position);
                    });

                    if(getContext() != null)
                        snackbar.setActionTextColor(getContext().getColor(R.color.blue_text));
                    snackbar.setBackgroundTint(getContext().getColor(R.color.blue_background));
                    snackbar.setTextColor(getContext().getColor(R.color.secondary_text));
                    snackbar.show();

                }
            };

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchhelper.attachToRecyclerView(recyclerViewExercise);
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null) {
                            int page = data.getIntExtra("page", Utility.ADD_WORKOUT_DAY);
                            if(page == Utility.ADD_WORKOUT_DAY) {
                                long id = data.getLongExtra(Utility.EXTRA_WORKOUT_DAY_ID, 0);
                                String name = data.getStringExtra(Utility.EXTRA_WORKOUT_DAY_NAME);
                                Integer numTimeDone = data.getIntExtra(Utility.EXTRA_WORKOUT_DAY_NUM_TIME_DONE, 0);
                                long idWorkoutPlan = data.getLongExtra(Utility.EXTRA_WORKOUT_DAY_ID_WORK_PLAIN, 0);
                                String workedMuscle = data.getStringExtra(Utility.EXTRA_WORKOUT_DAY_WORKED_MUSCLE);
                                String note = data.getStringExtra(Utility.EXTRA_WORKOUT_DAY_NOTE);

                                WorkoutDay workoutDay;
                                if(id == 0) {
                                    workoutDay = new WorkoutDay(name, numTimeDone, idWorkoutPlan, workedMuscle, note);
                                    homeViewModel.insert(workoutDay);
                                    homeViewModel.setWorkoutDaySelected().setValue(workoutDay);
                                    homeViewModel.getWorkoutDaySelected().observe(getViewLifecycleOwner(), HomeFragment.this::changedWorkoutDay);
                                }else {
                                    workoutDay = new WorkoutDay(id, name, numTimeDone, idWorkoutPlan, workedMuscle, note);
                                    homeViewModel.update(workoutDay);
                                }

                                if(getActivity() != null && getContext() != null)
                                    utility.createSnackbar(getString(R.string.title_workout_day_saved), getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getContext());
                            }else if(page == Utility.ADD_EXERCISE) {
                                long id = data.getLongExtra(Utility.EXTRA_EXERCISE_ID, 0);
                                String name = data.getStringExtra(Utility.EXTRA_EXERCISE_NAME);
                                long idWorkoutPlan = data.getLongExtra(Utility.EXTRA_EXERCISE_ID_WORK_DAY,0);
                                String note = data.getStringExtra(Utility.EXTRA_EXERCISE_NOTE);
                                String restTime = data.getStringExtra(Utility.EXTRA_EXERCISE_REST_TIME);
                                String numSetsReps = data.getStringExtra(Utility.EXTRA_EXERCISE_NUM_SETS_REPS);
                                String workedMuscle = data.getStringExtra(Utility.EXTRA_EXERCISE_WORKED_MUSCLE);
                                Exercise exercise;
                                if(id == 0) {
                                    exercise = new Exercise(name, idWorkoutPlan, note, restTime, numSetsReps, workedMuscle);
                                    homeViewModel.insert(exercise);
                                }else {
                                    exercise = new Exercise(id, name, idWorkoutPlan, note, restTime, numSetsReps, workedMuscle);
                                    homeViewModel.update(exercise);
                                }

                                if(getActivity() != null && getContext() != null)
                                    utility.createSnackbar(getString(R.string.title_exercise_saved), getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getContext());
                            }
                        }

                    }
                }
        });

    private void showAlertDeleteWorkoutDay() {
        if(getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View viewInfoDialog = View.inflate(getActivity(), R.layout.alert_delete, null);
            builder.setCancelable(false);
            builder.setView(viewInfoDialog);
            MaterialButton btnCancel = viewInfoDialog.findViewById(R.id.btnCancel);
            MaterialButton btnDelete = viewInfoDialog.findViewById(R.id.btnDelete);
            TextView textAlert = viewInfoDialog.findViewById(R.id.textAlert);
            String messageAlert = getString(R.string.text_alert_delete);
            if(homeViewModel.getWorkoutDaySelected().getValue() != null)
                messageAlert += " \"" + homeViewModel.getWorkoutDaySelected().getValue().getName() + "\" ";
            messageAlert += "?";

            textAlert.setText(messageAlert);
            final AlertDialog dialog = builder.create();
            if(dialog.getWindow() != null && getContext() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.transparent)));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            btnCancel.setOnClickListener(v -> {
                dialog.dismiss();
            });
            btnDelete.setOnClickListener(v -> {
                homeViewModel.delete(homeViewModel.getWorkoutDaySelected().getValue());
                dialog.dismiss();
                utility.createSnackbar(getString(R.string.title_deleted_workout_day), getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getContext());
            });
            dialog.show();
        }
    }

}
package it.bonny.app.wisegymdiary.manager.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import it.bonny.app.wisegymdiary.NewEditWorkoutDay;
import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.bean.SessionBean;
import it.bonny.app.wisegymdiary.manager.NewEditWorkoutPlanActivity;
import it.bonny.app.wisegymdiary.util.RecyclerViewClickInterface;
import it.bonny.app.wisegymdiary.util.SwipeToDeleteCallback;
import it.bonny.app.wisegymdiary.util.Utility;
import it.bonny.app.wisegymdiary.component.RecyclerViewHomePageAdapter;
import it.bonny.app.wisegymdiary.bean.WorkoutPlanBean;
import it.bonny.app.wisegymdiary.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private WorkoutPlanBean workoutPlanBeanApp;
    //New UI Design
    private ProgressBar progressBar1;
    private TextView titleInfoWeekWorkoutPlan, titleMyWorkoutPlan;
    private LinearLayout containerCurrentWeek;
    private LinearLayout containerSessionEmpty, containerSession;
    private RecyclerViewHomePageAdapter recyclerViewHomePageAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showWelcomeAlert();

        initElements(root.getContext());

        createCurrentWeek();

           homeViewModel.getWorkoutPlan().observe(getViewLifecycleOwner(), workoutPlan -> {
               if(workoutPlan == null) {
                   progressBar1.setVisibility(View.GONE);
                   //Devo creare una scheda
                   containerSessionEmpty.setVisibility(View.VISIBLE);
                   titleInfoWeekWorkoutPlan.setVisibility(View.GONE);
                   containerSession.setVisibility(View.GONE);
               }else {
                   progressBar1.setVisibility(View.GONE);
                   //Scheda di allenamento creata e selezionata
                   containerSessionEmpty.setVisibility(View.GONE);
                   titleInfoWeekWorkoutPlan.setVisibility(View.VISIBLE);
                   containerSession.setVisibility(View.VISIBLE);

                   titleMyWorkoutPlan.setText(workoutPlan.getName());

                   String numWeek = "1";
                   String weekStrFinal = getString(R.string.week) + " " + numWeek + " " + getString(R.string.of) + " " + workoutPlan.getNumWeek();
                   Spannable spannableCountWeek = new SpannableString(weekStrFinal);
                   spannableCountWeek.setSpan(new ForegroundColorSpan(ContextCompat.getColor(binding.getRoot().getContext(), R.color.primary)),
                           getString(R.string.week).length() + 1, getString(R.string.week).length() + 1 + numWeek.length() + 1,
                           Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                   titleInfoWeekWorkoutPlan.setText(spannableCountWeek);

                   homeViewModel.getWorkoutDayList(workoutPlan.getId()).observe(getViewLifecycleOwner(), workoutDays -> {
                       customUISessionIsEmpty(workoutDays == null || workoutDays.size() <= 0);
                       recyclerViewHomePageAdapter.updateSessionList(workoutDays);
                   });

               }
            });

        containerSessionEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNewEditWorkoutPlan();
            }
        });

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && btnNewSession.getVisibility() == View.VISIBLE) {
                    btnNewSession.animate()
                            .translationY(btnNewSession.getHeight())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    btnNewSession.setVisibility(View.GONE);
                                }
                            });
                } else if (dy < 0 && btnNewSession.getVisibility() != View.VISIBLE) {
                    btnNewSession.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    btnNewSession.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        });*/

        /*btnOptionsSession.setOnClickListener(v -> {
            if(getContext() != null) {
                PopupMenu popupMenu = new PopupMenu(getContext(), btnOptionsSession);
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
        });*/

        //enableSwipeToDeleteAndUndo();

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
        workoutPlanBeanApp = new WorkoutPlanBean();

        TextView titlePage = binding.titlePage;
        String titlePageTxt = getString(R.string.title_page_home_fragment_one) + " " + getString(R.string.title_page_home_fragment_two);
        Spannable spannableString = new SpannableString(titlePageTxt);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(binding.getRoot().getContext(), R.color.primary)),
                getString(R.string.title_page_home_fragment_one).length() + 1, getString(R.string.title_page_home_fragment_one).length() + 1 + getString(R.string.title_page_home_fragment_two).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titlePage.setText(spannableString);

        containerCurrentWeek = binding.containerCurrentWeek;
        containerSessionEmpty = binding.containerSessionEmpty;
        containerSession = binding.containerSession;
        titleInfoWeekWorkoutPlan = binding.titleInfoWeekWorkoutPlan;
        titleMyWorkoutPlan = binding.titleMyWorkoutPlan;

        progressBar1 = binding.progressBar1;

        titleMyWorkoutPlan = binding.titleMyWorkoutPlan;

        recyclerView = binding.recyclerView;
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerViewHomePageAdapter = new RecyclerViewHomePageAdapter(context, new RecyclerViewClickInterface() {
            @Override
            public void recyclerViewItemClick(long idElement, int typeButton) {
                if(typeButton == 0) {
                    //Edit
                    callNewEditWorkoutDay(idElement);
                }else {
                    //Delete

                }
            }
        });
        setAdapter();
    }

    private void customUISessionIsEmpty(boolean isEmpty) {
        progressBar1.setVisibility(View.GONE);
        /*if(isEmpty) {
            containerSessionEmpty.setVisibility(View.VISIBLE);
            containerSession.setVisibility(View.GONE);
        }else {*/
        containerSessionEmpty.setVisibility(View.GONE);
        containerSession.setVisibility(View.VISIBLE);
        //}
    }

    void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewHomePageAdapter);
    }

    private void callNewEditWorkoutDay() {
        callNewEditWorkoutDay(0);
    }

    private void callNewEditWorkoutDay(long idWorkoutDay) {
        Intent intent = new Intent(getActivity(), NewEditWorkoutDay.class);
        intent.putExtra("idWorkoutPlan", workoutPlanBeanApp.getId());
        if(idWorkoutDay > 0)
            intent.putExtra("idWorkoutDay", idWorkoutDay);
        //someActivityResultLauncher.launch(intent);
    }

    private void callNewEditWorkoutPlan() {
        Intent intent = new Intent(getActivity(), NewEditWorkoutPlanActivity.class);
        intent.putExtra("idWorkoutPlan", 0);
        startActivity(intent);
    }

    //Bottom sheet
   /* @Override
    public void onItemClick(long idElement) {
        if(idElement > 0) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                SessionBean workoutDay = AppDatabase.getInstance(getContext()).workoutDayDAO().getWorkoutDayById(idElement);
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        setIdWorkoutDaySelected(idElement);
                        homeViewModel.setWorkoutDaySelected().setValue(workoutDay);
                        homeViewModel.getWorkoutDaySelected().observe(getViewLifecycleOwner(), this::changedWorkoutDay);
                    });
                }
            });
        }else {
            callNewWorkoutDay(0);
        }
    }*/

    private void enableSwipeToDeleteAndUndo() {
        if(getContext() != null) {
            SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                    final int position = viewHolder.getAdapterPosition();
                    final SessionBean item = recyclerViewHomePageAdapter.getData().get(position);

                    AtomicBoolean clickedButtonAction = new AtomicBoolean(false);

                    recyclerViewHomePageAdapter.removeItem(position);

                    Snackbar snackbar = Snackbar.make(containerSession, getString(R.string.snackbar_text_exercise_removed), Snackbar.LENGTH_LONG);
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
                        recyclerViewHomePageAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    });

                    if(getContext() != null)
                        snackbar.setActionTextColor(getContext().getColor(R.color.blue_text));
                    snackbar.setBackgroundTint(getContext().getColor(R.color.blue_background));
                    snackbar.setTextColor(getContext().getColor(R.color.secondary_text));
                    snackbar.show();

                }
            };

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchhelper.attachToRecyclerView(recyclerView);
        }
    }

    /*ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null) {
                            int page = data.getIntExtra("page", Utility.ADD_SESSION);
                            if(page == Utility.ADD_SESSION) {
                                long id = data.getLongExtra(Utility.EXTRA_SESSION_ID, 0);
                                String name = data.getStringExtra(Utility.EXTRA_SESSION_NAME);
                                Integer numTimeDone = data.getIntExtra(Utility.EXTRA_SESSION_NUM_TIME_DONE, 0);
                                long idWorkoutPlan = data.getLongExtra(Utility.EXTRA_SESSION_ID_WORK_PLAIN, 0);
                                String workedMuscle = data.getStringExtra(Utility.EXTRA_SESSION_WORKED_MUSCLE);
                                String note = data.getStringExtra(Utility.EXTRA_SESSION_NOTE);
                                String label = data.getStringExtra(Utility.EXTRA_SESSION_LABEL);
                                int color = data.getIntExtra(Utility.EXTRA_SESSION_COLOR, 0);

                                SessionBean sessionBean;
                                if(id == 0) {
                                    sessionBean = new SessionBean(name, numTimeDone, idWorkoutPlan, workedMuscle, note, label, color);
                                    homeViewModel.insert(sessionBean);

                                    //setIdWorkoutDaySelected(idInsert);
                                    //getIdWorkoutDaySelected();
                                }else {
                                    sessionBean = new SessionBean(id, name, numTimeDone, idWorkoutPlan, workedMuscle, note, label, color);
                                    homeViewModel.update(sessionBean);
                                }

                                if(getActivity() != null && getContext() != null)
                                    utility.createSnackbar(getString(R.string.title_workout_day_saved), getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getContext());
                            }
                        }
                    }
                }
            });*/

   /* private void showAlertDeleteWorkoutDay() {
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
            btnCancel.setOnClickListener(v -> dialog.dismiss());
            btnDelete.setOnClickListener(v -> {
                homeViewModel.delete(homeViewModel.getWorkoutDaySelected().getValue());
                dialog.dismiss();
                if(getContext() != null)
                    utility.createSnackbar(getString(R.string.title_deleted_workout_day), getActivity().getWindow().getDecorView().findViewById(android.R.id.content), getContext());
            });
            dialog.show();
        }
    }*/

    private void createCurrentWeek() {
        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatNameDay = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat formatNumDay = new SimpleDateFormat("dd", Locale.getDefault());

        int[] days = new int[] {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY};

        for (int day : days) {
            boolean isToday = false;
            myCal.set(Calendar.DAY_OF_WEEK, day);

            if (myCal.get(Calendar.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
                isToday = true;

            String nameDay = Utility.capitalizeFirstLetterString(formatNameDay.format(myCal.getTime()));

            String numDay = formatNumDay.format(myCal.getTime());
            addCurrentWeekView(numDay, nameDay, isToday);
        }
    }

    private void addCurrentWeekView(String numDay, String nameDay, boolean isToday) {
        final View currentWeekView = getLayoutInflater().inflate(R.layout.view_item_current_week, null, false);

        View containerCurrentWeekDay = currentWeekView.findViewById(R.id.containerCurrentWeekDay);
        //MaterialCardView containerNumDay = containerCurrentWeekDay.findViewById(R.id.containerNumDay);
        TextView numDayView = currentWeekView.findViewById(R.id.numDay);
        TextView nameDayView = currentWeekView.findViewById(R.id.nameDay);

        numDayView.setText(numDay);
        nameDayView.setText(nameDay);

        if(isToday) {
            //containerNumDay.setCardBackgroundColor(ContextCompat.getColorStateList(getContext(), R.color.primary));
            nameDayView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.primary_text));
            numDayView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.primary_text));
        }


        //btnDelete.setOnClickListener(v -> removeView(setsRepsView));


        containerCurrentWeek.addView(containerCurrentWeekDay);
    }

}
package it.bonny.app.wisegymdiary.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import it.bonny.app.wisegymdiary.R;
import it.bonny.app.wisegymdiary.component.GridViewChooseColorAdapter;

public class Utility {
    public static  final String PREFS_NAME_FILE = "GymDiaryFileConf";
    public static final String SYMBOL_SPLIT = "&_&";
    public static final String SYMBOL_SPLIT_BETWEEN_SET_REP = ":";
    public static final String SYMBOL_SPLIT_BETWEEN_REPS = "_";
    public static final String SYMBOL_MAX = "Max";

    public static final int ADD_SESSION = 1;
    public static final int EDIT_SESSION = 2;
    public static final String EXTRA_SESSION_ID = "id";
    public static final String EXTRA_SESSION_NAME = "name";
    public static final String EXTRA_SESSION_NUM_TIME_DONE = "numTimeDone";
    public static final String EXTRA_SESSION_ID_WORK_PLAIN = "idWorkPlain";
    public static final String EXTRA_SESSION_WORKED_MUSCLE = "workedMuscle";
    public static final String EXTRA_SESSION_NOTE = "note";
    public static final String EXTRA_SESSION_LABEL = "label";
    public static final String EXTRA_SESSION_COLOR = "color";

    public static final int ADD_EXERCISE = 3;
    public static final int DELETE_EXERCISE = 4;
    public static final String EXTRA_EXERCISE_ID = "id";
    public static final String EXTRA_EXERCISE_NAME = "name";
    public static final String EXTRA_EXERCISE_ID_WORK_DAY = "idWorkDay";
    public static final String EXTRA_EXERCISE_NOTE = "note";
    public static final String EXTRA_EXERCISE_REST_TIME = "restTime";
    public static final String EXTRA_EXERCISE_NUM_SETS_REPS = "numSetsReps";
    public static final String EXTRA_EXERCISE_WORKED_MUSCLE = "workedMuscle";

    public static String getDayMonthCurrent() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
        return capitalizeWord(simpleDateFormat.format(Calendar.getInstance().getTime()));
    }

    public static String capitalizeWord(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for(String w: words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizeWord.toString();
    }

    public static Map<Integer, String> getAffectedMuscles(Activity activity) {
        Map<Integer, String> map = new HashMap<>();
        /*map.put(1, activity.getString(R.string.muscle_abductor));
        map.put(2, activity.getString(R.string.muscle_abs));
        map.put(3, activity.getString(R.string.muscle_adductors));
        map.put(4, activity.getString(R.string.muscle_backs));
        map.put(5, activity.getString(R.string.muscle_bicep));
        map.put(6, activity.getString(R.string.muscle_chest));
        map.put(7, activity.getString(R.string.muscle_femoral));
        map.put(8, activity.getString(R.string.muscle_forearm));
        map.put(9, activity.getString(R.string.muscle_glute));
        map.put(10, activity.getString(R.string.muscle_quadricep));
        map.put(11, activity.getString(R.string.muscle_shoulders));
        map.put(12, activity.getString(R.string.muscle_triceps));*/

        return map;
    }

    public static void createSnackbar(String message, View view, Context context) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(context.getColor(R.color.background_card));
        snackbar.setTextColor(context.getColor(R.color.primary_text));
        snackbar.show();
    }

    public int getColorByPosition(int i) {
        if(i == 1)
            return R.color.color2;
        else if(i == 2)
            return R.color.color3;
        else if(i == 3)
           return R.color.color4;
        else if(i == 4)
            return R.color.color5;
        else if(i == 5)
            return R.color.color6;
        else if(i == 6)
            return R.color.color7;
        else if(i == 7)
            return R.color.color8;
        else if(i == 8)
            return R.color.color9;
        else if(i == 9)
            return R.color.color10;
        else if(i == 10)
            return R.color.color11;
        else
            return R.color.primary;
    }

    public static String capitalizeFirstLetterString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static SimpleDateFormat getDateForm() {
       return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    }

    public static SimpleDateFormat getDateFormView() {
        return new SimpleDateFormat("dd MMM", Locale.getDefault());
    }

    public String convertStringDateToStringDateView(String d1) {
        String str;
        Date date;
        try {
            date = getDateForm().parse(d1);
        }catch (Exception exception) {
            date = new Date();
        }

        if(date == null) {
            date = new Date();
        }
        str = getDateFormView().format(date);

        return str;
    }

    public int getCountWeeks(String d1, String d2)  {
        int numWeeks = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            Date start = sdf.parse(d1);
            Date end = sdf.parse(d2);

            if(start != null && end != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Date firstOfWeek = Date.from(start.toInstant().atZone(ZoneId.of("Europe/Paris")).toLocalDate().with(
                            TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                    ).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date lastOfWeek = Date.from(end.toInstant().atZone(ZoneId.of("Europe/Paris")).toLocalDate().with(
                            TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)
                    ).atStartOfDay(ZoneId.systemDefault()).toInstant());

                    numWeeks = countWeeks(firstOfWeek, lastOfWeek);
                }else {
                    numWeeks = countWeeks(start, end);
                }
            }
        }catch (Exception ignored) {}
        
        return numWeeks;
    }

    private int countWeeks(Date start, Date end) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(start);

        int weeks = 0;
        while(cal.getTime().before(end)) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }

        return weeks;
    }

}


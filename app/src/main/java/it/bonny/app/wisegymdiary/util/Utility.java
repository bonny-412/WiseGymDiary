package it.bonny.app.wisegymdiary.util;

import android.app.Activity;
import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.bonny.app.wisegymdiary.R;

public class Utility {
    public static  final String PREFS_NAME_FILE = "GymDiaryFileConf";
    public static final String SYMBOL_SPLIT = "&_&";

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
        map.put(1, activity.getString(R.string.muscle_abductor));
        map.put(2, activity.getString(R.string.muscle_abs));
        map.put(3, activity.getString(R.string.muscle_backs));
        map.put(4, activity.getString(R.string.muscle_bicep));
        map.put(5, activity.getString(R.string.muscle_chest));
        map.put(6, activity.getString(R.string.muscle_forearm));
        map.put(7, activity.getString(R.string.muscle_glute));
        map.put(8, activity.getString(R.string.muscle_quadricep));
        map.put(9, activity.getString(R.string.muscle_shoulders));
        map.put(10, activity.getString(R.string.muscle_triceps));

        return map;
    }


}


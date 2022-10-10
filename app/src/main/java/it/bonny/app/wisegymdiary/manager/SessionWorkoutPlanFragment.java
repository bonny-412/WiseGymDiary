package it.bonny.app.wisegymdiary.manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import it.bonny.app.wisegymdiary.R;

public class SessionWorkoutPlanFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    private int[] COLOR_MAP = {
            R.color.color2, R.color.color5, R.color.color4, R.color.color10, R.color.color3
    };
    public SessionWorkoutPlanFragment() {
        // Required empty public constructor
    }
    public static SessionWorkoutPlanFragment newInstance(Integer counter) {
        SessionWorkoutPlanFragment fragment = new SessionWorkoutPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_fragment_session_workout_plan, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
        EditText nameSession = view.findViewById(R.id.nameSession);
        nameSession.setText("Fragment No " + (counter+1));
    }
}

package it.bonny.app.wisegymdiary.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.bonny.app.wisegymdiary.R;

public class HomeFragmentRoutineObject extends Fragment {
    public static final String ARG_OBJECT = "idRoutine";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routine_object_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        assert args != null;
        String txt = "CIAOOOOOO " + args.getLong(ARG_OBJECT);
        //textView.setText(txt);
    }
}

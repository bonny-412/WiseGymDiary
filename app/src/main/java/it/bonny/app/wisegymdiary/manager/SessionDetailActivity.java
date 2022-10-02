package it.bonny.app.wisegymdiary.manager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import it.bonny.app.wisegymdiary.manager.ui.home.HomeViewModel;

public class SessionDetailActivity extends AppCompatActivity {
    private SessionDetailViewModel sessionDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionDetailViewModel = new ViewModelProvider(this).get(SessionDetailViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(it.bonny.app.wisegymdiary.R.layout.activity_session_detail);

        long idWorkoutDay = getIntent().getLongExtra("idWorkoutDay", 0);

    }
}
package it.bonny.app.wisegymdiary.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import it.bonny.app.wisegymdiary.R;

public class NewEditWorkoutPlanActivity extends AppCompatActivity {

    private TextView titlePage, subTitlePage;
   // private EditText name, startDate, endDate, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_workout_plan);

        long idWorkoutPlan = getIntent().getLongExtra("idWorkoutPlan", 0);

        initElement();

        if(idWorkoutPlan == 0) {
            titlePage.setText(getString(R.string.title_page_new_edit_workout_plan));
            subTitlePage.setText(getString(R.string.sub_title_page_new_edit_workout_plan));
        }else {
            titlePage.setText(getString(R.string.title_page_new_edit_workout_plan_edit));
            subTitlePage.setText(getString(R.string.sub_title_page_new_edit_workout_plan_edit));
        }


    }

    private void initElement() {
        titlePage = findViewById(R.id.titlePage);
        subTitlePage = findViewById(R.id.subTitlePage);
       /* name = findViewById(R.id.name);
        startDate = findViewById(R.id.dateStart);
        endDate = findViewById(R.id.dateEnd);
        note = findViewById(R.id.note);*/
    }

}
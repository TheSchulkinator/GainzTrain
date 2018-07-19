package theschulk.com.gainztrain.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.R;

public class AddMuscleGroupActivity extends AppCompatActivity {

    String intentWorkoutString;

    @BindView(R.id.text_view_select_back) TextView mSelectBack;
    @BindView(R.id.text_view_select_bicep) TextView mSelectBicep;
    @BindView(R.id.text_view_select_chest) TextView mSelectChest;
    @BindView(R.id.text_view_select_legs) TextView mSelectLegs;
    @BindView(R.id.text_view_select_shoulder) TextView mSelectShoulders;
    @BindView(R.id.text_view_select_tricep) TextView mSelectTricep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_muscle_group);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_COMPONENT_NAME)) {
            intentWorkoutString = intent.getStringExtra(Intent.EXTRA_COMPONENT_NAME);
        } else {
            intentWorkoutString = null;
        }

        //Setup onClickHandlers
        mSelectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseActivity(getString(R.string.back));
            }
        });

        mSelectBicep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseActivity(getString(R.string.bicep));
            }
        });

        mSelectChest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseActivity(getString(R.string.chest));
            }
        });

        mSelectLegs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseActivity(getString(R.string.leg));
            }
        });

        mSelectShoulders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseActivity(getString(R.string.shoulder));
            }
        });

        mSelectTricep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseActivity(getString(R.string.tricep));
            }
        });

    }z

    public void addExerciseActivity(String muscleGroupFilter){
        Intent intent = new Intent(getApplicationContext(), AddExerciseActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, muscleGroupFilter);

        if(intentWorkoutString != null){
            intent.putExtra(Intent.EXTRA_COMPONENT_NAME, intentWorkoutString);
        }

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch(item.getItemId()){
            case R.id.menu_body_tracker:
                intent = new Intent(this, BodyTrackerActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_custom_workouts:
                intent = new Intent(this, CustomWorkoutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_home:
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;
            case  R.id.menu_history:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

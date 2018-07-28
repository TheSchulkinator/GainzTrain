package theschulk.com.gainztrain.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Adapters.CursorRecyclerViewAdapter;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class CustomWorkoutDetailActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.CursorOnClickHandler {

    @BindView(R.id.custom_workout_detail_recycler_view) RecyclerView customWorkoutDetailRecyclerView;
    @BindView(R.id.custom_exercise_detail_tile) TextView customDetailTitle;

    String customWorkoutDetail;
    CursorRecyclerViewAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_COMPONENT_NAME)) {
            customWorkoutDetail = intent.getStringExtra(Intent.EXTRA_COMPONENT_NAME);
        } else {
            customWorkoutDetail = null;
        }

        //set click handler for FAB
        FloatingActionButton customWorkoutDetailFab = findViewById(R.id.add_custom_workout_fab);
        customWorkoutDetailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, AddMuscleGroupActivity.class);
                intent.putExtra(Intent.EXTRA_COMPONENT_NAME, customWorkoutDetail);
                startActivity(intent);
            }
        });

        //set up database and adapters
        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        db = dbHelper.getReadableDatabase();
        db = dbHelper.getWritableDatabase();

        adapter = new CursorRecyclerViewAdapter(this);
        customQueryArray();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        customWorkoutDetailRecyclerView.setLayoutManager(layoutManager);
        customWorkoutDetailRecyclerView.setAdapter(adapter);

        customDetailTitle.setText(customWorkoutDetail);
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

        switch (item.getItemId()) {
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
            case R.id.menu_history:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void customQueryArray() {

        String[] selectionArgs = {customWorkoutDetail};
        String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME + "=?";
        Cursor workoutQueryCursor = db.query(false,
                WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);

        if (workoutQueryCursor != null) {
            String[] savedCustomWorkout = new String[workoutQueryCursor.getCount()];
            int counter = 0;
            if (workoutQueryCursor.moveToFirst()) {
                do {
                    String currentExercise = workoutQueryCursor.getString(workoutQueryCursor.getColumnIndex(
                            WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE
                    ));

                    savedCustomWorkout[counter] = currentExercise;
                    counter++;
                }
                while (workoutQueryCursor.moveToNext());
                workoutQueryCursor.close();
            }
            adapter.setData(savedCustomWorkout);
        }

    }

    @Override
    public void onClick(String selectedExercise) {
        // no need for click handler
        //TODO: add long click for delete and modify
    }
}

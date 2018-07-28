package theschulk.com.gainztrain.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Adapters.CursorRecyclerViewAdapter;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class CustomWorkoutActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.CursorOnClickHandler {

    SQLiteDatabase db;
    CursorRecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    @BindView(R.id.add_other_workout)
    EditText editWorkoutText;
    @BindView(R.id.add_workout_recycler_view)
    RecyclerView workoutRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout);
        ButterKnife.bind(this);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        db = dbHelper.getReadableDatabase();
        db = dbHelper.getWritableDatabase();

        adapter = new CursorRecyclerViewAdapter(this);
        queryArray();

        layoutManager = new LinearLayoutManager(this);
        workoutRecyclerView.setLayoutManager(layoutManager);
        workoutRecyclerView.setAdapter(adapter);
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

    public void addWorkoutOnClick(View view) {

        //get string from edit text
        String editTextString = editWorkoutText.getText().toString();

        //add data to exercise table and to muscle group table for exercise to persist in future
        if (editTextString != null && !editTextString.equals("")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME, editTextString);
            db.insert(WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE, null, contentValues);

            queryArray();

        } else {
            Toast toast = Toast.makeText(this, R.string.custom_workout_error, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void onClick(String selectedExercise) {
        Intent intent = new Intent(this, CustomWorkoutDetailActivity.class);
        intent.putExtra(Intent.EXTRA_COMPONENT_NAME, selectedExercise);
        startActivity(intent);
    }

    public void queryArray() {

        String[] columnSelection = {WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME};
        Cursor workoutQueryCursor = db.query(true,
                WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE,
                columnSelection,
                null,
                null,
                null,
                null,
                null,
                null);

        if (workoutQueryCursor != null) {
            String[] savedWorkouts = new String[workoutQueryCursor.getCount()];
            int counter = 0;
            if (workoutQueryCursor.moveToFirst()) {
                do {
                    String currentExercise = workoutQueryCursor.getString(workoutQueryCursor.getColumnIndex(
                            WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME
                    ));

                    savedWorkouts[counter] = currentExercise;
                    counter++;
                }
                while (workoutQueryCursor.moveToNext());
                workoutQueryCursor.close();
            }
            adapter.setData(savedWorkouts);
        }

    }
}

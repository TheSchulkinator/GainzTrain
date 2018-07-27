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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Adapters.CurrentExerciseRecyclerViewAdapter;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class
CurrentExerciseDetail extends AppCompatActivity {

    SQLiteDatabase db;
    String currentExerciseString;
    LinearLayoutManager mLayoutManager;
    LinearLayoutManager previousLayoutManager;

    @BindView(R.id.current_exercise_recycler_view) RecyclerView currentExerciseRecyclerView;
    @BindView(R.id.previous_exercise_recycler_view) RecyclerView previousExerciseRecyclerView;
    @BindView(R.id.current_exercise_title) TextView currentExerciseTitle;
    @BindView(R.id.current_exercise_add_exercise_button) Button addExerciseButton;
    @BindView(R.id.add_workout_reps) EditText addWorkoutReps;
    @BindView(R.id.add_workout_weight) EditText addWorkoutWeight;
    CurrentExerciseRecyclerViewAdapter adapter;
    CurrentExerciseRecyclerViewAdapter previousAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_exercise_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        currentExerciseString = intent.getStringExtra(Intent.EXTRA_TEXT);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this );
        db = dbHelper.getReadableDatabase();
        db = dbHelper.getWritableDatabase();

        //set adapters for recycler views
        adapter = new CurrentExerciseRecyclerViewAdapter(this);
        sendCursorToAdapter();
        previousAdapter = new CurrentExerciseRecyclerViewAdapter(this);
        sendPreviousDataToCursor();

        //Set up Views
        mLayoutManager = new LinearLayoutManager(this);
        currentExerciseRecyclerView.setLayoutManager(mLayoutManager);
        currentExerciseRecyclerView.setAdapter(adapter);


        currentExerciseTitle.setText(currentExerciseString);

        previousLayoutManager = new LinearLayoutManager(this);
        previousExerciseRecyclerView.setLayoutManager(previousLayoutManager);
        previousExerciseRecyclerView.setAdapter(previousAdapter);


    }

    public void addExerciseOnClick(View view){
        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
        String currentDate = dateFormat.format(date);

        //get string from edit text
        String editRepsString = addWorkoutReps.getText().toString();
        String editWeightString = addWorkoutWeight.getText().toString();

        //add data to exercise table and to muscle group table for exercise to persist in future
        if (editRepsString == null || editRepsString.equals("")) {

            Toast toast = Toast.makeText(this, R.string.emptyEditTextToast, Toast.LENGTH_SHORT);
            toast.show();

        } else if(editWeightString == null || editWeightString.equals("")) {
            Toast toast = Toast.makeText(this, R.string.emptyEditTextToast, Toast.LENGTH_SHORT);
            toast.show();
        } else {

            int editRepsInt = Integer.parseInt(editRepsString);
            int editWeightInt = Integer.parseInt(editWeightString);

            ContentValues contentValues = new ContentValues();
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME, currentExerciseString);
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE, currentDate);
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS, editRepsInt);
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WEIGHT, editWeightInt);
            db.insert(WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE, null, contentValues);

            addWorkoutWeight.getText().clear();
            addWorkoutReps.getText().clear();

            sendCursorToAdapter();
        }
    }

    public void sendCursorToAdapter(){
        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
        String[] selectionArgs = {dateFormat.format(date), currentExerciseString};
        String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE + "=? AND " +
                WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME + "=?" +
                " AND " + WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS + " IS NOT NULL";

        //query database and pass to recyclerview adapter
        Cursor cursor = db.query(WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE,
                null ,
                selection,
                selectionArgs,
                null,
                null,
                null);
        adapter.setData(cursor);
    }

    public void sendPreviousDataToCursor(){
        //See if the exercise has been recorded if so find the date in order to filter
        String previousDate = "";

        Cursor dateCursor = db.rawQuery("SELECT * FROM " +
                WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE +
        " WHERE " + WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE +
        " = ( SELECT MAX(" + WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE +") FROM " +
                WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE +
        " WHERE " + WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE + " < (SELECT MAX(" +
                WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE + ") FROM " +
                WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE +
                        " ) ) ;", null );

        if (dateCursor != null) {
            if (dateCursor.moveToFirst()) {
                do {
                    previousDate = dateCursor.getString(dateCursor.getColumnIndex(
                            WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE
                    ));

                }
                while (dateCursor.moveToNext());
                dateCursor.close();
            }
        }

        //if a current date exists get records and pass to recycler view adapter
        if(previousDate != null && previousDate !=""){
            String[] selectionArgs = {previousDate, currentExerciseString};
            String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE + "=? AND " +
                    WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME + "=?" +
                    " AND " + WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS + " IS NOT NULL";

            //query database and pass to recyclerview adapter
            Cursor cursor = db.query(WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE,
                    null ,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
            previousAdapter.setData(cursor);
        }

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

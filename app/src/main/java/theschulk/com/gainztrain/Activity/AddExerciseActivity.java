package theschulk.com.gainztrain.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Adapters.CursorRecyclerViewAdapter;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract.WorkoutEntry;
import theschulk.com.gainztrain.R;

public class AddExerciseActivity extends AppCompatActivity
        implements CursorRecyclerViewAdapter.CursorOnClickHandler{

    private String columnFilterString;
    private CursorRecyclerViewAdapter mCursorRecyclerViewAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    SQLiteDatabase db;
    private String intentWorkoutString;

    //Butterkinfe
    @BindView(R.id.add_exercise_recycler_view) RecyclerView addExerciseRecyclerView;
    @BindView(R.id.send_exercise_button) Button sendExerciseButton;
    @BindView(R.id.add_other_exercise) EditText addOtherExerciseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        columnFilterString = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(intent.hasExtra(Intent.EXTRA_COMPONENT_NAME)) {
            intentWorkoutString = intent.getStringExtra(Intent.EXTRA_COMPONENT_NAME);
        } else {
            intentWorkoutString = null;
        }
        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();

        mCursorRecyclerViewAdapter = new CursorRecyclerViewAdapter(this);

        queryCurrentExercise();

        mLayoutManager = new LinearLayoutManager(this);
        addExerciseRecyclerView.setLayoutManager(mLayoutManager);
        addExerciseRecyclerView.setAdapter(mCursorRecyclerViewAdapter);
    }

    @Override
    public void onClick(String selectedExercise) {
        ContentValues contentValues = new ContentValues();
        if(intentWorkoutString != null){
            contentValues = new ContentValues();
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME, intentWorkoutString);
            contentValues.put(WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE, selectedExercise);
            db.insert(WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE, null, contentValues);
            Intent intent = new Intent(this,CustomWorkoutActivity.class);
            startActivity(intent);
        } else {
            Date date = new Date();
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
            String currentDate = dateFormat.format(date);
            contentValues.put(WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME, selectedExercise);
            contentValues.put(WorkoutEntry.COLUMN_NAME_DATE, currentDate);
            db.insert(WorkoutEntry.WORKOUT_ENTRY_TABLE, null, contentValues);

            toEditActivityIntent(selectedExercise);
        }
    }

    public void sendExerciseOnClick(View view){

        //get date
        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
        String currentDate = dateFormat.format(date);

        //get string from edit text
        String editTextString = addOtherExerciseText.getText().toString();

        //add data to exercise table and to muscle group table for exercise to persist in future
        if (editTextString != null && !editTextString.equals("")) {
            if (intentWorkoutString != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME, intentWorkoutString);
                contentValues.put(WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE, editTextString);
                db.insert(WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE, null, contentValues);

                ContentValues muscleGroupContentValues = new ContentValues();
                muscleGroupContentValues.put(WorkoutEntry.COLUMN_NAME_EXERCISE_NAME, editTextString);
                muscleGroupContentValues.put(WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP, columnFilterString);
                db.insert(WorkoutEntry.MUSCLE_GROUP_TABLE, null, muscleGroupContentValues);

                Intent intent = new Intent(this, CustomWorkoutActivity.class);
                startActivity(intent);
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME, editTextString);
                contentValues.put(WorkoutEntry.COLUMN_NAME_DATE, currentDate);
                db.insert(WorkoutEntry.WORKOUT_ENTRY_TABLE, null, contentValues);

                ContentValues muscleGroupContentValues = new ContentValues();
                muscleGroupContentValues.put(WorkoutEntry.COLUMN_NAME_EXERCISE_NAME, editTextString);
                muscleGroupContentValues.put(WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP, columnFilterString);
                db.insert(WorkoutEntry.MUSCLE_GROUP_TABLE, null, muscleGroupContentValues);

                toEditActivityIntent(editTextString);
            }
            } else{
                Toast toast = Toast.makeText(this, R.string.emptyEditTextToast, Toast.LENGTH_SHORT);
                toast.show();
            }

    }

    public void toEditActivityIntent(String exerciseToEnter){
        Context context = getApplicationContext();
        Intent intent = new Intent(context, EnterWorkoutActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, exerciseToEnter);
        startActivity(intent);
    }

    public void queryCurrentExercise() {
        //query for current records in the database
        String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP
                + "=?";
        String[] selectionArgs = {columnFilterString};
        Cursor workoutQueryCursor = db.query(false,
                WorkoutEntry.MUSCLE_GROUP_TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);

        //set recycler view data to records returned from DB
        String[] exerciseNamesByMuscleGroup = new String[workoutQueryCursor.getCount()];
        int counter = 0;
        if (workoutQueryCursor.moveToFirst()) {
            do {
                String currentExercise = workoutQueryCursor.getString(workoutQueryCursor.getColumnIndex(
                        WorkoutEntry.COLUMN_NAME_EXERCISE_NAME
                ));

                exerciseNamesByMuscleGroup[counter] = currentExercise;
                counter++;
            }
            while (workoutQueryCursor.moveToNext());
            mCursorRecyclerViewAdapter.setData(exerciseNamesByMuscleGroup);

        }
    }
}

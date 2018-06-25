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
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        CursorRecyclerViewAdapter.CursorOnClickHandler{

    public static final int URL_ADD_EXERCISE_LOADER = 2;
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

        mCursorRecyclerViewAdapter = new CursorRecyclerViewAdapter(this);

        getSupportLoaderManager().initLoader(URL_ADD_EXERCISE_LOADER, null, this);

        mLayoutManager = new LinearLayoutManager(this);
        addExerciseRecyclerView.setLayoutManager(mLayoutManager);
        addExerciseRecyclerView.setAdapter(mCursorRecyclerViewAdapter);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        db = dbHelper.getWritableDatabase();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> workoutLoader = loaderManager.getLoader(URL_ADD_EXERCISE_LOADER);
        if(workoutLoader == null){
            loaderManager.
                    initLoader(URL_ADD_EXERCISE_LOADER, null, this).
                    forceLoad();
        }else {
            loaderManager.
                    restartLoader(URL_ADD_EXERCISE_LOADER, null, this).
                    forceLoad();

        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri authorityUri = WorkoutDatabaseContract.WorkoutEntry.BASE_CONTENT_URI;
        Uri pathUri;

        switch (id){
            case URL_ADD_EXERCISE_LOADER:
                String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP
                        + " = ? ";
                String[] selectionArgs = {columnFilterString};
                pathUri = authorityUri.buildUpon().
                        appendPath(WorkoutDatabaseContract.WorkoutEntry.MUSCLE_GROUP_TABLE).
                        build();
                return new CursorLoader(this, pathUri, null, selection, selectionArgs,  null);
           /* case URL_SAVED_WORKOUT_LOADER:
                String[] projection = {WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME};
                pathUri = authorityUri.buildUpon().
                        appendPath(WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE).
                        build();
                return new CursorLoader(this, pathUri, projection, null, null, null);*/
            default:
                return null;


        }
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            switch (loader.getId()) {
                case URL_ADD_EXERCISE_LOADER:
                    String[] exerciseNamesByMuscleGroup = new String[data.getCount()];
                    int counter = 0;
                    if (data.moveToFirst()) {
                        do {
                            String currentExercise = data.getString(data.getColumnIndex(
                                    WorkoutEntry.COLUMN_NAME_EXERCISE_NAME
                            ));

                            exerciseNamesByMuscleGroup[counter] = currentExercise;
                            counter++;
                        }
                        while (data.moveToNext());
                        data.close();
                        mCursorRecyclerViewAdapter.setData(exerciseNamesByMuscleGroup);
                        break;
                    }
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(String selectedExercise) {
        ContentValues contentValues = new ContentValues();
        if(intentWorkoutString != null){
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
}

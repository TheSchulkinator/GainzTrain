package theschulk.com.gainztrain.Activity;

import android.appwidget.AppWidgetManager;
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
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Adapters.CursorRecyclerViewAdapter;
import theschulk.com.gainztrain.BuildConfig;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class HomeActivity extends AppCompatActivity implements
        CursorRecyclerViewAdapter.CursorOnClickHandler {

    CursorRecyclerViewAdapter mCursorRecyclerViewAdapter;
    LinearLayoutManager mLayoutManager;
    private AdView adView;
    Boolean savedWorkoutSelected = false;
    SQLiteDatabase db;

    //Butterknife setup
    @BindView(R.id.prompt_user_add_exercise) TextView promptUserAddExercise;
    @BindView(R.id.home_activity_title) TextView homeActivityTitle;
    @BindView(R.id.home_activity_recycler_view) RecyclerView homeActivityRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();

        mCursorRecyclerViewAdapter = new CursorRecyclerViewAdapter(this);

       currentExerciseQuery();

        //Setup RecyclerView
        homeActivityRecyclerView = findViewById(R.id.home_activity_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        homeActivityRecyclerView.setLayoutManager(mLayoutManager);
        homeActivityRecyclerView.setAdapter(mCursorRecyclerViewAdapter);

        FloatingActionButton addExerciseFab = findViewById(R.id.add_exercise_fab);
        addExerciseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, AddMuscleGroupActivity.class);
                startActivity(intent);
            }
        });

        //Test Api Key api key actually in BuildConfig.
        adView = findViewById(R.id.adView);
        MobileAds.initialize(this, BuildConfig.adMobApiKey);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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

    @Override
    public void onClick(String selectedExercise) {
        if(savedWorkoutSelected){

            String[] selectionArgs = {selectedExercise};
            String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME + "=? AND " +
                    WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE + " IS NOT NULL";
            Cursor workoutQueryCursor = db.query(true,
                    WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE,
                    null,
                    selection,
                    selectionArgs,
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
                                WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE
                        ));

                        savedWorkouts[counter] = currentExercise;
                        counter++;
                    }
                    while (workoutQueryCursor.moveToNext());
                    workoutQueryCursor.close();
                }
                mCursorRecyclerViewAdapter.setData(savedWorkouts);
                savedWorkoutSelected = false;
            }
        }else{
            Intent widgetIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            widgetIntent.putExtra(Intent.EXTRA_TEXT, selectedExercise);
            sendBroadcast(widgetIntent);

            Context context = getApplicationContext();
            Intent intent = new Intent(context, CurrentExerciseDetail.class);
            intent.putExtra(Intent.EXTRA_TEXT, selectedExercise);
            startActivity(intent);
        }

    }

    //create database query methods that do not use cursor loader
    public void currentExerciseQuery(){
        //query the database for current today's exercise records
        String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE + "=?";
        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
        String[] selectionArgs = {dateFormat.format(date)};
        Cursor currentExerciseCursor = db.query(true,
                WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);

        //add records from database query
        if (currentExerciseCursor != null) {
            String[] currentWorkout = new String[currentExerciseCursor.getCount()];
            int counter = 0;
            if (currentExerciseCursor.moveToFirst()) {
                do {
                    String currentExercise = currentExerciseCursor.getString(currentExerciseCursor.getColumnIndex(
                            WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME
                    ));

                    currentWorkout[counter] = currentExercise;
                    counter++;
                }
                while (currentExerciseCursor.moveToNext());
                currentExerciseCursor.close();

                String[] distinctArray = new HashSet<>(Arrays.asList(currentWorkout)).toArray(new String[0]);

                homeActivityTitle.setText(R.string.current_workout);
                mCursorRecyclerViewAdapter.setData(distinctArray);
                homeActivityTitle.setVisibility(View.VISIBLE);
                promptUserAddExercise.setVisibility(View.GONE);
                homeActivityRecyclerView.setVisibility(View.VISIBLE);
            }else {
                customWorkoutQuery();
            }
        }

    }

    public void customWorkoutQuery(){
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

                homeActivityTitle.setText(R.string.saved_workouts);
                mCursorRecyclerViewAdapter.setData(savedWorkouts);
                homeActivityTitle.setVisibility(View.VISIBLE);
                promptUserAddExercise.setVisibility(View.GONE);
                homeActivityRecyclerView.setVisibility(View.VISIBLE);
                savedWorkoutSelected = true;
            } else{
                promptUserAddExercise.setVisibility(View.VISIBLE);
                homeActivityTitle.setVisibility(View.GONE);
                homeActivityRecyclerView.setVisibility(View.GONE);
            }
        }

    }
}

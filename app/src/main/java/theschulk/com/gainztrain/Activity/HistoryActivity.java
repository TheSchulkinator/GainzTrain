package theschulk.com.gainztrain.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    SQLiteDatabase db;
    String[] exercise;
    int[] weight;
    int[] reps;
    String[] id;
    String weightOrReps;

    @BindView(R.id.muscle_group_spinner) Spinner muscleGroupSpinner;
    @BindView(R.id.exercise_spinner) Spinner exerciseSpinner;
    @BindView(R.id.weight_or_reps_spinner) Spinner weightRepsSpinner;
    @BindView(R.id.graph) GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this );
        db = dbHelper.getReadableDatabase();
        db = dbHelper.getWritableDatabase();

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.muscle_group_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
    muscleGroupSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapterWeightOrReps = ArrayAdapter.createFromResource(this,
                R.array.weight_or_reps, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        weightRepsSpinner.setAdapter(adapterWeightOrReps);

    muscleGroupSpinner.setOnItemSelectedListener(this);
    exerciseSpinner.setOnItemSelectedListener(this);
    weightRepsSpinner.setOnItemSelectedListener(this);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

            case R.id.muscle_group_spinner:
                String muscleSpinnerText = parent.getSelectedItem().toString();

                String[] selectionArgs = { muscleSpinnerText};
                String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP + "=?";

                //query database and pass to recyclerview adapter
                Cursor cursor = db.query(WorkoutDatabaseContract.WorkoutEntry.MUSCLE_GROUP_TABLE,
                        null ,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);

                if (cursor != null) {
                    exercise = new String[cursor.getCount()];
                    int counter = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            String currentWeight = cursor.getString(cursor.getColumnIndex(
                                    WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_EXERCISE_NAME
                            ));

                            exercise[counter] = currentWeight;
                            counter++;
                        }
                        while (cursor.moveToNext());
                        cursor.close();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,
                            exercise);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    exerciseSpinner.setAdapter(adapter);

                }


                break;
            case R.id.exercise_spinner:

                String exerciseSpinnerText = parent.getSelectedItem().toString();

                String[] selectionArgsExercise = {exerciseSpinnerText};
                String selectionExercise = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME + "=?" +
                        " AND " + WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS + " IS NOT NULL";

                //query database and pass to recyclerview adapter
                Cursor cursorExercise = db.query(WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE,
                        null ,
                        selectionExercise,
                        selectionArgsExercise,
                        null,
                        null,
                        null);

                if (cursorExercise != null) {
                    weight = new int[cursorExercise.getCount()];
                    reps = new int[cursorExercise.getCount()];
                    int counter = 0;
                    if (cursorExercise.moveToFirst()) {
                        do {
                            String currentWeight = cursorExercise.getString(cursorExercise.getColumnIndex(
                                    WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WEIGHT
                            ));

                            String currentReps = cursorExercise.getString(cursorExercise.getColumnIndex(
                                    WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS
                            ));
                            if(currentWeight != null) weight[counter] = Integer.parseInt(currentWeight);
                            if(currentReps != null) reps[counter] = Integer.parseInt(currentReps);
                            counter++;
                        }
                        while (cursorExercise.moveToNext());
                        cursorExercise.close();
                    }
                }

                break;
            case R.id.weight_or_reps_spinner:
                weightOrReps = parent.getSelectedItem().toString();
        }

        if(weightOrReps != null  && weight != null)
        graphData(weightOrReps, weight, reps);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void graphData(String selectWeightOrReps, int[] selectedWeight, int[] selectedReps) {
        DataPoint[] dataPoints;
        if (selectWeightOrReps.equals("Weight")) {
            dataPoints = new DataPoint[selectedWeight.length];
            for (int i = 0; i < selectedWeight.length; i++) {
                double current = selectedWeight[i];
                double iDouble = i;
                dataPoints[i] = new DataPoint(i, current);
            }

        } else if (selectWeightOrReps.equals("Reps")) {
            dataPoints = new DataPoint[selectedReps.length];
            for (int i = 0; i < selectedReps.length; i++) {
                double current = selectedReps[i];
                double iDouble = i;
                dataPoints[i] = new DataPoint(i, current);
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            graphView.addSeries(series);
        }
    }
}

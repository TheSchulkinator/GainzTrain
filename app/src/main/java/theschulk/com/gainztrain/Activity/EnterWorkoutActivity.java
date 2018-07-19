package theschulk.com.gainztrain.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class EnterWorkoutActivity extends AppCompatActivity {

    String currentExerciseString;
    SQLiteDatabase db;

    @BindView(R.id.enter_workout_current_exercise) TextView textViewCurrentExercise;
    @BindView(R.id.edit_workout_reps) EditText editWorkoutReps;
    @BindView(R.id.edit_workout_weight) EditText editWorkoutWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_workout);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        currentExerciseString = intent.getStringExtra(Intent.EXTRA_TEXT);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        db = dbHelper.getWritableDatabase();

        textViewCurrentExercise.setText(currentExerciseString);
    }

    public void onClickSubmitWorkout(View view){

        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
        String currentDate = dateFormat.format(date);

        //get string from edit text
        String editRepsString = editWorkoutReps.getText().toString();
        String editWeightString = editWorkoutWeight.getText().toString();

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

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
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

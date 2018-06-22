package theschulk.com.gainztrain.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DateFormat;
import java.util.Date;

import theschulk.com.gainztrain.Adapters.CurrentExerciseRecyclerViewAdapter;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class CurrentExerciseDetail extends AppCompatActivity {

    SQLiteDatabase db;
    String currentExerciseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_exercise_detail);

        Intent intent = getIntent();
        currentExerciseString = intent.getStringExtra(Intent.EXTRA_TEXT);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this );
        db = dbHelper.getReadableDatabase();
        db = dbHelper.getWritableDatabase();

        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
        String[] selectionArgs = {dateFormat.format(date), currentExerciseString};
        String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_DATE + "=? and " +
                WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME + "=?";

        Cursor cursor = db.query(WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE,
                null ,
                selection,
                selectionArgs,
                null,
                null,
                null);
        CurrentExerciseRecyclerViewAdapter adapter = new CurrentExerciseRecyclerViewAdapter();
        adapter.setData(cursor);
    }
}

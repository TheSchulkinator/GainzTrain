package theschulk.com.gainztrain.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import theschulk.com.gainztrain.Adapters.CursorRecyclerViewAdapter;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int URL_CURRENT_WORKOUT_LOADER = 0;
    public static final int URL_SAVED_WORKOUT_LOADER = 1;
    CursorRecyclerViewAdapter mCursorRecyclerViewAdapter;
    LinearLayoutManager mLayoutManager;

    //Butterknife setup


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();

        mCursorRecyclerViewAdapter = new CursorRecyclerViewAdapter();
        RecyclerView homeActivityRecyclerView = (RecyclerView) findViewById(R.id.home_activity_recycler_view);

        //query the database if current workout session return else saved workouts
        getSupportLoaderManager().initLoader(URL_CURRENT_WORKOUT_LOADER, null, this);

        //Setup RecyclerView
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Uri authorityUri = WorkoutDatabaseContract.WorkoutEntry.BASE_CONTENT_URI;

        switch(id){
            case (URL_CURRENT_WORKOUT_LOADER):
                Uri pathUri = authorityUri.buildUpon().
                        appendPath(WorkoutDatabaseContract.WorkoutEntry.MUSCLE_GROUP_TABLE).
                        build();

                return new CursorLoader(this, pathUri, null, null, null, null);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data != null){
            String[] exerciseNamesByMuscleGroup = new String[data.getCount()];
            int counter = 0;
            if(data.moveToFirst()){
                do{
                    String currentExercise = data.getString(data.getColumnIndex(
                            WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_EXERCISE_NAME
                    ));

                    exerciseNamesByMuscleGroup[counter] = currentExercise;
                    counter++;
                }
                while (data.moveToNext());
                    data.close();
                mCursorRecyclerViewAdapter.setData(exerciseNamesByMuscleGroup);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    
}

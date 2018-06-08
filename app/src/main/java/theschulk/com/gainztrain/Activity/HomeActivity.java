package theschulk.com.gainztrain.Activity;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseTableCreator;
import theschulk.com.gainztrain.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }
}

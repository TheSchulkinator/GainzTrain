package theschulk.com.gainztrain.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import theschulk.com.gainztrain.R;

import static theschulk.com.gainztrain.Database.WorkoutDatabaseContract.WorkoutEntry.CREATE_WORKOUT_ENTRY;
import static theschulk.com.gainztrain.Database.WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE;

public class WorkoutDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "gainzTrain.db";
    private static final int DATABASE_VERSION = 4;
    private Context mContext;

    public WorkoutDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Create all tables
        sqLiteDatabase.execSQL(WorkoutDatabaseContract.WorkoutEntry.CREATE_MUSCLE_GROUP);
        sqLiteDatabase.execSQL(WorkoutDatabaseContract.WorkoutEntry.CREATE_CUSTOM_WORKOUT);
        sqLiteDatabase.execSQL(WorkoutDatabaseContract.WorkoutEntry.CREATE_USER);
        sqLiteDatabase.execSQL(WorkoutDatabaseContract.WorkoutEntry.CREATE_WORKOUT_ENTRY);

        //populate the tables with default workout selections
        WorkoutDatabaseTableCreator.createTable(R.string.back, R.array.back_array, sqLiteDatabase, mContext);
        WorkoutDatabaseTableCreator.createTable(R.string.bicep, R.array.bicep_array, sqLiteDatabase, mContext);
        WorkoutDatabaseTableCreator.createTable(R.string.tricep, R.array.tricep_array, sqLiteDatabase, mContext);
        WorkoutDatabaseTableCreator.createTable(R.string.chest, R.array.chest_array, sqLiteDatabase, mContext);
        WorkoutDatabaseTableCreator.createTable(R.string.leg, R.array.leg_array, sqLiteDatabase, mContext);
        WorkoutDatabaseTableCreator.createTable(R.string.shoulder, R.array.shoulder_array, sqLiteDatabase, mContext);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + WORKOUT_ENTRY_TABLE);
        sqLiteDatabase.execSQL(CREATE_WORKOUT_ENTRY);
    }
}

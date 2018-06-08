package theschulk.com.gainztrain.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public final class WorkoutDatabaseTableCreator {

    //method to create table with string array values
    //must pass in a resource id backed with a string array
    public static void createTable(int muscleGroupStringId,int stringArrayResourceId, SQLiteDatabase db, Context context){

        ContentValues contentValues = new ContentValues();
        Resources res = context.getResources();

        //Get values from resources
        String[] defaultTableData = res.getStringArray(stringArrayResourceId);
        String muscleGroupName = res.getString(muscleGroupStringId);

        //for loop to construct a table based on the default string array passed in.
        for(int i = 0; i < defaultTableData.length; i ++){
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP, muscleGroupName);
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_EXERCISE_NAME, defaultTableData[i]);
            db.insert(WorkoutDatabaseContract.WorkoutEntry.MUSCLE_GROUP_TABLE, null, contentValues);
        }
    }

}

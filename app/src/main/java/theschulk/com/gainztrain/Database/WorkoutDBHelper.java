package theschulk.com.gainztrain.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "gainzTrain.db";
    private static final int DATABASE_VERSION = 1;

    public WorkoutDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

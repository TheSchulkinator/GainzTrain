package theschulk.com.gainztrain.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class WorkoutContentProvider extends ContentProvider {

    SQLiteDatabase db;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(WorkoutDatabaseContract.WorkoutEntry.AUTHORITY, WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE, 1);
        uriMatcher.addURI(WorkoutDatabaseContract.WorkoutEntry.AUTHORITY, WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE + "/#", 2);
        uriMatcher.addURI(WorkoutDatabaseContract.WorkoutEntry.AUTHORITY, WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE, 3);
        uriMatcher.addURI(WorkoutDatabaseContract.WorkoutEntry.AUTHORITY, WorkoutDatabaseContract.WorkoutEntry.CUSTOM_WORKOUT_TABLE + "/#", 4);
        uriMatcher.addURI(WorkoutDatabaseContract.WorkoutEntry.AUTHORITY, WorkoutDatabaseContract.WorkoutEntry.MUSCLE_GROUP_TABLE, 5);
        uriMatcher.addURI(WorkoutDatabaseContract.WorkoutEntry.AUTHORITY, WorkoutDatabaseContract.WorkoutEntry.MUSCLE_GROUP_TABLE + "/#", 6);
    }

    @Override
    public boolean onCreate() {
        WorkoutDBHelper dbHelper = new WorkoutDBHelper(getContext());
        db = dbHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        //TODO: Add individual query method for each case
        //switch (uriMatcher.match(uri)){

       // }

        Cursor queryCursor = db.query(WorkoutDatabaseContract.WorkoutEntry.MUSCLE_GROUP_TABLE, null,
                null,
                null,
                null,
                null,
                null);
        return queryCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

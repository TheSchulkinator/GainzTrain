package theschulk.com.gainztrain.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class WorkoutDatabaseContract {

    private WorkoutDatabaseContract(){}

    public static class WorkoutEntry implements BaseColumns{

        //URI for the content provider
        public static final String AUTHORITY = "com.theschulk.gainztrain.provider";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);



        //table stores info about muscle group and individual exercises
        public static final String MUSCLE_GROUP_TABLE = "muscle_group";
        public static final String COLUMN_NAME_MUSCLE_GROUP = "muscle_group";
        public static final String COLUMN_NAME_EXERCISE_NAME = "exercise_name";

        //Table stores the info for individual workouts
        public static final String WORKOUT_ENTRY_TABLE = "workout_entry";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_REPS = "reps";
        public static final String COLUMN_NAME_SETS = "sets";
        public static final String COLUMN_NAME_DATE = "date";

        //Table stores a custom workout of exercises
        public static final String CUSTOM_WORKOUT_TABLE = "workout_table";
        public static final String COLUMN_NAME_WORKOUT_NAME = "workout_name";
        public static final String COLUMN_NAME_WORKOUT_EXERCISE = "workout_exercise";

        //Table to store user info
        public static final String USER_INFO_TABLE = "user_info";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_USER_WEIGHT = "user_weight";
        public static final String  COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_TARGET_WEIGHT = "target_weight";

        //Table creation scripts

        public static final String CREATE_MUSCLE_GROUP = "CREATE TABLE " +
                MUSCLE_GROUP_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_MUSCLE_GROUP + " TEXT NOT NULL, " +
                COLUMN_NAME_EXERCISE_NAME + " TEXT NOT NULL" +
                ");";

        public static final String CREATE_WORKOUT_ENTRY = "CREATE TABLE " +
                WORKOUT_ENTRY_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_WEIGHT + " INTEGER NOT NULL, " +
                COLUMN_NAME_REPS + " INTEGER NOT NULL, " +
                COLUMN_NAME_SETS + " INTEGER, " +
                COLUMN_NAME_DATE + " DATETIME" +
                ");";

        public static final String CREATE_CUSTOM_WORKOUT = "CREATE TABLE " +
                CUSTOM_WORKOUT_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_WORKOUT_NAME + " TEXT NOT NULL, " +
                COLUMN_NAME_WORKOUT_EXERCISE + " TEXT NOT NULL" +
                ");";

        public static final String CREATE_USER = "CREATE TABLE " +
                USER_INFO_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_AGE + " INTEGER NOT NULL, " +
                COLUMN_NAME_USER_WEIGHT + " INTEGER NOT NULL, " +
                COLUMN_NAME_HEIGHT + " INTEGER NOT NULL, " +
                COLUMN_NAME_TARGET_WEIGHT + " INTEGER" +
                ");";

        //Scripts to populate default table data


    }
}

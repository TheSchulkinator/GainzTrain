package theschulk.com.gainztrain.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DateFormat;
import java.util.Date;

import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class GainzTrainRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    public GainzTrainRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }


    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.widget_view_key),Context.MODE_PRIVATE);

        Uri authorityUri = WorkoutDatabaseContract.WorkoutEntry.BASE_CONTENT_URI;
        Uri pathUri = authorityUri.buildUpon().
                appendPath(WorkoutDatabaseContract.WorkoutEntry.WORKOUT_ENTRY_TABLE).
                build();

        String widgetView = sharedPreferences.getString(mContext.getString(R.string.widget_view_key), "");

        String[] selectionArgs = {widgetView};
        String selection = WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WORKOUT_EXERCISE_NAME + "=?" +
                " AND " + WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS + " IS NOT NULL";

        mCursor = mContext.getContentResolver().query(pathUri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        //Get strings to display in layout
        String weight ="";
        String reps = "";
        String sets = "";
        if(mCursor == null) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_select_recipe);
            return views;
        }

        if(mCursor.moveToPosition(position)){
            final int weightColumnIndex = mCursor.getColumnIndex(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WEIGHT);
            final int repsColumnIndex = mCursor.getColumnIndex(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS);

            weight = mCursor.getString(weightColumnIndex) + "lbs";
            reps = "x" + mCursor.getString(repsColumnIndex);
            sets = Integer.toString(position);
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_text_views);

        views.setTextViewText(R.id.widget_item_text_view_weight, weight);
        views.setTextViewText(R.id.widget_item_text_view_reps, reps);
        views.setTextViewText(R.id.widget_item_text_view_set, sets);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

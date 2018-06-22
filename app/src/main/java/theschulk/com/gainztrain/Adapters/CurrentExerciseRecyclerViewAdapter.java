package theschulk.com.gainztrain.Adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class CurrentExerciseRecyclerViewAdapter extends RecyclerView.Adapter<CurrentExerciseRecyclerViewAdapter.currentExerciseViewHolder> {

    String[] weight;
    String[] reps;

    public CurrentExerciseRecyclerViewAdapter(){}


    @NonNull
    @Override
    public currentExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull currentExerciseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class currentExerciseViewHolder extends RecyclerView.ViewHolder{

        public currentExerciseViewHolder(View itemView) {
            super(itemView);
        }


    }

    public void setData(Cursor cursor){
        if (cursor != null) {
            weight = new String[cursor.getCount()];
            reps = new String[cursor.getCount()];
            int counter = 0;
            if (cursor.moveToFirst()) {
                do {
                    String currentWeight = cursor.getString(cursor.getColumnIndex(
                            WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_WEIGHT
                    ));

                    String currentReps = cursor.getString(cursor.getColumnIndex(
                            WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_REPS
                    ));

                    weight[counter] = currentWeight;
                    reps[counter] = currentReps;
                    counter++;
                }
                while (cursor.moveToNext());
                cursor.close();
            }
        }
    }
}

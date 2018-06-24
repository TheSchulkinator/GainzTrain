package theschulk.com.gainztrain.Adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.current_exercise_double_item_view, parent, false);
        return new currentExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull currentExerciseViewHolder holder, int position) {

        String currentSet = Integer.toString(position + 1);

        if(reps != null || reps.length > 0){
            holder.currentSetsTextView.setText(currentSet);
            holder.currentWeightTextView.setText(weight[position] + "lbs");
            holder.currentRepsTextView.setText("x"+ reps[position]);
        }

    }

    @Override
    public int getItemCount() {
        if (null == reps) return  0;
        int arrayLength = reps.length;
        return arrayLength;
    }

    public class currentExerciseViewHolder extends RecyclerView.ViewHolder{

        public final TextView currentSetsTextView;
        public final TextView currentWeightTextView;
        public final TextView currentRepsTextView;

        public currentExerciseViewHolder(View itemView) {
            super(itemView);

            currentSetsTextView = itemView.findViewById(R.id.double_item_text_view_set);
            currentWeightTextView = itemView.findViewById(R.id.double_item_text_view_weight);
            currentRepsTextView = itemView.findViewById(R.id.double_item_text_view_reps);
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
                    if(currentWeight != null) weight[counter] = currentWeight;
                    if(currentReps != null) reps[counter] = currentReps;
                    counter++;
                }
                while (cursor.moveToNext());
                cursor.close();
                notifyDataSetChanged();
            }
        }
    }
}

package theschulk.com.gainztrain.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import theschulk.com.gainztrain.R;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.ViewHolder> {

    String[] mData;
    String selectedExercise;
    private final CursorOnClickHandler mCursorOnClickHandler;

    public interface CursorOnClickHandler{
        void onClick(String selectedExercise);
    }

    public CursorRecyclerViewAdapter(CursorOnClickHandler cursorOnClickHandler){
            mCursorOnClickHandler = cursorOnClickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_view_single_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mData != null || mData.length > 0){
            holder.mSingleItemTextView.setText(mData[position]);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mData) return  0;
        int arrayLength = mData.length;
        return arrayLength;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mSingleItemTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mSingleItemTextView = itemView.findViewById(R.id.single_item_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            selectedExercise = mData[adapterPosition];

            mCursorOnClickHandler.onClick(selectedExercise);
        }
    }

    public void setData(String[] data){
        mData = data;
        notifyDataSetChanged();
    }

}

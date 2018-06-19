package theschulk.com.gainztrain.Adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import theschulk.com.gainztrain.R;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.ViewHolder> {

    String[] mData;

    public CursorRecyclerViewAdapter(){

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mSingleItemTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mSingleItemTextView = (TextView) itemView.findViewById(R.id.single_item_text_view);
        }
    }

    public void setData(String[] data){
        mData = data;
        notifyDataSetChanged();
    }

}

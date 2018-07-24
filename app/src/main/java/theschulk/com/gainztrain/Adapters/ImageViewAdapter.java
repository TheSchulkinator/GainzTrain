package theschulk.com.gainztrain.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import theschulk.com.gainztrain.R;

public class ImageViewAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    //List of files to display
    File[] files;

    public ImageViewAdapter(Context mContext){
        context = mContext;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_image_view, container, false);

        ImageView imageView = view.findViewById(R.id.body_tracker_image_view);
        Picasso.get().load(files[position]).resize(1000,1000).centerCrop().into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setFiles(File[] mFiles){
        files = mFiles;
        notifyDataSetChanged();
    }
}

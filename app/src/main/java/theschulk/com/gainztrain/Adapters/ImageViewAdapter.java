package theschulk.com.gainztrain.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import theschulk.com.gainztrain.Activity.BodyTrackerActivity;
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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.adapter_image_view, container, false);

        ImageView imageView = view.findViewById(R.id.body_tracker_image_view);
        Picasso.get().load(files[position]).resize(1000,1000).centerCrop().into(imageView);

        //handle long click events to delete pictures
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Delete Image")
                        .setMessage("Are you sure you want to delete this image?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                File deleteFile = files[position];
                                if(deleteFile.exists()){
                                    try {
                                        deleteFile.delete();
                                    }
                                    catch (Exception e){
                                        throw new IllegalStateException("Picture was not deleted");
                                    }
                                }
                                //Create an intent to reload screen when dialog closed
                                Intent intent = new Intent(context, BodyTrackerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });

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

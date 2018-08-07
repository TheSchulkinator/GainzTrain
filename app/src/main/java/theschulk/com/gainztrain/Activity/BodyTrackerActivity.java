package theschulk.com.gainztrain.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Adapters.ImageViewAdapter;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class BodyTrackerActivity extends AppCompatActivity {

    SQLiteDatabase db;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    Uri photoURI;
    File[] files;
    ImageViewAdapter imageViewAdapter;

    @BindView(R.id.edit_user_weight) EditText editUserWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_tracker);
        ButterKnife.bind(this);

        //get the local camera files and set the pageradapter
        imageViewAdapter = new ImageViewAdapter(this);
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        files = directory.listFiles();
        imageViewAdapter.setFiles(files);
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(imageViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageViewAdapter = new ImageViewAdapter(this);
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        files = directory.listFiles();
        imageViewAdapter.setFiles(files);
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(imageViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch(item.getItemId()){
            case R.id.menu_body_tracker:
                intent = new Intent(this, BodyTrackerActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_custom_workouts:
                intent = new Intent(this, CustomWorkoutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_home:
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;
            case  R.id.menu_history:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickTakePhoto(View view){

       dispatchTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    dispatchTakePictureIntent();
                    galleryAddPic();
                }else{
                    Toast toast = Toast.makeText(this, R.string.camera_permission, Toast.LENGTH_SHORT);
                    toast.show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageViewAdapter.setFiles(files);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                Log.e("File Error", ex.getMessage());
                Log.e("File Error", photoFile.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "theschulk.com.gainztrain.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private File createImageFile() throws IOException {
        //save the user weight inside of the filename to display to the user
        String editWeightString = editUserWeight.getText().toString();

        //add data to exercise table and to muscle group table for exercise to persist in future
        if(editWeightString == null || editWeightString.equals("")) {
           /// Toast toast = Toast.makeText(this, R.string.emptyEditTextToast, Toast.LENGTH_SHORT);
            //toast.show();
            editWeightString = getString(R.string.no_user_weight);
        }


        // Create an image file name
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + editWeightString + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}

package theschulk.com.gainztrain.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.Database.WorkoutDBHelper;
import theschulk.com.gainztrain.Database.WorkoutDatabaseContract;
import theschulk.com.gainztrain.R;

public class BodyTrackerActivity extends AppCompatActivity {

    SQLiteDatabase db;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    Uri photoURI;
    File[] fileList;

    @BindView(R.id.edit_user_weight) EditText editUserWeight;
    @BindView(R.id.edit_user_height) EditText editUserHeight;
    @BindView(R.id.body_tracker_image_switcher) ImageView imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_tracker);
        ButterKnife.bind(this);

        WorkoutDBHelper dbHelper = new WorkoutDBHelper(this);
        db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();

        cameraAsyncTask cameraTask = new cameraAsyncTask();
        cameraTask.execute();

    }

    public void onClickSubmitUserInfo(View view){
        //get string from edit text
        String editWeightString = editUserWeight.getText().toString();
        String editHeightString = editUserHeight.getText().toString();

        //add data to exercise table and to muscle group table for exercise to persist in future
        if(editWeightString == null || editWeightString.equals("")) {
            Toast toast = Toast.makeText(this, R.string.emptyEditTextToast, Toast.LENGTH_SHORT);
            toast.show();
        } else {


            int editWeightInt = Integer.parseInt(editWeightString);

            ContentValues contentValues = new ContentValues();
            contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_USER_WEIGHT, editWeightInt);

            //only add height if value is entered
            Boolean isHeightAvailable = editHeightString.equals("");
            if(!isHeightAvailable){
                int editHeightInt = Integer.parseInt(editHeightString);
                contentValues.put(WorkoutDatabaseContract.WorkoutEntry.COLUMN_NAME_HEIGHT, editHeightInt);
            }

            db.insert(WorkoutDatabaseContract.WorkoutEntry.USER_INFO_TABLE, null, contentValues);

            Toast toast = Toast.makeText(this, R.string.info_added, Toast.LENGTH_SHORT);
            toast.show();
        }
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
            Picasso.get().load(photoURI).resize(1000,1000).centerCrop().into(imageSwitcher);
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
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
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

    public class cameraAsyncTask extends AsyncTask<Void, Void, File[]>{

        @Override
        protected File[] doInBackground(Void... voids) {
            File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File[] files = directory.listFiles();
            return files;
        }

        @Override
        protected void onPostExecute(File[] files) {
            super.onPostExecute(files);

            if (files.length > 0 && files != null){
            fileList = files;
            int filesLength = files.length - 1;
            Picasso.get().load(files[filesLength]).resize(1000,1000).centerCrop().into(imageSwitcher);
            }
        }
    }

}

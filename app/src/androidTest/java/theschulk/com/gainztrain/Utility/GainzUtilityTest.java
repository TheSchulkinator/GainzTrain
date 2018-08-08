package theschulk.com.gainztrain.Utility;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import theschulk.com.gainztrain.Model.BodyTrackerModel;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class GainzUtilityTest {

    @Test
    public void getBodyTrackerFromFile() {

        Context context = InstrumentationRegistry.getContext();

        String imageFileName = "JPEG_" + "01-01-0001" + "_" + "200" + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String expectedDate = "01-01-0001";
        String expectedWeight = "200";

        try{
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );

            BodyTrackerModel bodyTrackerModel = GainzUtility.getBodyTrackerFromFile(image);

            assertEquals(expectedDate, bodyTrackerModel.getPictureDate());
            assertEquals(expectedWeight, bodyTrackerModel.getPictureWeight());

        } catch (IOException e){ }





    }
}
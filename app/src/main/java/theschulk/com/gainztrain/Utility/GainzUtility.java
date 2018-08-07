package theschulk.com.gainztrain.Utility;

import java.io.File;

import theschulk.com.gainztrain.Model.BodyTrackerModel;

public class GainzUtility {

    public static BodyTrackerModel getBodyTrackerFromFile(File file){

        BodyTrackerModel bodyTrackerModel = new BodyTrackerModel();

        //get string from file and split into array delimited by _
        String pictureString = file.toString();
        String[] splitString = pictureString.split("_");

        //the picture date is in index 1 date in index 2
        bodyTrackerModel.setPictureDate(splitString[1]);
        bodyTrackerModel.setPictureWeight(splitString[2]);
        return  bodyTrackerModel;
    }
}

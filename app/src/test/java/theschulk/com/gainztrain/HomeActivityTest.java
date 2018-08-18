package theschulk.com.gainztrain;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import theschulk.com.gainztrain.Database.WorkoutDBHelper;

@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

    WorkoutDBHelper dbHelper;

    @Before
    public void setup(){
        dbHelper = new WorkoutDBHelper(RuntimeEnvironment.application);
    }
}

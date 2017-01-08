package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.hardware.SensorEvent;
import android.provider.ContactsContract;

/**
 * Created by Anthony on 2016-07-01.
 */

public class DataFilter {

    private float smoothX;
    private float smoothY;
    private float smoothZ;

    private int count = 0;
    private final int smoothing = 10;

    public DataFilter() {}

    public void run (SensorEvent se)
    {
        //input(se);
        filterData(se.values[0], se.values[1], se.values[2]);
    }

    public void filterData(float x1, float y1, float z1)
    {
        if (count == 0) {
            smoothX = x1;
            smoothY = y1;
            smoothZ = z1;
        } else {
            smoothX += (x1 - smoothX) / smoothing;
            smoothY += (y1 - smoothY) / smoothing;
            smoothZ += (z1 - smoothZ) / smoothing;
        }
        count++;
    }

    public float[] getReadings()
    {
        return new float[]{smoothX, smoothY, smoothZ};
        //return new float[]{0, 0, 0};
    }


}

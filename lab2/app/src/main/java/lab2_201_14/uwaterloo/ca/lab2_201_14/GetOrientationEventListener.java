package lab2_201_14.uwaterloo.ca.lab2_201_14;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Anthony on 2016-06-23.
 */

public class GetOrientationEventListener implements SensorEventListener {

    //------------------------------//
    private TextView output;
    private Context context;        // For properly saving .txt files to phone
    private FileOutputStream os;
    private PrintWriter osw;
    //------------------------------//
    private float azimuth;          // Rotation around the -Z axis
    private float pitch;            // Rotation around the -X axis
    private float roll;             // Rotation around the Y axis
    //------------------------------//
    private int numberOfDataPoints = 0;
    private int maxDataPoints = 2500;
    //------------------------------//


    // CONSTRUCTOR
    public GetOrientationEventListener(TextView outputView) {

        // Field initialization
        output = outputView;
        context = Lab2_201_14.getAppContext();

        // File output setup
        try {
            os = new FileOutputStream(new File(context.getExternalFilesDir(null), "external_rotation.txt"));
            osw = new PrintWriter(new OutputStreamWriter(os));
        } catch (IOException e) {
            Log.d("CATCHING IOEXCEPTION", "IOException caught!! AHHH!! OMG PANIC!");
        }

    }

    // Empty onAccuracyChanged
    public void onAccuracyChanged(Sensor s, int i) { }


    private float R[] = new float[9];
    private float orientation[] = new float[3];
    public void onSensorChanged(SensorEvent se) {
        // Whenever the sensor values change:
        // - Read the values and store in a local field
        // - Print them out to a .txt file on the phone
        // - Print out the values on the screen

        // if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Storing sensor readings into fields
            SensorManager.getOrientation(R, orientation);
            azimuth = orientation[0];
            pitch = orientation[1];
            roll = orientation[2];

            // Output data to file
            // A safety precaution to make sure it doesn't output data forever
            if (numberOfDataPoints < maxDataPoints) {
                osw.println(azimuth + " " + pitch + " " + roll);  // Printing to file
            } else if (numberOfDataPoints == maxDataPoints) {     // Close file after desired number of data points is reached
                osw.close();
            }
            numberOfDataPoints++;

            // Screen output
            output.setText("Azimuth: " + azimuth + "\nPitch: " + pitch + "\nRoll: " + roll);

        //}

    }

}

package lab3_201_14.uwaterloo.ca.lab3_201_14;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import ca.uwaterloo.mapper.PositionListener;

/**
 * Created by frederick on 5/17/2016.
 */

public class LinearAccelerationEventListener  implements SensorEventListener {

    // FIELDS //
    //TextView output;
    //LineGraphView graph;
    private TextView outputSteps;
    private TextView outputDisplacement;
    private Context context;

    private FileOutputStream os;
    private PrintWriter osw;
    private static StepDetector stepCounter;
    private PositionListener positionListener;

    private static float x;
    private static float y;
    private static float z;
    private float smoothX;
    private float smoothY;
    private float smoothZ;
    private float[] orientation;
    private final int smoothing = 10;  // Smoothing factor for filter
    private int numOfDataPts = 0;          // To limit
    private final int maxDataPoints = 0;   // file writing

    public static boolean detectorOn = false;
    // END OF FIELDS //

    // Constructor
    public LinearAccelerationEventListener(/*TextView outputView,*/ TextView stepCounterView, TextView displacementView, PositionListener posLis) {
        //output = outputView;
        //graph = graphView;
        outputSteps = stepCounterView;
        outputDisplacement = displacementView;
        positionListener = posLis;

        context = MainActivity.getAppContext();
        stepCounter = new StepDetector(positionListener);

        try { // Initializing the output stream and print writer
            os = new FileOutputStream(new File(context.getExternalFilesDir(null), "external.txt"));
            osw = new PrintWriter(new OutputStreamWriter(os));
        } catch (IOException e) { }
    }

    public void onAccuracyChanged(Sensor s, int i) { }

    public void onSensorChanged(SensorEvent se){
        if(se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            storeSensorValues(se); // Store
            filterData(x, y, z);   // Filter acceleration data
            if(detectorOn) {
                stepCounter.inputData(smoothZ, numOfDataPts); // FSM input
            }
            writeToFile();  // File output
            screenOutput(); // Screen output
            //graph.addPoint(se.values); // Graph output
        }
    }

    public void storeSensorValues(SensorEvent se) {
        x = se.values[0];
        y = se.values[1];
        z = se.values[2];
    }

    // Low-pass filter
    public void filterData(float x, float y, float z) {
        if (numOfDataPts == 0) {
            smoothX = x;
            smoothY = y;
            smoothZ = z;
        } else {
            smoothX += (x - smoothX) / smoothing;
            smoothY += (y - smoothY) / smoothing;
            smoothZ += (z - smoothZ) / smoothing;
        }
    }

    // Output data to file and limit it from writing forever
    public void writeToFile() {
        if (numOfDataPts < maxDataPoints) {
            OrientationSensorEventListener.calcOrientation();
            orientation = OrientationSensorEventListener.getOrientationMatrix();
            osw.println(x + " " + y + " " + z + " " + smoothX + " " + smoothY + " " + smoothZ + " " + orientation[0] + " " + orientation[1] + " " + orientation[2] + " " + StepDetector.numberOfSteps);
        } else if (numOfDataPts == maxDataPoints) {
            osw.close();
        }
        numOfDataPts++;
    }

    // Outputs: Number of steps, Displacement vectors (in meters and in steps)
    public void screenOutput() {
        outputSteps.setText(stepCounter.getNumberOfSteps() + " steps");
        outputDisplacement.setText(String.format("%.2f",DisplacementManager.getX()) + " m NS, " + String.format("%.2f",DisplacementManager.getY()) + " m EW" + "\n" + String.format("%.2f",DisplacementManager.getXSteps()) + " steps NS, " + String.format("%.2f",DisplacementManager.getYSteps()) + " steps EW" );
    }

    public static float[] getLinearAccelerationReading(){ return new float[]{x, y, z}; }

    public static void activateStepDetector() {
        detectorOn = true;
    }

    public static void deactivateStepDetector() {
        detectorOn = false;
    }

    public static boolean detectorStatus() {
        return detectorOn;
    }

}

package lab2_201_14.uwaterloo.ca.lab2_201_14;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

import java.io.PrintWriter;

import ca.uwaterloo.sensortoy.LineGraphView;

public class Lab2_201_14 extends AppCompatActivity implements SensorEventListener {
    LineGraphView graph;
    private static Context context;
    private float azimuth;
    private float pitch;
    private float roll;
    TextView orientationDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Lab2_201_14.context = getApplicationContext();

        setContentView(R.layout.activity_lab2_201_14);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOrientation(LinearLayout.VERTICAL);

        //ButtonEventListener();

//LIGHT DISPLAY
        //Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //TextView lightDisplay = (TextView) findViewById(R.id.lightLabel);
        //SensorEventListener lightListener = new LightSensorEventListener(lightDisplay);
        //sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

//ROTATION VECTOR DISPLAY
//        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//        TextView rotationDisplay= (TextView) findViewById(R.id.rotationLabel);
//        SensorEventListener rotationListener = new RotationSensorEventListener(rotationDisplay);
//        sensorManager.registerListener(rotationListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);

// MAGNETIC FIELD DISPLAY
//        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        TextView magneticDisplay= (TextView) findViewById(R.id.magneticLabel);
//        SensorEventListener magneticListener = new MagneticFieldSensorEventListener(magneticDisplay);
//        sensorManager.registerListener(magneticListener, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);


//ACCELEROMETER DISPLAY
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //LineGraphView implementation
        graph = new LineGraphView(getApplicationContext(),
                100,
                Arrays.asList("x","y","z"));
        layout.addView(graph);
        graph.setVisibility(View.VISIBLE);

        TextView accelerometerDisplay = (TextView) findViewById(R.id.accelerometerLabel);
        TextView stepsDisplay = (TextView) findViewById(R.id.numberOfSteps);
        SensorEventListener accelerometerListener = new AccelerometerEventListener(accelerometerDisplay, stepsDisplay, graph);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

        final Button button = (Button) findViewById(R.id.resetButton);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StepDetector.resetNumberOfSteps();
                    AccelerometerEventListener.stepCounter.setCurrentState(0);
                }
            });
        }

        orientationDisplay = (TextView) findViewById(R.id.orientationLabel);

    }

    // Returns the application context
    public static Context getAppContext() {

        return Lab2_201_14.context;

    }

//    float[] mGeomagnetic;
//    float[] mGravity;



    static public int counter = 0;
    public void onAccuracyChanged(Sensor s, int i) {};

    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            orientationDisplay.setText(counter);
            counter++;
        }

//        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
//            mGravity = event.values;
//        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
//            mGeomagnetic = event.values;
//        if(mGravity != null && mGeomagnetic != null) {
//            float R[] = new float[9];
//            float I[] = new float[9];
//            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//            if (success) {
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//                azimuth = orientation[0];
//                pitch = orientation[1];
//                roll = orientation[2];
//                orientationDisplay.setText("Azimuth: " + azimuth + "\nPitch: " + pitch + "\nRoll: " + roll);
//            }
//        }
    }

    static public int getCounter() {

        return counter;

    }

//BUTTON
//    Button resetButton;
//    public void ButtonEventListener(){
//        resetButton = (Button) findViewById(R.id.resetButton);
//        resetButton.setOnClickListener(new View.OnClickListener();
//                @Override
//                public void onClick() {
//                    AccelerometerEventListener.stepCounter.resetNumberOfSteps();
//                }
//    }

}



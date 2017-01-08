package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;


import ca.uwaterloo.mapper.*;

import ca.uwaterloo.sensortoy.LineGraphView;

public class Lab3_201_14 extends AppCompatActivity {

    private LineGraphView graph;
    private static Context context;
    private MapView mv;

    String mapName = "E2-3344.svg";
    private final int MAGNETIC = 0;
    private final int LINEAR_ACCELERATION = 1;
    private final int ACCELEROMETER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lab3_201_14);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOrientation(LinearLayout.VERTICAL);

        Lab3_201_14.context = getApplicationContext();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        loadAppComponents(layout, sensorManager);
    }

    // App components loader
    // Loads MapView, LineGraphView (if wanted), sensors and buttons
    public void loadAppComponents(LinearLayout layout, SensorManager sensorManager) {
        // LOAD CUSTOM VIEW COMPONENTS
        // MapView
        loadMap(layout);
        // LineGraphView (used for testing and stuff)
        // graph = new LineGraphView(getApplicationContext(),
        //         100,
        //         Arrays.asList("x","y","z"));
        // layout.addView(graph);
        // graph.setVisibility(View.VISIBLE);

        // LOAD SENSORS: Magnetic, Accelerometer
        loadSensor(sensorManager, Sensor.TYPE_MAGNETIC_FIELD, MAGNETIC, R.id.magneticLabel, SensorManager.SENSOR_DELAY_FASTEST);
        loadSensor(sensorManager, Sensor.TYPE_ACCELEROMETER, ACCELEROMETER, 0, SensorManager.SENSOR_DELAY_FASTEST);

        // Load linear acceleration (different from accelerometer)
        Sensor linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //TextView linearAccelerationDisplay = (TextView) findViewById(R.id.linearAccelerationLabel);
        TextView displacementVectorDisplay = (TextView) findViewById(R.id.displacementVector);
        TextView stepsDisplay = (TextView) findViewById(R.id.numberOfSteps);
        SensorEventListener linearAccelerationListener = new LinearAccelerationEventListener(/*linearAccelerationDisplay,*/ stepsDisplay, displacementVectorDisplay);
        sensorManager.registerListener(linearAccelerationListener, linearAccelerationSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // LOAD BUTTONS
        // Displacement reset button
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        if (resetButton != null) {
            resetButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DisplacementManager.resetVector();
                }
            });
        }
        // Steps reset button
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        if (clearButton != null) {
            clearButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StepDetector.resetNumberOfSteps();
                }
            });
        }
    }

    // Loading the map view into the app
    public void loadMap(LinearLayout layout) {
        MapView mv = new MapView(getApplicationContext(), 10000, 900, 36, 36);
        registerForContextMenu(mv);
        try {
            NavigationalMap map = MapLoader.loadMap(getExternalFilesDir(null), mapName);
            mv.setMap(map);
            layout.addView(mv);
            mv.setVisibility(View.VISIBLE);
        } catch (Exception x) { }
    }

    // Loading a sensor and linking it to a listener
    //    sensorManager - The app's sensor manager instance
    //    sensorType - In the form "Sensor.SENSOR_TYPE_HERE"
    //    sensorListenerType - Refer to the custom field constants at the top of this class (MAGNETIC, ACCELEROMETER etc)
    //    viewID - In the form "R.id.xmlTextViewLabelHere"
    //    sensorSpeed - In the form "SensorManager.SENSOR_DELAY_SPEED_HERE"
    public void loadSensor(SensorManager sensorManager, int sensorType, int sensorListenerType, int viewID, int sensorSpeed) {
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        SensorEventListener listener;
        if (viewID != 0) {
            TextView display = (TextView) findViewById(viewID);
            listener = createListener(sensorListenerType, display);
        } else {
            listener = createListener(sensorListenerType, null);
        }
        if (listener != null) {
            sensorManager.registerListener(listener, sensor, sensorSpeed);
        }
    }

    // Instantiating a listener depending on the request
    public SensorEventListener createListener(int sensorType, TextView textView) {
        switch (sensorType) {
            case MAGNETIC:
                return new MagneticFieldSensorEventListener(textView);
            case LINEAR_ACCELERATION:
                //return new LinearAccelerationEventListener(textView);
                break;
            case ACCELEROMETER:
                return new RealAccelerometerSensorEventListener();
            default:
                return null;
        }
        return null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mv.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item) || mv.onContextItemSelected(item);
    }

    // Returns the application context
    public static Context getAppContext() {
        return Lab3_201_14.context;
    }

}


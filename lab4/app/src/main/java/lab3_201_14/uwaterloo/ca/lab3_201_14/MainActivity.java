package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.content.Context;
import android.graphics.PointF;
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


import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.mapper.*;

import ca.uwaterloo.sensortoy.LineGraphView;

public class MainActivity extends AppCompatActivity implements PositionListener
{

    private LineGraphView graph;
    private static Context context;

    public static MapView mv;
    private PositionListener pl;
    public static NavigationalMap map;
    public static Navigator nav;

    String mapName = "E2-3344.svg";
    private final int MAGNETIC = 0;
    private final int LINEAR_ACCELERATION = 1;
    private final int ACCELEROMETER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lab3_201_14);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOrientation(LinearLayout.VERTICAL);

        MainActivity.context = getApplicationContext();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        loadAppComponents(layout, sensorManager);

        TextView outputOrigin = (TextView) findViewById(R.id.originLabel);
        TextView outputDestination = (TextView) findViewById(R.id.destinationLabel);
        TextView outputArrival = (TextView) findViewById(R.id.arrivalLabel);
        nav = new Navigator(map, mv, outputOrigin, outputDestination, outputArrival);

    }

    /*
     * App components loader
     * Loads MapView, LineGraphView (if wanted), sensors and buttons
     */
    public void loadAppComponents(LinearLayout layout, SensorManager sensorManager)
    {
        // LOAD CUSTOM VIEW COMPONENTS
        // MapView
        loadMap(layout);

        /* LineGraphView (used for testing and stuff)
         graph = new LineGraphView(getApplicationContext(),
                 100,
                 Arrays.asList("x","y","z"));
         layout.addView(graph);
         graph.setVisibility(View.VISIBLE);*/

        // LOAD SENSORS: Magnetic, Accelerometer
        loadSensor(sensorManager, Sensor.TYPE_MAGNETIC_FIELD, MAGNETIC, R.id.magneticLabel, SensorManager.SENSOR_DELAY_FASTEST);
        loadSensor(sensorManager, Sensor.TYPE_ACCELEROMETER, ACCELEROMETER, 0, SensorManager.SENSOR_DELAY_FASTEST);

        // Load linear acceleration (different from accelerometer)
        Sensor linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        /*TextView linearAccelerationDisplay = (TextView) findViewById(R.id.linearAccelerationLabel);*/
        TextView displacementVectorDisplay = (TextView) findViewById(R.id.displacementVector);
        TextView stepsDisplay = (TextView) findViewById(R.id.numberOfSteps);
        SensorEventListener linearAccelerationListener = new LinearAccelerationEventListener(/*linearAccelerationDisplay,*/ stepsDisplay, displacementVectorDisplay, pl);
        sensorManager.registerListener(linearAccelerationListener, linearAccelerationSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // LOAD BUTTONS
        // Reset button
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        if (clearButton != null) {
            clearButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StepDetector.resetNumberOfSteps();
                    DisplacementManager.resetVector();
                }
            });
        }
        // Activate/deactivate the step detector
        final Button stepButton = (Button) findViewById(R.id.stepButton);
        if (stepButton != null) {
            stepButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (LinearAccelerationEventListener.detectorStatus() == false) {
                        LinearAccelerationEventListener.activateStepDetector();
                    } else {
                        LinearAccelerationEventListener.deactivateStepDetector();
                    }
                }
            });
        }

        // Route calculate
        final Button routeButton = (Button) findViewById(R.id.calculateRoute);
        if (routeButton != null) {
            routeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    nav.determineRoute();
                }
            });
        }
    }

    // Loading the map view into the app
    public void loadMap(LinearLayout layout)
    {
        mv = new MapView(getApplicationContext(), 10000, 900, 36, 36);
        registerForContextMenu(mv);
        mv.setVisibility(View.VISIBLE);
        layout.addView(mv);
        try {
            map = MapLoader.loadMap(getExternalFilesDir(null), mapName);
            mv.setMap(map);
        } catch (Exception x) { }

        /*outputOrigin = (TextView) findViewById(R.id.originLabel);
        outputDestination = (TextView) findViewById(R.id.destinationLabel);*/
        /*origin = new PointF(0.00f, 0.00f);
        destination = new PointF(0.00f, 0.00f);
        outputOrigin.setText(String.format("%.2f", MainActivity.origin.x) + " " + String.format("%.2f", MainActivity.origin.y));
        outputDestination.setText(String.format("%.2f", MainActivity.destination.x) + " " + String.format("%.2f", MainActivity.destination.y));*/
        pl = new MainActivity();
        mv.addListener(pl);
    }

    /**
     * Loading a sensor and linking it to a listener
     * @param sensorManager - The app's sensor manager instance
     * @param sensorType - In the form "Sensor.SENSOR_TYPE_HERE"
     * @param sensorListenerType - Use the custom field constants at the top of this class (MAGNETIC, ACCELEROMETER etc)
     * @param viewID - In the form "R.id.xmlTextViewLabelHere". Set to 0 if no TextView is needed
     * @param sensorSpeed - In the form "SensorManager.SENSOR_DELAY_SPEED_HERE"
     */
    public void loadSensor(SensorManager sensorManager, int sensorType, int sensorListenerType, int viewID, int sensorSpeed)
    {
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
     // enums

    // Instantiating a listener depending on the request
    public SensorEventListener createListener(int sensorType, TextView textView)
    {
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
    public void originChanged(MapView source, PointF loc)
    {
        source.setOriginPoint(loc);
        source.setUserPoint(loc);
        nav.setOrigin(loc);
        nav.setUser(loc);
        MainActivity.nav.outputOrigin.setText(String.format("%.2f", Navigator.getOrigin().x) + " " + String.format("%.2f", Navigator.getOrigin().y));
        MainActivity.nav.clearRoute();
    }

    @Override
    public void destinationChanged(MapView source, PointF dest)
    {
        source.setDestinationPoint(dest);
        nav.setDestination(dest);
        MainActivity.nav.outputDestination.setText(String.format("%.2f", Navigator.getDestination().x) + " " + String.format("%.2f", Navigator.getDestination().y));
        MainActivity.nav.clearRoute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        mv.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) { return super.onContextItemSelected(item) || mv.onContextItemSelected(item); }

    // Returns the application context
    public static Context getAppContext() { return MainActivity.context; }

}


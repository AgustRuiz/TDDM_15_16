package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;

public class SensorFragment extends Fragment implements SensorEventListener {

    public static final String LOG_TAG = SensorFragment.class.getName() + "[A]";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    @BindView(R.id.table_accelerometer)
    TableLayout mTableAccelerometer;
    @BindView(R.id.text_view_accelerometer_error)
    TextView mAccelerometerError;
    @BindView(R.id.text_view_accelerometer_x)
    TextView mAccelerometerX;
    @BindView(R.id.text_view_accelerometer_y)
    TextView mAccelerometerY;
    @BindView(R.id.text_view_accelerometer_z)
    TextView mAccelerometerZ;

    Context mContext;

    //region [Fragment methods]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        ButterKnife.bind(this, view);
        initializeAccelerometer();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensorListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterSensorListeners();
    }

    //endregion

    //region [Accelerometer]

    private void initializeAccelerometer() {
        mAccelerometer = null;
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (listSensors.size() == 0) {
            setAccelerometerError(null);
        } else {
            mAccelerometer = listSensors.get(0);
            if (mAccelerometer == null) {
                setAccelerometerError(mContext.getString(R.string.unknown_error));
            }
        }
    }

    private void setAccelerometerError(String errorMessage) {
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
            mAccelerometerError.setText(errorMessage.trim());
        }
        mTableAccelerometer.setVisibility(View.GONE);
        mAccelerometerError.setVisibility(View.VISIBLE);
    }

    //endregion

    //region [Sensor]

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerX.setText(Float.toString(event.values[0]));
                mAccelerometerY.setText(Float.toString(event.values[1]));
                mAccelerometerZ.setText(Float.toString(event.values[2]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void registerSensorListeners() {
        if (mAccelerometer != null)
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterSensorListeners() {
        mSensorManager.unregisterListener(this);
    }

    //endregion
}

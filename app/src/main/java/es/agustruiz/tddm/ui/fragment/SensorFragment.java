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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;

public class SensorFragment extends Fragment implements SensorEventListener {

    public static final String LOG_TAG = SensorFragment.class.getName() + "[A]";

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mGravitySensor;
    private Sensor mGyroscopeSensor;
    private Sensor mLightSensor;
    private Sensor mLinearAccelerationSensor;

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

    @BindView(R.id.table_gravity)
    TableLayout mTableGravity;
    @BindView(R.id.text_view_gravity_error)
    TextView mGravityError;
    @BindView(R.id.text_view_gravity_x)
    TextView mGravityX;
    @BindView(R.id.text_view_gravity_y)
    TextView mGravityY;
    @BindView(R.id.text_view_gravity_z)
    TextView mGravityZ;

    @BindView(R.id.table_gyroscope)
    TableLayout mTableGyroscope;
    @BindView(R.id.text_view_gyroscope_error)
    TextView mGyroscopeError;
    @BindView(R.id.text_view_gyroscope_x)
    TextView mGyroscopeX;
    @BindView(R.id.text_view_gyroscope_y)
    TextView mGyroscopeY;
    @BindView(R.id.text_view_gyroscope_z)
    TextView mGyroscopeZ;

    @BindView(R.id.table_light)
    TableLayout mTableLight;
    @BindView(R.id.text_view_light_error)
    TextView mLightError;
    @BindView(R.id.text_view_light_value)
    TextView mLightValue;

    @BindView(R.id.table_linear_acceleration)
    TableLayout mTableLinearAcceleration;
    @BindView(R.id.text_view_linear_acceleration_error)
    TextView mLinearAccelerationError;
    @BindView(R.id.text_view_linear_acceleration_x)
    TextView mLinearAccelerationX;
    @BindView(R.id.text_view_linear_acceleration_y)
    TextView mLinearAccelerationY;
    @BindView(R.id.text_view_linear_acceleration_z)
    TextView mLinearAccelerationZ;

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
        initializeAccelerometerSensor();
        initializeGravitySensor();
        initializeGyroscopeSensor();
        initializeLightSensor();
        initializeLinearAccelerationSensor();
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

    private void initializeAccelerometerSensor() {
        mAccelerometerSensor = null;
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (listSensors.size() == 0) {
            setAccelerometerError(null);
        } else {
            mAccelerometerSensor = listSensors.get(0);
            if (mAccelerometerSensor == null) {
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

    //region [Gravity]

    private void initializeGravitySensor() {
        mGravitySensor = null;
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_GRAVITY);
        if (listSensors.size() == 0) {
            setGravityError(null);
        } else {
            mGravitySensor = listSensors.get(0);
            if (mGravitySensor == null) {
                setGravityError(mContext.getString(R.string.unknown_error));
            }
        }
    }

    private void setGravityError(String errorMessage) {
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
            mGravityError.setText(errorMessage.trim());
        }
        mTableGravity.setVisibility(View.GONE);
        mGravityError.setVisibility(View.VISIBLE);
    }

    //endregion

    //region [Gyroscope]

    private void initializeGyroscopeSensor() {
        mGyroscopeSensor = null;
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        if (listSensors.size() == 0) {
            setGyroscopeError(null);
        } else {
            mGyroscopeSensor = listSensors.get(0);
            if (mGyroscopeSensor == null) {
                setGyroscopeError(mContext.getString(R.string.unknown_error));
            }
        }
    }

    private void setGyroscopeError(String errorMessage) {
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
            mGyroscopeError.setText(errorMessage.trim());
        }
        mTableGyroscope.setVisibility(View.GONE);
        mGyroscopeError.setVisibility(View.VISIBLE);
    }

    //endregion

    //region [Light]

    private void initializeLightSensor() {
        mLightSensor = null;
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
        if (listSensors.size() == 0) {
            setLightError(null);
        } else {
            mLightSensor = listSensors.get(0);
            if (mLightSensor == null) {
                setLightError(mContext.getString(R.string.unknown_error));
            }
        }
    }

    private void setLightError(String errorMessage) {
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
            mLightError.setText(errorMessage.trim());
        }
        mTableLight.setVisibility(View.GONE);
        mLightError.setVisibility(View.VISIBLE);
    }

    //endregion

    //region [Gyroscope]

    private void initializeLinearAccelerationSensor() {
        mLinearAccelerationSensor = null;
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
        if (listSensors.size() == 0) {
            setLinearAccelerationError(null);
        } else {
            mLinearAccelerationSensor = listSensors.get(0);
            if (mGyroscopeSensor == null) {
                setLinearAccelerationError(mContext.getString(R.string.unknown_error));
            }
        }
    }

    private void setLinearAccelerationError(String errorMessage) {
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
            mGyroscopeError.setText(errorMessage.trim());
        }
        mTableGyroscope.setVisibility(View.GONE);
        mGyroscopeError.setVisibility(View.VISIBLE);
    }

    //endregion


    //region [Sensor general]

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerX.setText(String.format(Locale.getDefault(), "%f", event.values[0]));
                mAccelerometerY.setText(String.format(Locale.getDefault(), "%f", event.values[1]));
                mAccelerometerZ.setText(String.format(Locale.getDefault(), "%f", event.values[2]));
                break;
            case Sensor.TYPE_GRAVITY:
                mGravityX.setText(String.format(Locale.getDefault(), "%f", event.values[0]));
                mGravityY.setText(String.format(Locale.getDefault(), "%f", event.values[1]));
                mGravityZ.setText(String.format(Locale.getDefault(), "%f", event.values[2]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                mGyroscopeX.setText(String.format(Locale.getDefault(), "%f", event.values[0]));
                mGyroscopeY.setText(String.format(Locale.getDefault(), "%f", event.values[1]));
                mGyroscopeZ.setText(String.format(Locale.getDefault(), "%f", event.values[2]));
                break;
            case Sensor.TYPE_LIGHT:
                mLightValue.setText(String.format(Locale.getDefault(), "%f", event.values[0]));
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                mLinearAccelerationX.setText(String.format(Locale.getDefault(), "%f", event.values[0]));
                mLinearAccelerationY.setText(String.format(Locale.getDefault(), "%f", event.values[1]));
                mLinearAccelerationZ.setText(String.format(Locale.getDefault(), "%f", event.values[2]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void registerSensorListeners() {
        if (mAccelerometerSensor != null)
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (mGravitySensor != null)
            mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (mGyroscopeSensor != null)
            mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (mLightSensor != null)
            mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (mLinearAccelerationSensor != null)
            mSensorManager.registerListener(this, mLinearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterSensorListeners() {
        mSensorManager.unregisterListener(this);
    }

    //endregion
}

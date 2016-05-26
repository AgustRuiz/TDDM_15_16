package es.agustruiz.tddm.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;
import es.agustruiz.tddm.ui.activity.MainActivity;

public class GeopositionFragment extends Fragment implements MainActivity.OnFabClickListener {

    public static final String LOG_TAG = GeopositionFragment.class.getName() + "[A]";

    private final int REQUEST_PERMISSION_FINE_LOCATION_STATE = 1;

    @BindView(R.id.spinner_config_location_mode)
    MaterialBetterSpinner mSpinnerConfigLocationMode;
    private Integer mLocationMode = null;
    private static final int LOCATION_MODE_PROVIDER = R.string.title_by_location_provider;
    private static final int LOCATION_MODE_CRITERIA = R.string.title_by_criteria;

    @BindView(R.id.linear_layout_config_location)
    LinearLayoutCompat mLinearLayoutConfigLocation;

    @BindView(R.id.linear_layout_config_criteria)
    LinearLayoutCompat mLinearLayoutConfigCriteria;

    @BindView(R.id.spinner_location_provider)
    MaterialBetterSpinner mSpinnerLocationProvider;

    @BindView(R.id.spinner_power_criteria)
    MaterialBetterSpinner mSpinnerPowerCriteria;

    @BindView(R.id.spinner_accuracy_criteria)
    MaterialBetterSpinner mSpinnerAccuracyCriteria;

    @BindView(R.id.text_view_latitude)
    TextView mTextViewLatitude;

    @BindView(R.id.text_view_longitude)
    TextView mTextViewLongitude;

    @BindView(R.id.text_view_accuracy)
    TextView mTextViewAccuracy;

    @BindView(R.id.text_view_altitude)
    TextView mTextViewAltitude;

    @BindView(R.id.text_view_bearing)
    TextView mTextViewBearing;

    @BindView(R.id.text_view_speed)
    TextView mTextViewSpeed;

    List<String> mListLocationProvider;
    Map<Integer, Integer> mPowerCriteriaMap;
    Map<Integer, Integer> mAccuracyCriteriaMap;

    ArrayAdapter mSpinnerConfigLocationModeAdapter;
    ArrayAdapter mSpinnerLocationProviderAdapter;
    ArrayAdapter mSpinnerPowerCriteriaAdapter;
    ArrayAdapter mSpinnerAccuracyCriteriaAdapter;

    LocationManager mLocationManager;
    LocationProvider mLocationProvider;
    LocationListener mLocationListener;

    Context mContext;

    //region [Fragment methods]

    public GeopositionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geoposition, container, false);
        ButterKnife.bind(this, view);
        initialize();
        loadConfigModeSpinner();
        loadLocationProviderSpinner();
        loadPowerCriteriaSpinner();
        loadAccuracyCriteriaSpinner();
        return view;
    }

    //endregion

    //region [Private methods]

    private void initialize() {
        mContext = getContext();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

    }

    private void loadConfigModeSpinner() {
        final String[] configModeArray = {
                mContext.getString(LOCATION_MODE_PROVIDER),
                mContext.getString(LOCATION_MODE_CRITERIA)};
        mSpinnerConfigLocationModeAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_spinner_dropdown_item,
                configModeArray);
        mSpinnerConfigLocationMode.setAdapter(mSpinnerConfigLocationModeAdapter);
        mSpinnerConfigLocationMode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(getContext().getString(LOCATION_MODE_PROVIDER))) {
                    switchModeProvider();
                } else if (s.toString().equals(getContext().getString(LOCATION_MODE_CRITERIA))) {
                    switchModeCriteria();
                }
            }
        }); // setOnItemSelectedListener not working with MaterialBetterSpinner
    }

    private void loadLocationProviderSpinner() {
        if (checkLocationPermission()) {
            mListLocationProvider = mLocationManager.getProviders(true);
            String[] providersArray = mListLocationProvider.toArray(new String[mListLocationProvider.size()]);
            mSpinnerLocationProviderAdapter = new ArrayAdapter<>(
                    mContext,
                    android.R.layout.simple_dropdown_item_1line,
                    providersArray);
            mSpinnerLocationProvider.setAdapter(mSpinnerLocationProviderAdapter);
        } else {
            showMessage("Check permissions");
        }
    }

    private void loadPowerCriteriaMap() {
        if (mPowerCriteriaMap == null) {
            mPowerCriteriaMap = new LinkedHashMap<>();
        } else {
            mPowerCriteriaMap.clear();
        }
        mPowerCriteriaMap.put(R.string.power_high, Criteria.POWER_HIGH);
        mPowerCriteriaMap.put(R.string.power_medium, Criteria.POWER_MEDIUM);
        mPowerCriteriaMap.put(R.string.power_low, Criteria.POWER_LOW);
    }

    private void loadPowerCriteriaSpinner() {
        loadPowerCriteriaMap();
        List<String> powerCriteriaKeyArray = new ArrayList<>();
        for (Integer id : mPowerCriteriaMap.keySet()) {
            powerCriteriaKeyArray.add(mContext.getString(id));
        }
        mSpinnerPowerCriteriaAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_dropdown_item_1line,
                powerCriteriaKeyArray);
        mSpinnerPowerCriteria.setAdapter(mSpinnerPowerCriteriaAdapter);
    }

    private void loadAccuracyCriteriaMap() {
        if (mAccuracyCriteriaMap == null) {
            mAccuracyCriteriaMap = new LinkedHashMap<>();
        } else {
            mAccuracyCriteriaMap.clear();
        }
        mAccuracyCriteriaMap.put(R.string.accuracy_fine, Criteria.ACCURACY_FINE);
        mAccuracyCriteriaMap.put(R.string.accuracy_high, Criteria.ACCURACY_HIGH);
        mAccuracyCriteriaMap.put(R.string.accuracy_medium, Criteria.ACCURACY_MEDIUM);
        mAccuracyCriteriaMap.put(R.string.accuracy_low, Criteria.ACCURACY_LOW);
        mAccuracyCriteriaMap.put(R.string.accuracy_coarse, Criteria.ACCURACY_COARSE);
    }

    private void loadAccuracyCriteriaSpinner() {
        loadAccuracyCriteriaMap();
        List<String> accuracyCriteriaKeyArray = new ArrayList<>();
        for (Integer id : mAccuracyCriteriaMap.keySet()) {
            accuracyCriteriaKeyArray.add(mContext.getString(id));
        }
        mSpinnerAccuracyCriteriaAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_dropdown_item_1line,
                accuracyCriteriaKeyArray);
        mSpinnerAccuracyCriteria.setAdapter(mSpinnerAccuracyCriteriaAdapter);
    }

    private void resetValues() {
        String defaultValue = mContext.getString(R.string.unknown);
        mTextViewLatitude.setText(defaultValue);
        mTextViewLongitude.setText(defaultValue);
        mTextViewAccuracy.setText(defaultValue);
        mTextViewAltitude.setText(defaultValue);
        mTextViewBearing.setText(defaultValue);
        mTextViewSpeed.setText(defaultValue);
    }

    private void checkUnsuportedValues(LocationProvider locationProvider) {
        if (!locationProvider.supportsAltitude()) {
            mTextViewAltitude.setText(mContext.getString(R.string.unsuported));
        }
        if (!locationProvider.supportsBearing()) {
            mTextViewBearing.setText(mContext.getString(R.string.unsuported));
        }
        if (!locationProvider.supportsSpeed()) {
            mTextViewSpeed.setText(mContext.getString(R.string.unsuported));
        }
    }

    private void updateValues(Location location) {
        mTextViewLatitude.setText(String.format("%s", location.getLatitude()));
        mTextViewLongitude.setText(String.format("%s", location.getLongitude()));
        mTextViewAccuracy.setText(String.format("%s", location.getAccuracy()));
        if (location.hasAltitude())
            mTextViewAltitude.setText(String.format("%s", location.getAltitude()));
        if (location.hasBearing())
            mTextViewBearing.setText(String.format("%s", location.getBearing()));
        if (location.hasSpeed())
            mTextViewSpeed.setText(String.format("%s", location.getSpeed()));
    }

    private void switchModeProvider() {
        mLocationMode = LOCATION_MODE_PROVIDER;
        mLinearLayoutConfigLocation.setVisibility(View.VISIBLE);
        mLinearLayoutConfigCriteria.setVisibility(View.GONE);
    }

    private void switchModeCriteria() {
        mLocationMode = LOCATION_MODE_CRITERIA;
        mLinearLayoutConfigLocation.setVisibility(View.GONE);
        mLinearLayoutConfigCriteria.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void startGeolocation() {
        if (mLocationMode == null) {
            showMessage(mContext.getString(R.string.msg_check_configuration));
        } else {
            switch (mLocationMode) {
                case LOCATION_MODE_PROVIDER:
                    String provider = mSpinnerLocationProvider.getText().toString();
                    if (provider.isEmpty()) {
                        showMessage(mContext.getString(R.string.msg_location_provider_not_selected));
                    } else {
                        resetValues();
                        mLocationProvider = mLocationManager.getProvider(provider);
                        if (mLocationProvider == null) {
                            resetValues();
                            showMessage(mContext.getString(R.string.msg_error_location_provider));
                        } else {
                            checkUnsuportedValues(mLocationProvider);
                            if (checkLocationPermission()) {
                                mLocationListener = new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        updateValues(location);
                                    }

                                    @Override
                                    public void onStatusChanged(String provider, int status, Bundle extras) {
                                    }

                                    @Override
                                    public void onProviderEnabled(String provider) {
                                        showMessage(mContext.getString(R.string.msg_provider_enabled, provider));
                                    }

                                    @Override
                                    public void onProviderDisabled(String provider) {
                                        showMessage(mContext.getString(R.string.msg_provider_disabled, provider));
                                    }
                                };
                                mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
                            } else {
                                showMessage("Check permissions");
                            }
                        }
                    }
                    break;
                case LOCATION_MODE_CRITERIA:
                    showMessage("Not implemented yet");
                    break;
            }
        }
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_STATE);
            return false;
        } else {
            return true;
        }
    }

    //endregion

    //region [MainActivity.OnFabClickListener methods]

    @Override
    public void onFabClick() {
        Log.d(LOG_TAG, "onFabClick()");
        if (mLocationProvider == null) {
            showMessage("Starting");
            startGeolocation();
        } else {
            if (checkLocationPermission()) {
                showMessage("Stopping");
                mLocationManager.removeUpdates(mLocationListener);
                mLocationProvider = null;
                resetValues();
            }
        }
    }

    //endregion

}

package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import es.agustruiz.tddm.service.Permission;
import es.agustruiz.tddm.ui.activity.MainActivity;

public class GeopositionFragment extends Fragment implements MainActivity.OnFabClickListener {

    //public static final String LOG_TAG = GeopositionFragment.class.getName() + "[A]";

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

    @BindView(R.id.card_location_details)
    CardView mCardLocationDetails;

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

    boolean mIsLocationListenerOn = false;
    private static final String IS_LOCATION_LISTENER_ON_TAG = "mIsLocationListenerOn";

    Context mContext;

    //region [Fragment methods]

    public GeopositionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            mIsLocationListenerOn = savedInstanceState.getBoolean(IS_LOCATION_LISTENER_ON_TAG);
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_LOCATION_LISTENER_ON_TAG, mIsLocationListenerOn);
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
        if (Permission.getInstance().checkLocationPermission(mContext, getActivity())) {
            mListLocationProvider = mLocationManager.getProviders(true);
            String[] providersArray = mListLocationProvider.toArray(new String[mListLocationProvider.size()]);
            mSpinnerLocationProviderAdapter = new ArrayAdapter<>(
                    mContext,
                    android.R.layout.simple_dropdown_item_1line,
                    providersArray);
            mSpinnerLocationProvider.setAdapter(mSpinnerLocationProviderAdapter);
        } else {
            showMessage(mContext.getString(R.string.msg_check_permissions));
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
        String defaultValue = mContext.getString(R.string.no_data);
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
            String provider;
            switch (mLocationMode) {
                case LOCATION_MODE_PROVIDER:
                    provider = mSpinnerLocationProvider.getText().toString();
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
                            if (Permission.getInstance().checkLocationPermission(mContext, getActivity())) {
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
                                mCardLocationDetails.setVisibility(View.VISIBLE);
                                mIsLocationListenerOn=true;
                                showMessage(mContext.getString(R.string.msg_starting_with_provider, provider));

                            } else {
                                showMessage(mContext.getString(R.string.msg_check_permissions));
                            }
                        }
                    }
                    break;
                case LOCATION_MODE_CRITERIA:
                    String powerCriteriaString = mSpinnerPowerCriteria.getText().toString();
                    String accuracyCriteriaString = mSpinnerAccuracyCriteria.getText().toString();
                    if (!powerCriteriaString.isEmpty() && !accuracyCriteriaString.isEmpty()) {
                        Criteria criteria = new Criteria();
                        Integer powerCriteria = null;
                        for(Map.Entry<Integer, Integer> entry: mPowerCriteriaMap.entrySet()){
                            if(mContext.getString(entry.getKey()).equals(powerCriteriaString)){
                                powerCriteria = entry.getValue();
                                break;
                            }
                        }
                        if(powerCriteria!=null)
                            criteria.setPowerRequirement(powerCriteria);

                        Integer accuracyCriteria = null;
                        for(Map.Entry<Integer, Integer> entry: mAccuracyCriteriaMap.entrySet()){
                            if(mContext.getString(entry.getKey()).equals(accuracyCriteriaString)){
                                accuracyCriteria = entry.getValue();
                                break;
                            }
                        }
                        if(accuracyCriteria!=null)
                            criteria.setAccuracy(accuracyCriteria);

                        provider = mLocationManager.getBestProvider(criteria, true);
                        if(provider == null || provider.isEmpty()){
                            showMessage(mContext.getString(R.string.msg_no_location_found_by_criteria));
                        }else{
                            mLocationProvider = mLocationManager.getProvider(provider);
                            if (mLocationProvider == null) {
                                resetValues();
                                showMessage(mContext.getString(R.string.msg_error_location_provider));
                            } else {
                                checkUnsuportedValues(mLocationProvider);
                                if (Permission.getInstance().checkLocationPermission(mContext, getActivity())) {
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
                                    mCardLocationDetails.setVisibility(View.VISIBLE);
                                    mIsLocationListenerOn=true;
                                    showMessage(mContext.getString(R.string.msg_starting_with_provider, provider));
                                } else {
                                    showMessage(mContext.getString(R.string.msg_check_configuration));
                                }
                            }
                        }
                    } else {
                        showMessage(mContext.getString(R.string.msg_check_configuration));
                    }
                    break;
            }
        }
    }
    //endregion

    //region [MainActivity.OnFabClickListener methods]

    @Override
    public void onFabClick() {
        if (mLocationProvider == null) {
            startGeolocation();
        } else {
            if (Permission.getInstance().checkLocationPermission(mContext, getActivity())) {
                showMessage(mContext.getString(R.string.stopping));
                mLocationManager.removeUpdates(mLocationListener);
                mLocationProvider = null;
                mCardLocationDetails.setVisibility(View.GONE);
                resetValues();
                mIsLocationListenerOn=false;
            }
        }
    }

    //endregion

}

package es.agustruiz.tddm.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;

public class MainMenuFragment extends Fragment {

    public static final String LOG_TAG = MainMenuFragment.class.getName() + "[A]";

    @BindView(R.id.card_geoposition)
    CardView mCardGeoposition;

    @BindView(R.id.card_sensors)
    CardView mCardSensor;

    @BindView(R.id.card_video)
    CardView mCardVideo;

    @BindView(R.id.card_notification)
    CardView mCardNotification;

    //region [Fragment methods]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, view);
        initializeViews(inflater);
        return view;
    }

    //endregion

    //region [Private methods]

    private void initializeViews(LayoutInflater inflater) {
        mCardGeoposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Card Geoposition");
            }
        });

        mCardSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Card Sensors");
            }
        });

        mCardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Card Video");
            }
        });

        mCardNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Card Notification");
            }
        });
    }

    //endregion

}

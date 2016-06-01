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
    public static final char ITEM_GEOPOSITION = 1;

    @BindView(R.id.card_sensors)
    CardView mCardSensor;
    public static final char ITEM_SENSORS = 2;

    @BindView(R.id.card_video)
    CardView mCardVideo;
    public static final char ITEM_VIDEO = 3;

    @BindView(R.id.card_notification)
    CardView mCardNotification;
    public static final char ITEM_NOTIFICATION = 4;

    MenuClickListener mListener = null;

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

    //region [Public methods]

    public void addMenuClickListener(MenuClickListener listener){
        mListener = listener;
    }

    //endregion

    //region [Private methods]

    private void initializeViews(LayoutInflater inflater) {
        mCardGeoposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(ITEM_GEOPOSITION);
                }
            }
        });

        mCardSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(ITEM_SENSORS);
                }
            }
        });

        mCardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(ITEM_VIDEO);
                }
            }
        });

        mCardNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(ITEM_NOTIFICATION);
                }
            }
        });
    }

    //endregion

    //region [Interface MenuClickListener]

    public interface MenuClickListener{
        void onItemClick(char item);
    }

    //endregion

}

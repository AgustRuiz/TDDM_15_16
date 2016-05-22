package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.agustruiz.tddm.R;

public class SensorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //region [Fragment methods]

    public SensorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SensorFragment newInstance() {
        return new SensorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    //endregion

    //region [Interaction with activity]

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //endregion

    //region [Interface OnFragmentInteractionListener]

    public interface OnFragmentInteractionListener {
        void onSensorFragmentInteraction(Context context);
    }

    public void onFabPressed(Context context) {
        if (mListener != null) {
            mListener.onSensorFragmentInteraction(context);
        }
    }

    //endregion
}

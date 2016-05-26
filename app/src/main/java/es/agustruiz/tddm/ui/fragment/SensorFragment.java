package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import es.agustruiz.tddm.R;

public class SensorFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    //endregion
}

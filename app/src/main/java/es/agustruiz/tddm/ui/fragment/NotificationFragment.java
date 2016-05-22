package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.agustruiz.tddm.R;

public class NotificationFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //region [Fragment methods]

    public NotificationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
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
        void onNotificationFragmentInteraction(Context context);
    }

    public void onFabPressed(Context context) {
        if (mListener != null) {
            mListener.onNotificationFragmentInteraction(context);
        }
    }

    //endregion
}

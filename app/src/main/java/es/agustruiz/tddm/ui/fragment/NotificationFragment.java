package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;
import es.agustruiz.tddm.model.Notification;
import es.agustruiz.tddm.model.dao.NotificationDAO;
import es.agustruiz.tddm.ui.adapter.NotificationAdapter;

public class NotificationFragment extends Fragment {

    @BindView(R.id.notification_list_view)
    ListViewCompat mNotificationListView;

    private OnFragmentInteractionListener mListener;

    //region [Fragment methods]

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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        initializeViews(inflater);
        return view;
    }

    //endregion

    //region [Private methods]

    private void initializeViews(LayoutInflater inflater) {
        NotificationDAO notificationDAO = new NotificationDAO(getContext());
        notificationDAO.openReadOnly();
        List<Notification> mNotificationList = notificationDAO.getAll();
        notificationDAO.close();
        NotificationAdapter mNotificationAdapter = new NotificationAdapter(getContext(), inflater, R.layout.notification_list_row, mNotificationList);
        mNotificationListView.setAdapter(mNotificationAdapter);
        mNotificationListView.setSelector(android.R.color.transparent);
    }

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

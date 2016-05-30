package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import es.agustruiz.tddm.R;
import es.agustruiz.tddm.ui.activity.MainActivity;

public class VideoFragment extends Fragment implements MainActivity.OnFabClickListener {

    Context mContext;

    //region [Public methods]

    public VideoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    //endregion

    //region [Private methods]

    private void initialize(){
        mContext = getContext();
    }

    private void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    //endregion

    //region [MainActivity.OnFabClickListener methods]

    @Override
    public void onFabClick() {
        showMessage("FAB click");
    }

    //endregion
}

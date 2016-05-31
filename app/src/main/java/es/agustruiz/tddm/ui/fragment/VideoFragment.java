package es.agustruiz.tddm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;
import es.agustruiz.tddm.internet.DownloadVideoFromUrl;
import es.agustruiz.tddm.service.Permission;
import es.agustruiz.tddm.ui.activity.MainActivity;

public class VideoFragment extends Fragment implements MainActivity.OnFabClickListener {

    @BindView(R.id.video_status)
    TextView mTextViewStatus;

    @BindView(R.id.btn_video_download)
    Button mBtnVideoDownload;

    @BindView(R.id.progress_video_download)
    ProgressBar mProgressVideoDownload;

    @BindView(R.id.video_view)
    VideoView mVideoView;

    Context mContext;

    private final String VIDEO_URL = "http://agustruiz.es/dev/tddm_video/PollenAlert.mp4";
    private final String STORAGE_VIDEO_NAME = "PollenAlert.mp4";
    private final String STORAGE_VIDEO_RELATIVE_PATH = "TDDM_video";

    private final char VIDEO_STATUS_UNDEFINED = 0;
    private final char VIDEO_STATUS_NOT_FOUND = 1;
    private final char VIDEO_STATUS_DOWNLOADING = 2;
    private final char VIDEO_STATUS_OK = 3;
    private char mVideoStatus = VIDEO_STATUS_UNDEFINED;
    private final String VIDEO_STATUS_TAG = "mVideoStatus";

    private int mVideoPosition;
    private final String VIDEO_POSITION_TAG = "mVideoPosition";

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
        if (savedInstanceState != null) {
            mVideoStatus = savedInstanceState.getChar(VIDEO_STATUS_TAG);
            mVideoView.seekTo(savedInstanceState.getInt(VIDEO_POSITION_TAG));
            switchVideoDownloadedStatus();
        } else {
            initializeVideoStatus();
        }
        initialize();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putChar(VIDEO_STATUS_TAG, mVideoStatus);
        outState.putInt(VIDEO_POSITION_TAG, mVideoView.getCurrentPosition());
    }

    //endregion

    //region [Private methods]

    private void initializeVideoStatus() {
        File file = new File(DownloadVideoFromUrl.getRootPath() + "/" + STORAGE_VIDEO_RELATIVE_PATH + "/" + STORAGE_VIDEO_NAME);
        if (file.exists()) {
            mVideoStatus = VIDEO_STATUS_OK;
            setViewsVideoReady();
        } else {
            mVideoStatus = VIDEO_STATUS_NOT_FOUND;
            setViewsVideoNotFound();
        }
    }

    private void switchVideoDownloadedStatus(){
        switch (mVideoStatus) {
            case VIDEO_STATUS_OK:
                setViewsVideoReady();
                break;
            case VIDEO_STATUS_NOT_FOUND:
                setViewsVideoNotFound();
                break;
        }
    }

    private void setViewsVideoReady() {
        mTextViewStatus.setText(getString(R.string.msg_video_ready));
        mProgressVideoDownload.setVisibility(View.GONE);
        mBtnVideoDownload.setEnabled(false);
        mBtnVideoDownload.setVisibility(View.GONE);
        mBtnVideoDownload.setVisibility(View.GONE);
    }

    private void setViewsVideoNotFound() {
        mTextViewStatus.setText(getString(R.string.msg_download_video));
        mProgressVideoDownload.setVisibility(View.GONE);
        mBtnVideoDownload.setEnabled(true);
        mBtnVideoDownload.setVisibility(View.VISIBLE);
    }

    private void initialize() {
        mContext = getContext();

        mBtnVideoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Permission.getInstance().checkExternalStoragePermission(mContext, getActivity())) {
                    downloadVideo();
                }
            }
        });

        if (Permission.getInstance().checkExternalStoragePermission(mContext, getActivity())) {
            switchVideoDownloadedStatus();
        } else {
            mTextViewStatus.setText(getString(R.string.msg_no_storage_permission));
            mBtnVideoDownload.setVisibility(View.GONE);
        }

        if(mVideoStatus==VIDEO_STATUS_OK){
            mVideoView.setVideoPath(DownloadVideoFromUrl.getRootPath() + "/"
                    + STORAGE_VIDEO_RELATIVE_PATH + "/" + STORAGE_VIDEO_NAME);
            MediaController mediaController = new MediaController(mContext);
            mVideoView.setMediaController(mediaController);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void downloadVideo() {
        if (Permission.getInstance().checkExternalStoragePermission(mContext, getActivity())) {
            mProgressVideoDownload.setVisibility(View.VISIBLE);
            mTextViewStatus.setText(getString(R.string.msg_downloading_video));
            mProgressVideoDownload.setMax(DownloadVideoFromUrl.PROGRESS_MAX_VALUE);
            DownloadVideoFromUrl downloader = new DownloadVideoFromUrl(
                    new DownloadVideoFromUrl.DownloadProgressUpdateListener() {
                        @Override
                        public void onProgressUpdate(int progress) {
                            mBtnVideoDownload.setEnabled(false);
                            mProgressVideoDownload.setProgress(progress);
                            mVideoStatus = VIDEO_STATUS_DOWNLOADING;
                        }
                    },
                    new DownloadVideoFromUrl.DownloadFinishedListener() {
                        @Override
                        public void onPostExecute() {
                            setViewsVideoReady();
                            mVideoStatus = VIDEO_STATUS_OK;
                        }
                    }
            );
            downloader.execute(VIDEO_URL, STORAGE_VIDEO_RELATIVE_PATH, STORAGE_VIDEO_NAME);

        } else {
            Toast.makeText(mContext, "Need storage permission", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    //region [MainActivity.OnFabClickListener methods]

    @Override
    public void onFabClick() {
        showMessage("FAB click");
    }

    //endregion
}

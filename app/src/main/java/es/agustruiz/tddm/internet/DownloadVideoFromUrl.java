package es.agustruiz.tddm.internet;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadVideoFromUrl extends AsyncTask<String, Integer, Void> {

    public static final String LOG_TAG = DownloadVideoFromUrl.class.getName() + "[A]";
    public static int PROGRESS_MAX_VALUE = 100;

    protected final String VIDEO_PATH = "/TDDM_video";

    private DownloadProgressUpdateListener mProgressListener = null;
    private DownloadFinishedListener mFinishedListener = null;

    public DownloadVideoFromUrl(DownloadProgressUpdateListener updateListener,
                                DownloadFinishedListener finishedListener) {
        mProgressListener = updateListener;
        mFinishedListener = finishedListener;
    }

    //region [AsyncTask methods]

    @Override
    protected Void doInBackground(String... f_url) {
        int count;
        try {
            String root = getRootPath();

            System.out.println("Downloading");
            URL url = new URL(f_url[0]);

            URLConnection conection = url.openConnection();
            conection.connect();

            int lenghtOfFile = conection.getContentLength();


            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file

            String folderPath = root + "/" + f_url[1];
            File folder = new File(folderPath);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (success) {
                OutputStream output = new FileOutputStream(folderPath + "/" + f_url[2]);
                byte data[] = new byte[1024];
                long total = 0;
                int progressPublished = 0;
                int progressAux;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                    progressAux = Math.round((float) total / (float) lenghtOfFile * PROGRESS_MAX_VALUE);
                    if(progressAux!=progressPublished){
                        progressPublished = progressAux;
                        publishProgress(progressPublished);
                    }
                }
                output.flush();
                output.close();
            }
            input.close();

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error downloading ", e);
        }

        return null;

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d(LOG_TAG, "Download progres: " + values[0]);
        mProgressListener.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(LOG_TAG, "Downloaded!");
        mFinishedListener.onPostExecute();
    }

    public static String getRootPath(){
        return Environment.getExternalStorageDirectory().toString();
    }

    //endregion

    //region [Interface DownloadProgressUpdateListener]

    public interface DownloadProgressUpdateListener {
        void onProgressUpdate(int progress);
    }

    //endregion

    //region [Interface DownloadFinishedListener]

    public interface DownloadFinishedListener {
        void onPostExecute();
    }

    //endregion
}

package es.agustruiz.tddm.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class Permission {

    public final int REQUEST_PERMISSION_FINE_LOCATION_STATE = 1;
    public final int REQUEST_PERMISSION_WRITE_EXTERNAL_STATE = 2;

    private static Permission instance = new Permission();

    public static Permission getInstance(){
        return instance;
    }

    public boolean checkLocationPermission(Context context, Activity activity) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_STATE);
            return false;
        } else {
            return true;
        }
    }

    public boolean checkExternalStoragePermission(Context context, Activity activity) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STATE);
            return false;
        } else {
            return true;
        }
    }

}

package es.agustruiz.tddm.service.notification;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import es.agustruiz.tddm.R;

public class RegistrationService extends IntentService {

    public static final String LOG_TAG = RegistrationService.class.getName() + "[A]";
    public static final String SERVICE_NAME = "RegistrationService";

    private static final String PATH_MY_LITTLE_TOPIC = "/topics/my_little_topic";

    public RegistrationService() {
        super(SERVICE_NAME);
        //mContext = getApplicationContext();
    }

    //region [IntentService methods]

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        String token;
        GcmPubSub subscription = GcmPubSub.getInstance(getApplicationContext());
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            subscription.subscribe(token, PATH_MY_LITTLE_TOPIC, null);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Subscription error", e);
        }
    }

    //endregion
}

package es.agustruiz.tddm.service.notification;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class TokenRefreshListenerService extends InstanceIDListenerService {

    //region [InstanceIDListenerService overriden methods]

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        Intent intent = new Intent(this, RegistrationService.class);
        startService(intent);
    }

    //endregion
}

package es.agustruiz.tddm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import es.agustruiz.tddm.service.notification.RegistrationService;

public class SplashActivity extends AppCompatActivity {

    public static final String LOG_TAG = SplashActivity.class.getName() + "[A]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentRegistrationService = new Intent(this, RegistrationService.class);
        startService(intentRegistrationService);

        Intent intentMainActivity = new Intent(this, MainActivity.class);
        startActivity(intentMainActivity);

        finish();
    }
}

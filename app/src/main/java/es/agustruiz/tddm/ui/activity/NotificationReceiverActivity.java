package es.agustruiz.tddm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import es.agustruiz.tddm.R;
import es.agustruiz.tddm.model.Notification;
import es.agustruiz.tddm.model.dao.NotificationDAO;

public class NotificationReceiverActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName() + "[A]";

    private static final String KEY_TITLE_TAG = "title";
    private static final String KEY_MESSAGE_TAG = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(KEY_TITLE_TAG) && intent.hasExtra(KEY_MESSAGE_TAG)) {
            String title = intent.getStringExtra(KEY_TITLE_TAG);
            String message = intent.getStringExtra(KEY_MESSAGE_TAG);
            addToDb(title, message);
        }

        Intent intentMainActivity = new Intent(this, MainActivity.class);
        startActivity(intentMainActivity);

    }

    private void addToDb(String title, String message){
        NotificationDAO notificationDAO = new NotificationDAO(getApplicationContext());
        notificationDAO.openWritable();
        notificationDAO.add(new Notification(null, title, message, System.currentTimeMillis()));
        notificationDAO.close();
    }

}

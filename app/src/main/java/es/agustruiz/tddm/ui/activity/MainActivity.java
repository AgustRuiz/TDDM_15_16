package es.agustruiz.tddm.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;
import es.agustruiz.tddm.ui.fragment.GeopositionFragment;
import es.agustruiz.tddm.ui.fragment.NotificationFragment;
import es.agustruiz.tddm.ui.fragment.SensorFragment;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName() + "[A]";

    private static final char FRAGMENT_MODE_EMPTY = 0;
    private static final char FRAGMENT_MODE_GEOPOSITION = 1;
    private static final char FRAGMENT_MODE_SENSOR = 2;
    private static final char FRAGMENT_MODE_NOTIFICATION = 3;
    private char mFragmentMode = FRAGMENT_MODE_EMPTY;
    private static final String FRAGMENT_MODE_TAG = "mFragmentMode";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.main_frame_layout_container)
    FrameLayout mContainerFragment;

    private Context mContext = null;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private static final String FRAGMENT_TAG = "mFragment";

    private OnFabClickListener onFabClickListener = null;

    //region [Public methods]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        if(savedInstanceState!=null) {
            mFragmentMode = savedInstanceState.getChar(FRAGMENT_MODE_TAG);
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
            switch (mFragmentMode){
                case FRAGMENT_MODE_GEOPOSITION:
                    addOnFabClickListener((GeopositionFragment) mFragment);
                    fabGeopositionMode();
                    break;
                case FRAGMENT_MODE_SENSOR:
                    //addOnFabClickListener((SensorFragment) mFragment);
                    fabSensorMode();
                    break;
                case FRAGMENT_MODE_NOTIFICATION:
                    //addOnFabClickListener((NotificationFragment) mFragment);
                    fabNotificationMode();
                    break;
                default:
                    Log.d(LOG_TAG, "Nothing here");
            }
        }
        initialize();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putChar(FRAGMENT_MODE_TAG, mFragmentMode);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //endregion

    //region [Private methods]

    private void initialize() {
        setSupportActionBar(mToolbar);
        initializeViews();
        initializeDrawer();
        mFragmentManager = getSupportFragmentManager();
    }

    private void initializeViews() {
        switch (mFragmentMode){
            case FRAGMENT_MODE_GEOPOSITION:
                fabGeopositionMode();
                break;
            case FRAGMENT_MODE_SENSOR:
                fabSensorMode();
                break;
            case FRAGMENT_MODE_NOTIFICATION:
                fabNotificationMode();
                break;
            default:
                mFab.setVisibility(View.GONE);
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mFragmentMode) {
                    case FRAGMENT_MODE_GEOPOSITION:
                        if(onFabClickListener!=null){
                            onFabClickListener.onFabClick();
                        }
                        break;
                    case FRAGMENT_MODE_SENSOR:
                    case FRAGMENT_MODE_NOTIFICATION:
                    default:
                        //Snackbar.make(view, "Fragment not loaded...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    private void initializeDrawer() {
        assert mDrawer != null;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_geolocation:
                        if (mFragmentMode != FRAGMENT_MODE_GEOPOSITION) {
                            mFragmentMode = FRAGMENT_MODE_GEOPOSITION;
                            createGeolocationFragment();
                            fabGeopositionMode();
                        }
                        break;
                    case R.id.nav_sensor:
                        if (mFragmentMode != FRAGMENT_MODE_SENSOR) {
                            mFragmentMode = FRAGMENT_MODE_SENSOR;
                            createSensorFragment();
                            fabSensorMode();
                        }
                        break;
                    case R.id.nav_notification:
                        if (mFragmentMode != FRAGMENT_MODE_NOTIFICATION) {
                            mFragmentMode = FRAGMENT_MODE_NOTIFICATION;
                            createNotificationFragment();
                            fabNotificationMode();
                        }
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void fabGeopositionMode(){
        Drawable drawableIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_location_on_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawableIcon.setTint(ContextCompat.getColor(mContext, android.R.color.white));
        }else{
            drawableIcon = drawableIcon.mutate();
            DrawableCompat.setTint(drawableIcon, ContextCompat.getColor(mContext, android.R.color.white));
        }
        mFab.setImageDrawable(drawableIcon);
        mFab.show();
    }

    private void fabSensorMode(){
        mFab.hide();
    }

    private void fabNotificationMode(){
        mFab.hide();
    }

    //endregion

    //region [Fragments method]

    private void fragmentTransaction() {

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        try {
            fragmentTransaction.add(R.id.main_frame_layout_container, mFragment);
        } catch (IllegalStateException e) {
            fragmentTransaction.remove(mFragment);
        }
        fragmentTransaction.commit();
    }

    private void createGeolocationFragment() {
        mContainerFragment.removeAllViews();
        mFragment = new GeopositionFragment();
        addOnFabClickListener((GeopositionFragment) mFragment);
        fragmentTransaction();
    }

    private void createSensorFragment() {
        mContainerFragment.removeAllViews();
        mFragment = new SensorFragment();
        fragmentTransaction();
    }

    private void createNotificationFragment() {
        mContainerFragment.removeAllViews();
        mFragment = new NotificationFragment();
        fragmentTransaction();
    }

    //endregion

    public interface OnFabClickListener {
        void onFabClick();
    }

    public void addOnFabClickListener(OnFabClickListener listener) {
        onFabClickListener = listener;
    }

}

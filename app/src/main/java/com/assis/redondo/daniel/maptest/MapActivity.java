package com.assis.redondo.daniel.maptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.assis.redondo.daniel.maptest.data.controller.DriverLocationController;
import com.assis.redondo.daniel.maptest.data.loader.LastLocationsLoader;
import com.assis.redondo.daniel.maptest.data.vo.LocationVO;
import com.assis.redondo.daniel.maptest.dispatcher.DataEvent;
import com.assis.redondo.daniel.maptest.dispatcher.Event;
import com.assis.redondo.daniel.maptest.dispatcher.EventListener;
import com.assis.redondo.daniel.maptest.utils.AlertManager;
import com.assis.redondo.daniel.maptest.utils.MapConstants;
import com.assis.redondo.daniel.maptest.utils.Utils;
import com.assis.redondo.daniel.maptest.utils.dialogs.DialogManager;
import com.assis.redondo.daniel.maptest.utils.toolbar.ToolBarHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MapActivity.class.getSimpleName();

    private MapView mMapView;
    private LatLng LatLong;
    private GoogleApiClient mClient;
    private boolean mIsMapLoaded;
    private AlertManager mAlertManager;
    private Handler mHandler;

    private ToolBarHelper topbarHolder;
    private DriverLocationController mDriverLocationController;
    private DialogManager mDialogManager;
    private HashMap<Integer, Marker> mMarkerReference;

    private ArrayList<LocationVO> mDriversLastLocations;
    private EventListener lastLocationsListener = new EventListener() {

        @Override
        public void onEvent(Event event) {
            LastLocationsLoader driversLoader = mDriverLocationController.getDriversLastLocationsLoader();

            if (driversLoader.loadStatus == DriverLocationController.LoadStatus.LOADED) {
                mDialogManager.dismissProgressDialog();

                if(mDriverLocationController.getDriversLoadedLocations() != null) {
                    mDriversLastLocations.addAll(mDriverLocationController.getDriversLoadedLocations());
                }

                addDriversMarkers();
                mAlertManager.addReloadLocationsAlarm();

                mHandler.removeCallbacks(reloadDriversRunnable);
                mHandler.postDelayed(reloadDriversRunnable, 5000);

            } else if (driversLoader.loadStatus == DriverLocationController.LoadStatus.ERROR) {
                mDialogManager.dismissProgressDialog();
                mDialogManager.showErrorFromApi();

            }
        }
    };
    private Button reloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        topbarHolder = new ToolBarHelper(getApplicationContext());
        topbarHolder.init(this);
        topbarHolder.setHomeNavigationIcon();
        topbarHolder.setTitle(getResources().getString(R.string.map_title));

        mDriversLastLocations = new ArrayList<>();
        mMarkerReference = new HashMap<>();
        mHandler = new Handler();

        mDriverLocationController = DriverLocationController.getInstance(this);
        mDialogManager = new DialogManager(this);
        mAlertManager = new AlertManager(this);

        reloadButton = (Button) findViewById(R.id.reloadButton);
        if(mMapView == null) {
            createMapView(savedInstanceState);
        }
    }

    public void createMapView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mapLayout);
        mMapView.onCreate(null);
        mMapView.getMapAsync(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        mHandler.removeCallbacks(reloadDriversRunnable);
        mAlertManager.removeReloadAlarm();
        mDriverLocationController.removeListener(DataEvent.POPULATE_DRIVERS_LAST_LOCATIONS, lastLocationsListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mDriverLocationController.addListener(DataEvent.POPULATE_DRIVERS_LAST_LOCATIONS, lastLocationsListener);
        mDriverLocationController.getDriversLastLocations();
        mDialogManager.showProgressDialog(this);
        mMapView.onResume();

        reloadButton.setVisibility(View.GONE);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDriverLocationController.getDriversLastLocationsLoader().reloadDriversLocations();

            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mClient = new GoogleApiClient.Builder(MapActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClient.connect();

        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        googleMap.setPadding(0, padding, 0, 0); //ignore "curve"
        mIsMapLoaded = true;


    }

    @Override
    public void onLocationChanged(Location location) {
        zoomIn(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    public void zoomIn(double Lat, double Long) {
        try {

            if(Lat == 0 && Long == 0){

                LatLng LatLongNorthEast = new LatLng(MapConstants.BrazilNorthEastLat, MapConstants.BrazilNorthEastLong);
                LatLng LatLongSouthWest = new LatLng(MapConstants.BrazilSouhtWestLat, MapConstants.BrazilSouhtWestLong);
                try {
                    LatLngBounds bounds = new LatLngBounds (LatLongSouthWest, LatLongNorthEast);
                    int padding = 0;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMapView.getMap().animateCamera(cu, 1, null);
                } catch(Exception e){

                }
            } else {

                double homeMapAlignment = Lat;
                LatLong = new LatLng(homeMapAlignment, Long);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(LatLong)
                        .zoom(14)
                        .build();
                if (mMapView != null) {
                    mMapView.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1, null);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override

    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mClient);
        if(location!=null) {
            zoomIn(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "API Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w(TAG, "API Connection failed");
    }

    public void addDriversMarkers() {


        if (mDriversLastLocations != null && mDriversLastLocations.size() > 0) {


            final ArrayList<LocationVO> tempDriversLocations = new ArrayList<>(mDriversLastLocations);


            final View customMarker = ((LayoutInflater) MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
            ImageView imageViewMarker = (ImageView) customMarker.findViewById(R.id.imageTaxi);

            Bitmap bmp = Utils.createDrawableFromView(MapActivity.this, customMarker);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bmp);

            try {
                if (mMapView != null) for (LocationVO driver : tempDriversLocations) {

                    if (mMarkerReference.containsKey(driver.getDriverId())) {
                            changeMarkerPosition(mMarkerReference.get(driver.getDriverId()), driver.getLatLngPos(), false);
                        } else {

                            Marker marker = mMapView.getMap().addMarker(new MarkerOptions()
                                    .icon(icon)
                                    .position(new LatLng(driver.getLatitude(), driver.getLongitude()))
                                    .title(Integer.toString(driver.getDriverId())));

                            mMarkerReference.put(driver.getDriverId(), marker);

                        }

                    mMapView.getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public Runnable reloadDriversRunnable = new Runnable (){
        public void run() {
            mDriverLocationController.getDriversLastLocations();
        }
    };

    public void changeMarkerPosition(final Marker marker, final LatLng toPosition,
                                     final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMapView.getMap().getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

}

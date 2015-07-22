package com.leili.imhere.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leili.imhere.R;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

/**
 * Main Test Activity
 * Created by lei.li on 7/13/15.
 */
public class HomeActivity extends Activity implements LocationListener, TencentMap.OnMarkerDraggedListener {
    static final double
            DP_LAT = 31.217239, DP_LNG = 121.415648,
            KUNMING_LAT = 25.042060, KUNMING_LNG = 102.711182;
    static LatLng
            DIAN_PING = new LatLng(DP_LAT, DP_LNG),
            KUNMING = new LatLng(KUNMING_LAT, KUNMING_LNG);

    double chosenLat = DP_LAT, chosenLng = DP_LNG;

    TextView tvTitle;
    Button btnDp;
    MapView mapView;
    TencentMap tencentMap;
    LocationManager locationManager;
    String providerName = LocationManager.GPS_PROVIDER;

    Marker dpMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.map_fragment);
        initData();
        initView();
        addMarkers();
        moveToDp();
        startMockLocation();
    }

    private void initData() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(providerName, false, true, false, false, true, true,
                true, 0, 5);
        locationManager.setTestProviderEnabled(providerName, true);
        locationManager.requestLocationUpdates(providerName, 0, 0, this);
    }

    private void startMockLocation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    setLocation(KUNMING_LAT, KUNMING_LNG);
                    setLocation(chosenLat, chosenLng);
                }
            }
        }).start();
    }
    private void setLocation(double latitude, double longitude) {
        Location location = new Location(providerName);
        location.setTime(System.currentTimeMillis());
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAltitude(2.0f);
        location.setAccuracy(3.0f);
        locationManager.setTestProviderLocation(providerName, location);
    }

    private void moveToDp() {
        tencentMap.animateTo(DIAN_PING);
    }
    private void addMarkers() {
        dpMarker = tencentMap.addMarker(new MarkerOptions()
                        .position(DIAN_PING)
                        .title("选择位置")
                        .snippet("安化路492号")
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.defaultMarker())
                        .draggable(true)
        );
        dpMarker.showInfoWindow();
    }

    private void initView() {
        mapView = (MapView) super.findViewById(R.id.map_view);
        btnDp = (Button) super.findViewById(R.id.btn_dp);
        btnDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tencentMap.animateTo(DIAN_PING);
                dpMarker.setPosition(DIAN_PING);
                updateMarkerSnippetAndTitle(dpMarker);
            }
        });
        tencentMap = mapView.getMap();
        tencentMap.setOnMarkerDraggedListener(this);
        tvTitle = (TextView) super.findViewById(R.id.tv_title);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        updateMarkerSnippetAndTitle(marker);
    }

    private void updateMarkerSnippetAndTitle(Marker marker) {
        chosenLat = marker.getPosition().getLatitude();
        chosenLng = marker.getPosition().getLongitude();
        String posString = marker.getPosition().toString();
        tvTitle.setText(posString);
        marker.setSnippet(posString);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
}

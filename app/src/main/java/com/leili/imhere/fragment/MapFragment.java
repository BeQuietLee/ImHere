package com.leili.imhere.fragment;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by lei.li on 7/22/15.
 */
public class MapFragment extends Fragment implements LocationListener, TencentMap.OnMarkerDraggedListener, TencentMap.OnMapClickListener {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        addMarkers();
        moveToDp();
        startMockLocation();
    }

    private void initData() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        mapView = (MapView) getView().findViewById(R.id.map_view);
        btnDp = (Button) getView().findViewById(R.id.btn_dp);
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
        tencentMap.setOnMapClickListener(this);
        tvTitle = (TextView) getView().findViewById(R.id.tv_title);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        tencentMap.animateTo(latLng);
        dpMarker.setPosition(latLng);
        updateMarkerSnippetAndTitle(dpMarker);
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
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }
}

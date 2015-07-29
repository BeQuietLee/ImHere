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
import com.leili.imhere.entity.Position;
import com.leili.imhere.utils.MapUtils;
import com.leili.imhere.utils.ViewUtils;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import de.greenrobot.event.EventBus;

import static com.leili.imhere.event.Event.LocatePositionEvent;

/**
 * Created by lei.li on 7/22/15.
 */
public class MapFragment extends Fragment implements LocationListener, TencentMap.OnMarkerDraggedListener, TencentMap.OnMapClickListener, TencentMap.OnMapCameraChangeListener {
    static final double
            DP_LAT = 31.216089, DP_LNG = 121.420586;
    private static final int LOG_WINDOW_COUNT = 5;
    static LatLng
            DIAN_PING = new LatLng(DP_LAT, DP_LNG);

    double
            chosenLat = DP_LAT, chosenLng = DP_LNG,
            wgsLat, wgsLng;

    TextView tvTitle;
    MapView mapView;
    private Button btnZoomIn, btnZoomOut; // 放大、缩小
    private ViewGroup logWindow;
    TencentMap tencentMap;
    LocationManager locationManager;
    String providerName = LocationManager.GPS_PROVIDER;

    Marker dpMarker;

    private View.OnClickListener zoomListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int zoomLevel = tencentMap.getZoomLevel();
            if (v == btnZoomIn) { // 放大
                if (++zoomLevel > tencentMap.getMaxZoomLevel()) {
                    ViewUtils.toast(getActivity(), "已到最大缩放级别");
                    return;
                }
            } else if (v == btnZoomOut) { // 缩小
                if (--zoomLevel < tencentMap.getMinZoomLevel()) {
                    ViewUtils.toast(getActivity(), "已到最小缩放级别");
                    return;
                }
            }
            tencentMap.setZoom(zoomLevel);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
                    double[] wgsLngLat = MapUtils.GCJ2WGS_exact(chosenLng, chosenLat);
                    wgsLat = wgsLngLat[1];
                    wgsLng = wgsLngLat[0];
                    setLocation(wgsLat, wgsLng);
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

    public void onEventMainThread(LocatePositionEvent event) {
        Position position = event.getPosition();
        updateMarkerByPosition(position);
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
        logWindow = (ViewGroup) getView().findViewById(R.id.operate_log_layout);
        tencentMap = mapView.getMap();
        tencentMap.setOnMarkerDraggedListener(this);
        tencentMap.setOnMapClickListener(this);
        tencentMap.setOnMapCameraChangeListener(this);
        tvTitle = (TextView) getView().findViewById(R.id.tv_title);
        btnZoomIn = (Button) getView().findViewById(R.id.zoom_in);
        btnZoomIn.setOnClickListener(zoomListener);
        btnZoomOut = (Button) getView().findViewById(R.id.zoom_out);
        btnZoomOut.setOnClickListener(zoomListener);
    }

    private void addLogToWindow(String log) {
        TextView tv = new TextView(getActivity());
        tv.setText(log);
        if (logWindow.getChildCount() >= LOG_WINDOW_COUNT) {
            logWindow.removeViewAt(0);
        }
        logWindow.addView(tv);
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
        addLogToWindow("onMarkerDragEnd");
    }

    private void updateMarkerSnippetAndTitle(Marker marker) {
        chosenLat = marker.getPosition().getLatitude();
        chosenLng = marker.getPosition().getLongitude();
        String posString = marker.getPosition().toString();
        tvTitle.setText(posString);
        marker.setTitle("选择位置");
        marker.setSnippet(posString);
    }

    /**
     * 根据Position更新marker位置与描述<br>
     *     同时调整Camera
     * @param position 位置
     */
    private void updateMarkerByPosition(Position position) {
        chosenLat = position.getLatitude();
        chosenLng = position.getLongitude();
        dpMarker.setTitle(position.getTitle());
        dpMarker.setSnippet(position.getAddress());
        dpMarker.showInfoWindow();
        LatLng latLng = new LatLng(chosenLat, chosenLng);
        dpMarker.setPosition(latLng);
        tencentMap.animateTo(latLng);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        addLogToWindow("onCameraChangeFinish");
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
        EventBus.getDefault().unregister(this);
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }
}

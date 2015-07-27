package com.leili.imhere.entity;

import com.tencent.lbssearch.object.result.SearchResultObject;

/**
 * Created by Lei.Li on 7/24/15 11:05 AM.
 */
public class Position {
    private String tencentId;
    private String title;
    private String address;
    private double latitude;
    private double longitude;

    private Position() {}

    public static Position from(String tencentId, String title, String address, double latitude, double longitude) {
        Position position = new Position();
        position.tencentId = tencentId;
        position.title = title;
        position.address = address;
        position.latitude = latitude;
        position.longitude = longitude;
        return position;
    }

    public static Position from(SearchResultObject.SearchResultData data) {
        Position item = new Position();
        item.tencentId = data.id;
        item.title = data.title;
        item.address = data.address;
        item.latitude = data.location.lat;
        item.longitude = data.location.lng;
        return item;
    }

    public String getTencentId() {
        return tencentId;
    }

    public void setTencentId(String tencentId) {
        this.tencentId = tencentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

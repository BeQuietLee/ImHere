package com.leili.imhere.entity;

import com.tencent.lbssearch.object.result.SearchResultObject;

/**
 * Created by Lei.Li on 7/24/15 11:05 AM.
 */
public class SearchResultItem {
    private String title;
    private String address;
    private double latitude;
    private double longitude;

    public static SearchResultItem from(SearchResultObject.SearchResultData data) {
        SearchResultItem item = new SearchResultItem();
        item.title = data.title;
        item.address = data.address;
        item.latitude = data.location.lat;
        item.longitude = data.location.lng;
        return item;
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

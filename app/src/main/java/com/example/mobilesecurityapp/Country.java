package com.example.mobilesecurityapp;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class Country {

    private String neighborhood;
    private String city;
    private String state;
    private String countryIsoCode;
    private Double longitude;
    private Double latitude;

    public Country() {
    }

    public Country(String neighborhood, String city, String state, String countryIsoCode, Double latitude, Double longitude) {
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.countryIsoCode = countryIsoCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isValid() {
        return ObjectUtils.allNotNull(this.longitude, this.latitude)
                && StringUtils.isNoneBlank(this.state, this.countryIsoCode)
                && !StringUtils.isAllBlank(this.neighborhood, this.city);
    }

    @Override
    public @NonNull String toString() {
        String str = null;
        if (StringUtils.isNotEmpty(this.city)) {
            str = this.city;
        }
        else {
            str = this.neighborhood;
        }
        return MessageFormat.format("{0}, {1}, {2}", str, state, countryIsoCode);
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}

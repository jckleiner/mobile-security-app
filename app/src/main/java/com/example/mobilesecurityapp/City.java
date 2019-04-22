package com.example.mobilesecurityapp;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

class City {

    private String neighborhood;
    private String name;
    private String state;
    private String countryIsoCode;
    private Double latitude;
    private Double longitude;
    private WeatherInfo weatherInfo;

    public City() {
    }

    City(String neighborhood, String name, String state, String countryIsoCode, Double latitude, Double longitude) {
        this.neighborhood = neighborhood;
        this.name = name;
        this.state = state;
        this.countryIsoCode = countryIsoCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    boolean isValid() {
        return ObjectUtils.allNotNull(this.longitude, this.latitude)
                && StringUtils.isNoneBlank(this.state, this.countryIsoCode)
                && !StringUtils.isAllBlank(this.neighborhood, this.name);
    }

    @Override
    public @NonNull String toString() {
        String str = null;
        if (StringUtils.isNotEmpty(this.neighborhood)) {
            str = this.neighborhood;
        }
        else {
            str = this.name;
        }
        return MessageFormat.format("{0}, {1}, {2}", str, state, countryIsoCode);
    }

    WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    String getNeighborhood() {
        return neighborhood;
    }

    void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    String getName() {
        return name;
    }

    String getState() {
        return state;
    }

    String getCountryIsoCode() {
        return countryIsoCode;
    }

    Double getLongitude() {
        return longitude;
    }

    Double getLatitude() {
        return latitude;
    }

    void setName(String name) {
        this.name = name;
    }

    void setState(String state) {
        this.state = state;
    }

    void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}

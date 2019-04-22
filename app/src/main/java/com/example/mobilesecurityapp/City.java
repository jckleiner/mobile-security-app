package com.example.mobilesecurityapp;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class City {

    private String neighborhood;
    private String name;
    private String state;
    private String countryIsoCode;
    private Double latitude;
    private Double longitude;
    private WeatherInfo weatherInfo;

    public City() {
    }

    public City(String neighborhood, String name, String state, String countryIsoCode, Double latitude, Double longitude) {
        this.neighborhood = neighborhood;
        this.name = name;
        this.state = state;
        this.countryIsoCode = countryIsoCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isValid() {
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

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
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

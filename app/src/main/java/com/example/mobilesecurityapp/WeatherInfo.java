package com.example.mobilesecurityapp;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class WeatherInfo {

    private String weatherCondition;
    private String temperature;
    private String feelsLikeTemperature;
    private String relativeHumidity;
    private String windSpeed;

    // TODO add weather quality fields when its implemented

    public WeatherInfo() {
    }

    public WeatherInfo(String weatherCondition, String temperature, String feelsLikeTemperature, String relativeHumidity, String windSpeed) {
        this.weatherCondition = weatherCondition;
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.relativeHumidity = relativeHumidity;
        this.windSpeed = windSpeed;
    }

    @Override
    public @NonNull String toString() {
        return MessageFormat.format("The weather today is {0}.\nTemperature: {1} C\nFeels like: {2} C\nHumidity: {3} %\nWind speed: {4} km/h\nAir quality: ...",
                weatherCondition, temperature, feelsLikeTemperature, relativeHumidity, windSpeed);
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(String feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}

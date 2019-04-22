package com.example.mobilesecurityapp;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;

class WeatherInfo {

    private String weatherCondition;
    private String temperature;
    private String feelsLikeTemperature;
    private String relativeHumidity;
    private String windSpeed;

    // TODO add weather quality fields when its implemented

    public WeatherInfo() {
    }

    WeatherInfo(String weatherCondition, String temperature, String feelsLikeTemperature, String relativeHumidity, String windSpeed) {
        this.weatherCondition = weatherCondition;
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.relativeHumidity = relativeHumidity;
        this.windSpeed = windSpeed;
    }

    @Override
    public @NonNull String toString() {
        return MessageFormat.format("The weather today is {0}.\nTemperature: {1} C\nFeels like: {2} C\nHumidity: {3} %\nWind speed: {4} km/h",
                weatherCondition, temperature, feelsLikeTemperature, relativeHumidity, getWindSpeed());
    }

    String getWeatherCondition() {
        return weatherCondition;
    }

    void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    String getTemperature() {
        return temperature;
    }

    void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    String getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    void setFeelsLikeTemperature(String feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    String getRelativeHumidity() {
        return relativeHumidity;
    }

    void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    String getWindSpeed() {
        try {
            BigDecimal bd = new BigDecimal(windSpeed);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bd.toString();

        }
        catch (NumberFormatException e) {
            return windSpeed;
        }
    }

    void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}

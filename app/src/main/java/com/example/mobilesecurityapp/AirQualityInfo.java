package com.example.mobilesecurityapp;

import android.support.annotation.NonNull;

import java.text.MessageFormat;

class AirQualityInfo {

    private Integer airQualityIndex;
    private String airQualityDescription;
    private String dominantPollutant;

    AirQualityInfo(Integer airQualityIndex, String airQualityDescription, String dominantPollutant) {
        this.airQualityIndex = airQualityIndex;
        this.airQualityDescription = airQualityDescription;
        this.dominantPollutant = dominantPollutant;
    }

    @Override
    public @NonNull
    String toString() {
        return MessageFormat.format("Air quality: {0}\nAir quality index: {1}",
                getAirQualityDescription(), getAirQualityIndexString());
    }

    String getAirQualityIndexString() {
        Integer display = airQualityIndex != null ? airQualityIndex : 0;
        return MessageFormat.format("{0}.{1}", display / 10, display % 10);
    }

    Integer getAirQualityIndex() { return airQualityIndex; }

    void setAirQualityIndex(Integer airQualityIndex) { this.airQualityIndex = airQualityIndex; }



    String getAirQualityDescription() { return airQualityDescription != null ? airQualityDescription.split(" ")[0].toLowerCase() : null; }

    void setAirQualityDescription(String airQualityDescription) { this.airQualityDescription = airQualityDescription; }

    String getDominantPollutant() { return dominantPollutant; }

    void setDominantPollutant(String dominantPollutant) { this.dominantPollutant = dominantPollutant; }
}

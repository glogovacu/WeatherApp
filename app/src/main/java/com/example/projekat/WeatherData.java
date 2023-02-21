package com.example.projekat;

public class WeatherData {
    private String timezone;
    private String timezone_abbreviation;
    private double elevation;
    private CurrentWeather CurrentWeather;

    public com.example.projekat.CurrentWeather getCurrentWeather() {
        return CurrentWeather;
    }

    public void setCurrentWeather(com.example.projekat.CurrentWeather currentWeather) {
        CurrentWeather = currentWeather;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_abbreviation() {
        return timezone_abbreviation;
    }

    public void setTimezone_abbreviation(String timezone_abbreviation) {
        this.timezone_abbreviation = timezone_abbreviation;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }


}

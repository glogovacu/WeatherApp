package com.example.projekat;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenMeteoApiClient {
    private static final String OPEN_METEO_API_ENDPOINT = "https://api.open-meteo.com/v1/forecast";

    public static void getWeatherForecast(double latitude, double longitude, WeatherForecastListener listener) {
        new AsyncTask<Double, Void, String>() {
            @Override
            protected String doInBackground(Double... params) {
                try {
                    double latitude = params[0];
                    double longitude = params[1];
                    URL url = new URL(OPEN_METEO_API_ENDPOINT + "?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                listener.onWeatherForecastReceived(result);
            }
        }.execute(latitude, longitude);
    }

    public interface WeatherForecastListener {
        void onWeatherForecastReceived(String weatherForecast);
    }
}

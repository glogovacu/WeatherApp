package com.example.projekat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenMeteoApiHourlyClient {
    private static final String OPEN_METEO_API_ENDPOINT = "https://api.open-meteo.com/v1/forecast";

    public static void getHourlyForecast(double latitude, double longitude, String startDate, String endDate, HourlyForecastListener listener) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    double latitude = Double.parseDouble(params[0]);
                    double longitude = Double.parseDouble(params[1]);
                    String startDate = params[2];
                    String endDate = params[3];

                    URL url = new URL(OPEN_METEO_API_ENDPOINT + "?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m&hourly=weathercode&timeformat=unixtime" + "&start_date=" + startDate + "&end_date=" + endDate);
                    Log.d("TAG", "Brate: " + url);
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
                listener.onHourlyForecastReceived(result);
            }
        }.execute(Double.toString(latitude), Double.toString(longitude), startDate, endDate);
    }

    public interface HourlyForecastListener {
        void onHourlyForecastReceived(String hourlyForecast);
    }
}

package com.example.projekat;

import static com.example.projekat.OpenMeteoApiClient.getWeatherForecast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class MainActivity extends AppCompatActivity implements OpenMeteoApiClient.WeatherForecastListener {
    private TextView txtString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //txtString = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMeteoApiClient.getWeatherForecast(44.77829603241415, 20.474693407054083, MainActivity.this);
            }
        });
    }

    @Override
    public void onWeatherForecastReceived(String weatherForecast) {
        String jsonString = weatherForecast;
        txtString.setText(weatherForecast);
        System.out.println(weatherForecast);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject currentWeatherObject = jsonObject.getJSONObject("current_weather");
            double temperature = currentWeatherObject.getDouble("temperature");
            double windSpeed = currentWeatherObject.getDouble("windspeed");
            double windDirection = currentWeatherObject.getDouble("winddirection");
            int weatherCode = currentWeatherObject.getInt("weathercode");
            String time = currentWeatherObject.getString("time");


            //TextView txtTemperature = findViewById(R.id.textTemperature);
            //TextView txtTime = findViewById(R.id.txtTime);
            //txtTemperature.setText(String.valueOf(temperature) + "°C");
           // txtTime.setText(time);
        } catch (JSONException e) {

            e.printStackTrace();

        }

    }
}

package com.example.projekat;

import static com.example.projekat.OpenMeteoApiClient.getWeatherForecast;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements OpenMeteoApiClient.WeatherForecastListener, OpenMeteoApiHourlyClient.HourlyForecastListener{
    TextView[] txtTempSat = new TextView[3];
    ImageView[] imageWeather = new ImageView[3];
    TextView[] txtVreme = new TextView[3];
    TextView txtgrad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        txtTempSat[0] = findViewById(R.id.textTempSat2);
        txtTempSat[1] = findViewById(R.id.textTempSat3);
        txtTempSat[2] = findViewById(R.id.textTempSat4);
        imageWeather[0] = findViewById(R.id.imageSat2);
        imageWeather[1] = findViewById(R.id.imageSat3);
        imageWeather[2] = findViewById(R.id.imageSat4);
        EditText editTextLatitude = findViewById(R.id.editTextLatitude);
        EditText editTextLongitude = findViewById(R.id.editTextLongitude);
        Button button = findViewById(R.id.button);
        txtVreme[0] = findViewById(R.id.textSat1);
        txtVreme[1] = findViewById(R.id.textSat2);
        txtVreme[2] = findViewById(R.id.textSat3);
        txtgrad = findViewById(R.id.textView);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String currentDateString = dateFormat.format(currentDate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitudeString = editTextLatitude.getText().toString().trim();
                String longitudeString = editTextLongitude.getText().toString().trim();
                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);
                if (!latitudeString.isEmpty() && !longitudeString.isEmpty()) {
                    try {
                        if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
                            OpenMeteoApiClient.getWeatherForecast(latitude, longitude, MainActivity.this);
                            OpenMeteoApiHourlyClient.getHourlyForecast(latitude,longitude,currentDateString, currentDateString, MainActivity.this);
                            try {
                                // Get the address from the location
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                                if ( addresses != null && addresses.size() > 0) {
                                    // Get the city name from the address
                                    String city = addresses.get(0).getLocality();
                                    Log.d("TAG", "City: " + city);
                                    txtgrad.setText(city);

                                }
                                else {
                                    txtgrad.setText("Unknown");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error: Latitude or Longitude must be between -90 and 90.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Error: Invalid Latitude or Longitude value", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error: Latitude or Longitude cant be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        OpenMeteoApiClient.getWeatherForecast(44.8, 20.4, MainActivity.this);
        OpenMeteoApiHourlyClient.getHourlyForecast(44.8,20.4,currentDateString, currentDateString, MainActivity.this);

    }

    @Override
    public void onWeatherForecastReceived(String weatherForecast) {
        String jsonString = weatherForecast;
        System.out.println(weatherForecast);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject currentWeatherObject = jsonObject.getJSONObject("current_weather");
            double temperature = currentWeatherObject.getDouble("temperature");
            double windSpeed = currentWeatherObject.getDouble("windspeed");
            double windDirection = currentWeatherObject.getDouble("winddirection");
            int weatherCode = currentWeatherObject.getInt("weathercode");
            String time = currentWeatherObject.getString("time");


            TextView txtTrenutneTemp = findViewById(R.id.textTempSad);
            TextView txtSadTemp = findViewById(R.id.textTempSat1);
            txtTrenutneTemp.setText(String.valueOf(temperature) + "°C");
            txtSadTemp.setText(String.valueOf(temperature) + "°C");
            ImageView imageSad = findViewById(R.id.imageSad);
            TextView txtVremenskePrilike = findViewById(R.id.textVremenskePrilike);
            if (weatherCode == 0) {
                txtVremenskePrilike.setText("Clear Sky");
                imageSad.setImageResource(R.drawable.sun);
            } else if (weatherCode == 1) {
                txtVremenskePrilike.setText("Mainly clear");
                imageSad.setImageResource(R.drawable.cloudy);
            } else if (weatherCode == 2) {
                txtVremenskePrilike.setText("Partly cloudy");
                imageSad.setImageResource(R.drawable.cloudy);
            } else if (weatherCode == 3) {
                txtVremenskePrilike.setText("Overcast");
                imageSad.setImageResource(R.drawable.cloudy);
            } else {
                txtVremenskePrilike.setText("Rain");
                imageSad.setImageResource(R.drawable.kisa);
            }
        } catch (JSONException e) {

            e.printStackTrace();

        }

    }
    @Override
    public void onHourlyForecastReceived(String hourlyForecast) {
        // implementation for hourly forecast receiver
        String apiResponse = hourlyForecast;
        try {
            JSONObject jsonObject = new JSONObject(apiResponse);
            JSONObject hourly = jsonObject.getJSONObject("hourly");

            JSONArray timeArray = hourly.getJSONArray("time");
            JSONArray temperatureArray = hourly.getJSONArray("temperature_2m");
            JSONArray weatherCodeArray = hourly.getJSONArray("weathercode");
            List<String> timeList = new ArrayList<>();
            int currentIndex = -1;

            for (int i = 0; i < timeArray.length(); i++) {
                long unixTime = timeArray.getLong(i);
                Date date = new Date(unixTime * 1000L); // Convert unixtime to Java Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); // Set the time format
                String formattedTime = dateFormat.format(date); // Format the time
                timeList.add(formattedTime); // Add the formatted time to the list

                // Check if the current time is equal to the time in the array
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                if (currentHour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                    currentIndex = i;
                }
            }
            for (int i = 0; i < 3; i++) {
                txtTempSat[i].setText(String.valueOf(temperatureArray.getDouble(currentIndex + i+1))+"°C");
                txtVreme[i].setText(timeList.get(currentIndex + i+1));
                if (weatherCodeArray.getInt(currentIndex+i+1) == 0) {
                    imageWeather[i].setImageResource(R.drawable.sun);
                } else if (weatherCodeArray.getInt(currentIndex +i+1)  == 1) {
                    imageWeather[i].setImageResource(R.drawable.cloudy);
                } else if (weatherCodeArray.getInt(currentIndex+i+1)  == 2) {
                    imageWeather[i].setImageResource(R.drawable.cloudy);
                } else if (weatherCodeArray.getInt(currentIndex+i+1)  == 3) {
                    imageWeather[i].setImageResource(R.drawable.cloudy);
                } else {
                    imageWeather[i].setImageResource(R.drawable.kisa);
                }
            }


        }catch (JSONException e) {

            e.printStackTrace();

        }
    }
}

package com.example.weather_app_test;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private TextView textViewWeather;

    private static final String API_KEY = "e561305ed089e4c25ccd64098a3b156a";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        textViewWeather = findViewById(R.id.textViewWeather);

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                if (!city.isEmpty()) {
                    getWeatherData(city);
                }
            }
        });
    }

    private void getWeatherData(final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(String.format(API_URL, city, API_KEY));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder json = new StringBuilder(1024);
                    String tmp;
                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    JSONObject data = new JSONObject(json.toString());
                    final double temperature = data.getJSONObject("main").getDouble("temp");
                    final String description = data.getJSONArray("weather").getJSONObject(0).getString("description");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String weatherInfo = String.format("Temperature: %.1f°C\nDescription: %s", temperature, description);
                            textViewWeather.setText(weatherInfo);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
}
}

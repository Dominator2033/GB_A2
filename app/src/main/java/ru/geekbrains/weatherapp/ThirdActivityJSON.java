package ru.geekbrains.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class ThirdActivityJSON extends AppCompatActivity {
    private final Handler handler = new Handler();
    private TextView cityField, updatedField, weatherIcon, detailsField;
    private TextView minTemp, maxTemp, currentTemperatureField, detailsWeatherField;
    private EditText cityFromMainActivity;
    private Button startServiceBtn, stopServiceBtn;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_json);
        initViews();
        cityFromMainActivity.setText(Objects.requireNonNull(getIntent().getExtras()).getString("test"));
        updateWeatherData(cityFromMainActivity.getText().toString());
        str = cityFromMainActivity.getText().toString();
        startService(str);
        stopService();
    }

    public void initViews() {
        cityField = findViewById(R.id.city_field);
        cityFromMainActivity = findViewById(R.id.city_from_main_activity);
        updatedField = findViewById(R.id.updated_field);
        weatherIcon = findViewById(R.id.weather_icon);
        currentTemperatureField = findViewById(R.id.current_temperature_field);
        detailsField = findViewById(R.id.details_field);
        maxTemp = findViewById(R.id.temp_max);
        minTemp = findViewById(R.id.temp_min);
        detailsWeatherField = findViewById(R.id.details_weather);
        startServiceBtn = findViewById(R.id.start_service);
        stopServiceBtn = findViewById(R.id.stop_service);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showInputDialog();
        return true;
    }

    public void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");

        final EditText input = new EditText(this);

        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateWeatherData(input.getText().toString());
                cityFromMainActivity.setText(input.getText().toString());
                startService(cityFromMainActivity.getText().toString());
            }
        });
        builder.show();
    }

    public void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getApplicationContext(), city);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.place_not_found, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);

            JSONObject main = jsonObject.getJSONObject("main");
            setPlaceName(jsonObject);
            setDetails(details, main);
            setTemp(main);
            setDate(jsonObject);

            setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = "\u2600";
                //icon = getString(R.string.weather_sunny);
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    icon = "\u2601";
                    break;
                }
            }
        }
        weatherIcon.setText(icon);
    }

    private void setDate(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
        String updateText = "Last update: " + updateOn;
        updatedField.setText(updateText);
    }

    private void setTemp(JSONObject main) throws JSONException {
        String mainTemp = Math.round(main.getDouble("temp")) + "\u00b0";
        String minTempStr = String.format("%.1f", main.getDouble("temp_min")) + "\u2103";
        String maxTempStr = String.format("%.1f", main.getDouble("temp_max")) + "\u2103";
        currentTemperatureField.setText(mainTemp);
        maxTemp.setText(maxTempStr);
        minTemp.setText(minTempStr);
    }

    private void setDetails(JSONObject details, JSONObject main) throws JSONException {
        String detailsText = "Влажность: " + main.getString("humidity") + "%\n"
                + "Давление: " + main.getString("pressure") + "hPa";

        String detailsWeather = details.getString("description").toUpperCase();
        detailsWeatherField.setText(detailsWeather);
        detailsField.setText(detailsText);
    }

    private void setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase();
        cityField.setText(cityText);
    }

    private void startService(final String strCityField) {
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivityJSON.this, OpenWeatherService.class);
                intent.putExtra("testOpen", strCityField);
                startService(intent);
            }
        });
    }

    private void stopService() {
        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivityJSON.this, OpenWeatherService.class);
                stopService(intent);
            }
        });
    }
}


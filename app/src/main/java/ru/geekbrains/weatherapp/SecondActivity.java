package ru.geekbrains.weatherapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    private Sensor sensorTemp, sensorHum;
    private SensorManager sensorManagerTemp, sensorManagerHum;
    private TextView textTemp, textCity, textDate, wetness, wind;
    private Group groupRain, groupSun, groupTemp;

    Button startServiceBtn, stopServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
        // передача названия города
        showNameCity();
        // показать или скрыть температуру
        showTemp();
        // показать или скрыть скорость ветра
        showSpeedWind();
        // показать или скрыть влажность
        showWetness();
        // показать дождь
        showRain();
        // показать солнце
        showSun();
        // вывод текущей даты
        showDate();

        sensorManagerHum = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManagerTemp = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorHum = sensorManagerHum.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorTemp = sensorManagerTemp.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        sensorManagerHum.registerListener(listenerHum, sensorHum, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManagerTemp.registerListener(listenerTemp, sensorTemp, SensorManager.SENSOR_DELAY_NORMAL);

        startService();
        stopService();
    }

    private void initViews() {
        startServiceBtn = findViewById(R.id.start_service);
        stopServiceBtn = findViewById(R.id.stop_service);
        textTemp = findViewById(R.id.temp);
        textCity = findViewById(R.id.city);
        textDate = findViewById(R.id.date_id);
        wetness = findViewById(R.id.wetness);
        wind = findViewById(R.id.wind_speed);
        groupRain = findViewById(R.id.group_rain);
        groupSun = findViewById(R.id.group_sun);
        groupTemp = findViewById(R.id.group);
    }

    private void startService() {
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherService.class);
                startService(intent);
            }
        });
    }

    private void stopService() {
        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherService.class);
                stopService(intent);
            }
        });
    }

    private void showHumSensor(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Влажность - ").append(Math.round(event.values[0])).append("%");
        wetness.setText(stringBuilder);
    }

    SensorEventListener listenerHum = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            showHumSensor(event);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void showTempSensor(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Math.round(event.values[0])).append(getResources().getString(R.string.degrees));
        textTemp.setText(stringBuilder);
    }

    SensorEventListener listenerTemp = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            showTempSensor(event);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sensorManagerHum.unregisterListener(listenerHum, sensorHum);
        sensorManagerTemp.unregisterListener(listenerTemp, sensorTemp);
    }

    private void showNameCity() {
        textCity.setText(Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.city_show)));
    }

    private void showDate() {
        DateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM, HH:mm", Locale.getDefault());
        textDate.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
    }

    // Показать влажность
    public void showWetness() {
        String wet = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.wetness_show));
        if (wet != null && wet.equals("View.VISIBLE")) {
            wetness.setVisibility(View.VISIBLE);
        } else {
            wetness.setVisibility(View.INVISIBLE);
        }
    }

    // Показать скорость ветра
    public void showSpeedWind() {
        String windSpeed = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.wind_show));
        if (windSpeed != null && windSpeed.equals("View.VISIBLE")) {
            wind.setVisibility(View.VISIBLE);
        } else {
            wind.setVisibility(View.INVISIBLE);
        }
    }

    // Будет ли дождь?
    public void showRain() {
        String str = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.rain_show));
        if (str != null && str.equals("View.VISIBLE")) {
            groupRain.setVisibility(View.VISIBLE);
        } else {
            groupRain.setVisibility(View.INVISIBLE);
        }
    }

    // Солнце
    public void showSun() {
        String str = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.sun_show));
        if (str != null && str.equals("View.VISIBLE")) {
            groupSun.setVisibility(View.VISIBLE);
        } else {
            groupSun.setVisibility(View.INVISIBLE);
        }
    }

    // Показать температуру
    public void showTemp() {
        String s = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.temp_show));
        if (s != null && s.equals("View.VISIBLE")) {
            groupTemp.setVisibility(View.VISIBLE);
        } else {
            groupTemp.setVisibility(View.INVISIBLE);
        }
    }
}

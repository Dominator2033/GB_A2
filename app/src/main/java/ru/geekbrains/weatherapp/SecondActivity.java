package ru.geekbrains.weatherapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    //Sensors
    private Sensor sensorTemp;
    private Sensor sensorHum;
    private SensorManager sensorManagerTemp;
    private SensorManager sensorManagerHum;
    private TextView textTemp;
    private TextView textHum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
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

        textHum = findViewById(R.id.wetness);
        textTemp = findViewById(R.id.temp);

        sensorManagerHum = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManagerTemp = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorHum = sensorManagerHum.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorTemp = sensorManagerTemp.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        sensorManagerHum.registerListener(listenerHum, sensorHum, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManagerTemp.registerListener(listenerTemp, sensorTemp, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void showHumSensor(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Влажность - ").append(Math.round(event.values[0])).append("%");
        textHum.setText(stringBuilder);
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
        TextView textView = findViewById(R.id.city);
        textView.setText(Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.city_show)));
    }

    private void showDate() {
        DateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM, HH:mm", Locale.getDefault());
        TextView textDate = findViewById(R.id.date_id);
        textDate.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
    }

    // Показать влажность
    public void showWetness() {
        TextView wetness = findViewById(R.id.wetness);
        String wet = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.wetness_show));
        if (wet != null && wet.equals("View.VISIBLE")) {
            wetness.setVisibility(View.VISIBLE);
        } else {
            wetness.setVisibility(View.INVISIBLE);
        }
    }

    // Показать скорость ветра
    public void showSpeedWind() {
        TextView wind = findViewById(R.id.wind_speed);
        String windSpeed = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.wind_show));
        if (windSpeed != null && windSpeed.equals("View.VISIBLE")) {
            wind.setVisibility(View.VISIBLE);
        } else {
            wind.setVisibility(View.INVISIBLE);
        }
    }

    // Будет ли дождь?
    public void showRain() {
        Group group = findViewById(R.id.group_rain);
        String str = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.rain_show));
        if (str != null && str.equals("View.VISIBLE")) {
            group.setVisibility(View.VISIBLE);
        } else {
            group.setVisibility(View.INVISIBLE);
        }
    }

    // Солнце
    public void showSun() {
        Group group = findViewById(R.id.group_sun);
        String str = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.sun_show));
        if (str != null && str.equals("View.VISIBLE")) {
            group.setVisibility(View.VISIBLE);
        } else {
            group.setVisibility(View.INVISIBLE);
        }
    }

    // Показать температуру
    public void showTemp() {
        Group group = findViewById(R.id.group);
        String s = Objects.requireNonNull(getIntent().getExtras()).getString(getResources().getString(R.string.temp_show));
        if (s != null && s.equals("View.VISIBLE")) {
            group.setVisibility(View.VISIBLE);
        } else {
            group.setVisibility(View.INVISIBLE);
        }
    }
}

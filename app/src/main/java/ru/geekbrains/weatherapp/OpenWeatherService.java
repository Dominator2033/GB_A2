package ru.geekbrains.weatherapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class OpenWeatherService extends IntentService {
    int messageId = 0;

    private String cityName;
    private final Handler handler = new Handler();
    private boolean running;

    public OpenWeatherService() {
        super("OpenService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        running = true;
        while (running) {
            cityName = Objects.requireNonNull(Objects.requireNonNull(intent).getExtras()).getString("testOpen");
            updateWeatherData(cityName);
            if (!cityName.equals(Objects.requireNonNull(intent.getExtras()).getString("testOpen"))) {
                updateWeatherData(cityName);
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        makeNote("Destroy");
        running = false;
    }

    private void makeNote(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Текущая температура")
                .setContentText(message);
        Intent resultIntent = new Intent(this, OpenWeatherService.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId, builder.build());
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
            JSONObject main = jsonObject.getJSONObject("main");
            setTemp(main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTemp(JSONObject main) throws JSONException {
        String mainTemp = main.getDouble("temp") + "\u00b0";
        makeNote(mainTemp);
    }
}

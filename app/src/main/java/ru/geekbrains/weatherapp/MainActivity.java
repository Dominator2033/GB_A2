package ru.geekbrains.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String str;
    private Switch aSwitch, rainSwitch, sunSwitch;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Button sendButton, jsonActivityBtn;
    private EditText editTextCity;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setSupportActionBar(toolbar);
        fab();
        drawer(toolbar);
        setSunSwitch();
        setRainSwitch();
        addCity();

        jsonActivity();
        //retrofitActivity();
    }

    public void initViews() {
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        sendButton = findViewById(R.id.send_btn);
        editTextCity = findViewById(R.id.editCityText);
        checkBox = findViewById(R.id.checkBox);
        aSwitch = findViewById(R.id.switch_for_wetness);
        sunSwitch = findViewById(R.id.switch_for_sun);
        rainSwitch = findViewById(R.id.switch_for_rain);
        aSwitch = findViewById(R.id.switch_for_speed);
        jsonActivityBtn = findViewById(R.id.third_activity_btn);
    }

    private void jsonActivity() {
        jsonActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivityJSON.class);
                intent.putExtra("test", editTextCity.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void drawer(Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void addCity() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWeatherParam();
            }
        });
    }

    private void setWeatherParam() {
        if (editTextCity != null) {
            str = editTextCity.getText().toString();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);

            windSpeed(intent);
            rainSwitch(intent);
            sunSwitch(intent);
            wetnessSwitch(intent);
            showTemp(intent);
            checkBtn(editTextCity, intent);
        }
    }

    public void checkBtn(EditText editText, Intent intent) {
        if (editText.getText().toString().equals("")) {
            getInfo(R.string.attention, R.string.fill_field);
        } else {
            intent.putExtra(getResources().getString(R.string.city_show), str);
            startActivity(intent);
        }
    }

    public void showTemp(Intent intent) {
        if (checkBox.isChecked()) {
            intent.putExtra(getResources().getString(R.string.temp_show), "View.VISIBLE");
        } else {
            intent.putExtra(getResources().getString(R.string.temp_show), "View.INVISIBLE");
        }
    }

    public void wetnessSwitch(Intent intent) {
        if (aSwitch.isChecked()) {
            intent.putExtra(getResources().getString(R.string.wetness_show), "View.VISIBLE");
        } else {
            intent.putExtra(getResources().getString(R.string.wetness_show), "View.INVISIBLE");
        }
    }

    public void rainSwitch(Intent intent) {
        if (rainSwitch.isChecked()) {
            intent.putExtra(getResources().getString(R.string.rain_show), "View.VISIBLE");
        } else {
            intent.putExtra(getResources().getString(R.string.rain_show), "View.INVISIBLE");
        }
    }

    public void sunSwitch(Intent intent) {
        if (sunSwitch.isChecked()) {
            intent.putExtra(getResources().getString(R.string.sun_show), "View.VISIBLE");
        } else {
            intent.putExtra(getResources().getString(R.string.sun_show), "View.INVISIBLE");
        }
    }

    public void setSunSwitch() {
        sunSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sunSwitch.isChecked()) {
                    rainSwitch.setChecked(false);
                }
            }
        });
    }

    public void setRainSwitch() {
        rainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rainSwitch.isChecked()) {
                    sunSwitch.setChecked(false);
                }
            }
        });
    }

    public void windSpeed(Intent intent) {
        if (aSwitch.isChecked()) {
            intent.putExtra(getResources().getString(R.string.wind_show), "View.VISIBLE");
        } else {
            intent.putExtra(getResources().getString(R.string.wind_show), "View.INVISIBLE");
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu_clear) {
            clearCity();
        }
        if (id == R.id.menu_add) {
            setWeatherParam();
        }
        if (id == R.id.menu_info) {
            Toast.makeText(this, "Вызов информации", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearCity() {
        if (editTextCity != null) {
            editTextCity.setText("");
            Toast.makeText(this, "Очистили поле город", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            callDefaultCamera();
        } else if (id == R.id.nav_gallery) {
            FragmentNavGallery fragmentNavGallery = new FragmentNavGallery();
            setFragmentInfo(fragmentNavGallery);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_dev) {
            getInfo(R.string.about_dev, R.string.about_dev_info);
        } else if (id == R.id.nav_sup) {
            FragmentNavSupport fragmentNavSupport = new FragmentNavSupport();
            setFragmentInfo(fragmentNavSupport);
        } else if (id == R.id.nav_contacts) {
            FragmentNavContacts fragmentNavContacts = new FragmentNavContacts();
            setFragmentInfo(fragmentNavContacts);
        } else if (id == R.id.nav_rate) {
            FragmentNavRate fragmentNavRate = new FragmentNavRate();
            setFragmentInfo(fragmentNavRate);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callDefaultCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void getInfo(int about_dev, int about_dev_info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(about_dev));
        builder.setMessage(getResources().getString(about_dev_info));
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setFragmentInfo(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

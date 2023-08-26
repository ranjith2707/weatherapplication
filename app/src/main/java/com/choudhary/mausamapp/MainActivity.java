package com.choudhary.mausamapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.choudhary.mausamapp.Models.Food;
import com.choudhary.mausamapp.Models.MausamData;
import com.choudhary.mausamapp.Models.Meals;
import com.choudhary.mausamapp.Models.Weather;
import com.choudhary.mausamapp.Models.main;
import com.choudhary.mausamapp.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class
MainActivity extends AppCompatActivity {


    ArrayList<Weather> list;

    ActivityMainBinding binding;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        constraintLayout = findViewById(R.id.constraint_layout);



        list = new ArrayList<>();


        SimpleDateFormat format = new SimpleDateFormat("dd MMMM YYYY");
        String currentDate = format.format(new Date());

        binding.date.setText(currentDate);

        requestPermissions();

        fetchweather("Indonesia");

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeybaord();

                if (TextUtils.isEmpty(binding.searchCityEdittext.getText())){
                    binding.searchCityEdittext.setError("Please your city name");
                    return;
                }

                String CITY_NAME =binding.searchCityEdittext.getText().toString().trim();
                fetchweather(CITY_NAME);
                binding.searchCityEdittext.setText("");
            }
        });



       //  fetchFood();



    }
    private void hideKeybaord() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(constraintLayout.getApplicationWindowToken(),0);
    }



    private void requestPermissions() {
        // below line is use to request permission in the current activity.
        // this method is use to handle error in runtime permissions
        Dexter.withActivity(this)
                // below line is use to request the number of permissions which are required in our app.
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        // below is the list of permissions
                        Manifest.permission.ACCESS_FINE_LOCATION
                      )
                // after adding permissions we are calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, we will show user a dialog message.
                            showSettingsDialog();

                          //  Toast.makeText(this,"Please Give Permission to read external storage and Location",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }


                }).withErrorListener(error -> {
            // we are displaying a toast message for error message.
            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
        })
                // below line is use to run the permissions on same thread and to check the permissions
                .onSameThread().check();
    }


    void fetchweather(String city){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);


        Call<MausamData>  call =    interfaceAPI.getData(city,"32f564017b84535cba5c517e48098fb8","metric");

        call.enqueue(new Callback<MausamData>() {
            @Override
            public void onResponse(Call<MausamData> call, Response<MausamData> response) {
                if (response.isSuccessful()){

                    MausamData mausamData = response.body();

                    Log.d("Th",Thread.currentThread().getName()+" ID" +" "+ Thread.currentThread().getId());


                    main  to = mausamData.getMain();



                    binding.mainTempValue.setText((to.getTemp()) + " \u2103");


                    binding.cityName.setText(mausamData.getName());
                    binding.maxTempValue.setText(String.valueOf(to.getTemp_max()) + " \u2103");
                    binding.minTempValue.setText(String.valueOf(to.getTemp_min()) + " \u2103");
                    binding.pressreValue.setText(String.valueOf(to.getPressure())+"Pa");
                    binding.humidityValue.setText(String.valueOf(to.getHumidity())+"g/m");

                    Log.d("MV",to.getFeels_like()+" " +to.getHumidity()+" "+to.getPressure()+" "+to.getTemp());

                    list = (ArrayList<Weather>) mausamData.getWeather();

                    for( Weather  weather : list ){

                        Log.d("MV2",weather.getId()+" "+weather.getMain()+" "+weather.getDescription());
                       binding.description.setText(weather.getDescription());
                    }

                    

                }
            }

            @Override
            public void onFailure(Call<MausamData> call, Throwable t) {

                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel();
            // below is the intent from which we are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // this method is called when user click on negative button.
            dialog.cancel();
        });
        // below line is used to display our dialog
        builder.show();
    }





}
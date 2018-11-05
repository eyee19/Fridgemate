package com.example.eyee3.fridgemate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);

        Log.d("SPLASH SCREEN", "SPLASH SCREEN SUCCESS");

        startActivity(intent);
        finish();
    }
}

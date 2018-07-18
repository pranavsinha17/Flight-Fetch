package com.example.blaze.blazeflight;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.blaze.blazeflight.Blaze;
import com.example.blaze.blazeflight.MainActivity;

public class SplashActivity extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=new Intent(this,Blaze.class);
        startActivity(i);
        finish();
    }
}


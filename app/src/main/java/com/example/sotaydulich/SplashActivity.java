package com.example.sotaydulich;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        delayedStartActivity();
    }


    public void delayedStartActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity();
            }
        }, 1000);
    }

    public void  startActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.silde_in_left, R.anim.silde_out_left);
        finish();
    }
}
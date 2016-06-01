package com.example.sriramhariharan.houstonsights;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private static boolean splashLoaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!splashLoaded) {
            setContentView(R.layout.activity_splash);
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, ChooseTour.class));
                    finish();
                }
            }, secondsDelayed * 200);

            splashLoaded = true;
        }
        else {
            Intent goToMainActivity = new Intent(SplashActivity.this, ChooseTour.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}

package com.example.sriramhariharan.houstonsights;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Finish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }

    public void restartTour(View view){
        Values.history = false;
        Values.culture = false;
        Values.art = false;
        Values.science = false;
        Values.food = false;
        Values.museum = false;
        Values.shopping = false;
        Values.park = false;
        Values.landmark = false;
        Values.spiritual = false;
        Values.entertainment  = false;
        Values.directiontype = "";
        Values.places = null;
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }



}

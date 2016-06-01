package com.example.sriramhariharan.houstonsights;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MoreInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("info");
        TextView ttxtv = (TextView)findViewById(R.id.infotitle);
        TextView dttv = (TextView)findViewById(R.id.infodescription);
        ttxtv.setText(title);
        dttv.setText(description);

    }
}

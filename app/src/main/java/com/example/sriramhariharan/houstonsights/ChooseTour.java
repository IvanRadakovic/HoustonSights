package com.example.sriramhariharan.houstonsights;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class ChooseTour extends AppCompatActivity {

    EditText edit;
    CheckBox everything;
    CheckBox history;
    CheckBox culture;
    CheckBox art;
    CheckBox science;
    CheckBox food;
    CheckBox museum;
    CheckBox shopping;
    CheckBox park;
    CheckBox landmark;
    CheckBox spiritual;
    CheckBox entertainment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tour);
        everything = (CheckBox)findViewById(R.id.everythingcheckbox);
        history = (CheckBox)findViewById(R.id.historycheckbox);
        culture = (CheckBox)findViewById(R.id.culturecheckbox);
        art = (CheckBox)findViewById(R.id.artcheckbox);
        science = (CheckBox)findViewById(R.id.sciencecheckbox);
        food = (CheckBox)findViewById(R.id.foodcheckbox);
        museum = (CheckBox)findViewById(R.id.museumcheckbox);
        shopping = (CheckBox)findViewById(R.id.shoppingcheckbox);
        park = (CheckBox)findViewById(R.id.parkcheckbox);
        landmark = (CheckBox)findViewById(R.id.landmarkcheckbox);
        spiritual = (CheckBox)findViewById(R.id.spiritualcheckbox);
        entertainment = (CheckBox)findViewById(R.id.entertainmentcheckbox);
        //hello this is srirams change
    }

    public void everythingClicked(View view){
        if(everything.isChecked()){
            Values.history = true;
            Values.culture = true;
            Values.art = true;
            Values.science = true;
            Values.food = true;
            Values.museum = true;
            Values.shopping = true;
            Values.park = true;
            Values.landmark = true;
            Values.spiritual = true;
            Values.entertainment  = true;
            history.setChecked(true);
            culture.setChecked(true);
            art.setChecked(true);
            science.setChecked(true);
            food.setChecked(true);
            museum.setChecked(true);
            shopping.setChecked(true);
            park.setChecked(true);
            landmark.setChecked(true);
            spiritual.setChecked(true);
            entertainment.setChecked(true);
        }
        else{
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
            history.setChecked(false);
            culture.setChecked(false);
            art.setChecked(false);
            science.setChecked(false);
            food.setChecked(false);
            museum.setChecked(false);
            shopping.setChecked(false);
            park.setChecked(false);
            landmark.setChecked(false);
            spiritual.setChecked(false);
            entertainment.setChecked(false);
        }


    }


    public void historyClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.history = true;
        }
        else{
            Values.history = false;
        }
    }

    public void cultureClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.culture = true;
        }
        else{
            Values.culture = false;
        }

    }

    public void artClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.art = true;
        }
        else{
            Values.art = false;
        }

    }

    public void scienceClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.science = true;
        }
        else{
            Values.science = false;
        }

    }

    public void foodClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.food= true;
        }
        else{
            Values.food = false;
        }

    }

    public void museumClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.museum = true;
        }
        else{
            Values.museum = false;
        }

    }
    public void shoppingClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.shopping= true;
        }
        else{
            Values.shopping = false;
        }

    }
    public void parkClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.park = true;
        }
        else{
            Values.park = false;
        }

    }
    public void landmarkClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.landmark = true;
        }
        else{
            Values.landmark = false;
        }

    }
    public void spiritualClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.spiritual = true;
        }
        else{
            Values.spiritual = false;
        }

    }
    public void entertainmentClicked(View view){
        if (((CheckBox) view).isChecked()) {
            Values.spiritual = true;
        }
        else{
            Values.spiritual = false;
        }

    }
    public void startTourClicked(View view){
        EditText editText = (EditText)findViewById(R.id.editext);
        String x = editText.getText().toString();
        if(!x.isEmpty() && !(x == null)){
            Values.range = Double.parseDouble(x);
            Intent myIntent = new Intent(ChooseTour.this, TourActivity.class);
            startActivity(myIntent);
        }
        else
            Toast.makeText(this, "Please enter a range for the tour",
                    Toast.LENGTH_SHORT).show();


    }


}

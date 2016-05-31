package com.example.sriramhariharan.houstonsights;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TourActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    String text;
    public TextView nametxt;
    public TextView desctxt;
    private static View rootview;
    private static ListView yourListView;
    TextToSpeech tts;
    public static ArrayList<Polyline> poly = new ArrayList<Polyline>();
    public static ArrayList<Marker> markers = new ArrayList<Marker>();
    public static ArrayList<Place> places = new ArrayList<Place>();
    Marker mMarker;
    GoogleMap map;
    private LocationManager locationManager;
    ArrayList<LatLng> markerPoints;
    GoogleApiClient mGoogleApiClient;
    Button button;
    LocationAdapter la;


    public void addItems(Place x) {
        places.add(x);
        la.notifyDataSetChanged();
    }
    public void addItems(Place x,int index) {
        places.add(index, x);
        la.notifyDataSetChanged();
    }
    public void removeItems(int index) {
        places.remove(index);
        la.notifyDataSetChanged();
    }
    Polyline currpath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        yourListView = (ListView)findViewById(R.id.list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        la = new LocationAdapter(getApplicationContext(),places);
        yourListView.setAdapter(la);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tts=new TextToSpeech(TourActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.UK);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        //ConvertTextToSpeech();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //nametxt = (TextView)findViewById(R.id.nametxt);
        //desctxt = (TextView)findViewById(R.id.desctxt);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                500,   // 3 sec
                0, this);

        /********* After registration onLocationChanged method  ********/
        /********* called periodically after each 3 sec ***********/
        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_maps
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();

        if (map != null) {

            // Enable MyLocation Button in the Map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);

            // Setting onclick event listener for the map
            /*map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already two locations
                    if (markerPoints.size() > 1) {
                        //markerPoints.clear();
                        //map.clear();
                    }

                    // Adding new item to the ArrayList
                    markerPoints.add(point);

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    /*if (markerPoints.size() == 1) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    } else {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    // Add new marker to the Google Map Android API V2
                    markers.add(map.addMarker(options));

                    createPath();
                }
            });*/
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        }
    }

    @Override
    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    public void putMarker(double lat, double lon, String col){

        LatLng point = new LatLng(lat,lon);

        markerPoints.add(point);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if (col.equals("blue")) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Add new marker to the Google Map Android API V2
        markers.add(map.addMarker(options));
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //s
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //putMarker(mLastLocation.getLatitude(),mLastLocation.getLongitude(),"blue");
            //LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            LatLng latLng = new LatLng(29.7522, -95.3756);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 19);
            map.animateCamera(cameraUpdate);  /*                    UNCOMMENT THIS WHEN DONE TESTING*/
            //places = Generator.getTour(Values.range,mLastLocation.getLatitude(),mLastLocation.getLongitude());
            places = Generator.getTour(Values.range,29.7522,-95.3756);
            // ErrorSpot
            la.notifyDataSetChanged();
            for(Place p : places){
                putMarker(p.getLatitude(),p.getLongitude(),"red");
            }
            if(places.size()>1)createPath2(places.get(places.size()-1),places.get(0),"norm");
            for(int i=places.size()-1;i>0;i--){
                if(i==1){
                    //createPath2(places.get(i-1),places.get(i),"hl");
                }
                else createPath2(places.get(i-1),places.get(i),"norm");
            }
            if(places.size()>1) {
                //nametxt.setText(places.get(1).getName());
                //desctxt.setText(places.get(1).getDescription());
            }
            /*for(int i=1;i<places.size()-1;i++){
                if(i==1)createPath2(places.get(i-1),places.get(i),"hl");
                else createPath2(places.get(i-1),places.get(i),"norm");
            }*/
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void replaceFL(ArrayList arr){
        if(arr.size()>1) {
            //arr.remove(0);
            arr.add(0, arr.get(arr.size() - 1));
            arr.remove(arr.size() - 1);
        }
    }

    private void createPath() {

        // Checks, whether start and end locations are captured
        LatLng origin;
        LatLng dest;
        String url;
        if (markerPoints.size() >= 2) {
            /*for(int i = 1;i<markerPoints.size();i++){
                origin = markerPoints.get(i-1);
                dest = markerPoints.get(i);
                // Getting URL to the Google Directions API
                url = getDirectionsUrl(origin, dest);
                // Start downloading json data from Google Directions API
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }*/
            origin = markerPoints.get(markerPoints.size() - 2);
            dest = markerPoints.get(markerPoints.size() - 1);
            // Getting URL to the Google Directions API
            url = getDirectionsUrl(origin, dest);
            // Start downloading json data from Google Directions API
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);


        }
    }

    private void createPath2(Place p1, Place p2,String col){

        LatLng origin = new LatLng(p1.getLatitude(),p1.getLongitude());
        LatLng dest = new LatLng(p2.getLatitude(),p2.getLongitude());
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        // Start downloading json data from Google Directions API
        if(col.equals("hl")) {
            DownloadTask2 downloadTask = new DownloadTask2();
            downloadTask.execute(url);
        } else {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }
    private void createPath3(LatLng l1, LatLng l2,String col){

        LatLng origin = l1;
        LatLng dest = l2;
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        // Start downloading json data from Google Directions API
        if(col.equals("hl")) {
            DownloadTask2 downloadTask = new DownloadTask2();
            downloadTask.execute(url);
        } else {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }
    private void createPat4(LatLng l1, Place p2){

        LatLng origin = l1;
        LatLng dest = new LatLng(p2.getLatitude(),p2.getLongitude());
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        // Start downloading json data from Google Directions API
        DownloadTask3 downloadTask = new DownloadTask3();
        downloadTask.execute(url);
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        //Mode of transportation
        String transportation = "mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + transportation;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    boolean tmp = true;
    @Override
    public void onLocationChanged(Location location) {
        LatLng point = new LatLng(location.getLatitude(),location.getLongitude());
        Log.e("onLocationChanged", "called");
        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 19);
        map.animateCamera(cameraUpdate); UNCOMMENT THIS WHEN DONE TESTING */
        if(places.size()>2 && Math.abs(location.getLatitude()-places.get(1).getLatitude())<=.0001 && Math.abs(location.getLongitude()-places.get(1).getLongitude())<=.0001){
            /*poly.get(0).remove();
            poly.remove(0);
            /*LatLng ll1 = new LatLng(places.get(1).getLatitude(),places.get(1).getLongitude());
            LatLng ll2 = new LatLng(places.get(2).getLatitude(),places.get(2).getLongitude());
            createPath3(ll1,ll2,"hl");*/
            /*nametxt.setText(places.get(1).getName());
            desctxt.setText(places.get(1).getDescription());
            places.remove(0);
            markers.get(0).remove();
            markers.remove(0);
            ConvertTextToSpeech(places.get(0).getDescription());
            replaceFL(poly);*/

        } else if(places.size()==2 && Math.abs(location.getLatitude()-places.get(1).getLatitude())<=.0001 && Math.abs(location.getLongitude()-places.get(1).getLongitude())<=.0001){
            /*poly.get(0).remove();
            poly.remove(0);
            /*LatLng ll1 = new LatLng(places.get(1).getLatitude(),places.get(1).getLongitude());
            LatLng ll2 = new LatLng(places.get(0).getLatitude(),places.get(0).getLongitude());
            createPath3(ll1,ll2,"hl");*/
            /*nametxt.setText(places.get(1).getName());
            desctxt.setText(places.get(1).getDescription());
            places.remove(0);
            ConvertTextToSpeech(places.get(0).getDescription());*/
        } else {
            /*poly.get(0).remove();
            poly.remove(0);
            LatLng ll1 = new LatLng(places.get(0).getLatitude(),places.get(0).getLongitude());
            createPath3(point,ll1,"hl");
            replaceFL(poly);*/
        }
        if(places.size()>=2 && Math.abs(location.getLatitude()-places.get(1).getLatitude())<=.0001 &&
                Math.abs(location.getLongitude()-places.get(1).getLongitude())<=.0001){
            poly.get(poly.size()-1).remove();
            poly.remove(poly.size()-1);
            markers.get(0).remove();
            markers.remove(0);
            ConvertTextToSpeech(places.get(0).getDescription());
            places.remove(0);
        }
        createPat4(point,places.get(0));
    }

    public void skip(LatLng latLng){
        if(places.size()>2){

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                //lineOptions.color(Color.parseColor("#FF9800"));
                lineOptions.color(Color.parseColor("#FF4081"));
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions!=null) {
                poly.add(map.addPolyline(lineOptions));
            }
            //map.addPolyline(lineOptions);
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        if(tts != null){

            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
    private void ConvertTextToSpeech(String speech) {
        // TODO Auto-generated method stub
        text = speech;
        if(text==null||"".equals(text))
        {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }else
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    public void onClick(View view){
        Intent myIntent = new Intent(TourActivity.this, MoreInfo.class);
        startActivity(myIntent);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*map = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
    private class DownloadTask2 extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask2 parserTask = new ParserTask2();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask2 extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                //lineOptions.color(Color.parseColor("#FF9800"));
                lineOptions.color(Color.parseColor("#c73366"));
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions!=null) {
                poly.add(map.addPolyline(lineOptions));
            }
        }
    }
    //test

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
    private class DownloadTask3 extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask3 parserTask = new ParserTask3();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask3 extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                //lineOptions.color(Color.parseColor("#FF9800"));
                lineOptions.color(Color.parseColor("#c73366"));
            }

            // Drawing polyline in the Google Map for the i-th route
            Polyline p=currpath;
            if(lineOptions!=null) {
                currpath=map.addPolyline(lineOptions);
            }
            if(p!=null)p.remove();
        }
    }
}

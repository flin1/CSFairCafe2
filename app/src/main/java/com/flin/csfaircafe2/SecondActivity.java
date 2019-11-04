package com.flin.csfaircafe2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;

import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity implements LocationListener {
    retrofit2.Call<SearchResponse> call;
    Response<SearchResponse> response;
    SearchResponse searchResponse;
    YelpFusionApi yelpFusionApi;
    Map<String, String> params;
    LocationManager lm;
    double lat, lng;
    String lati, lngi;
    Location location;
    TextView tv2, tv4, tv5;
    String businessName;
    String busiaddress;
    Double rating;
    Button btn;
    String cafe;
    ArrayList<Business> businesses;
    int numOfSearchResults = 20;
    int currentBusinessIndex = 0;
    ArrayList<String> businessNames;
    ArrayList<String> businessImages;
    ArrayList<String> businessUrls;
    String businessLocation;
    LinearLayout ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        btn = findViewById(R.id.button2);
        TextView tv = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);

        cafe = "cafe";
        thread.start();





//location stuff, don't touch
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
               == PackageManager.PERMISSION_GRANTED) {
            // if permission is granted...

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
           location = lm.getLastKnownLocation(lm.GPS_PROVIDER);
          lat = location.getLatitude();
          lng = location.getLongitude();
          lati = Double.toString(lat);
          lngi = Double.toString(lng);


          String text = "Current Location- Latitiude: "+ lati + ", Longitude: " + lngi;
          tv2.setText(text);
      }

//yelp stuff, in progress

        //test business search exception

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              params = new HashMap<>();
                    // general params
                    params.put("term", "cafe");
                    params.put("latitude", lati);
                    params.put("longitude", lngi);

                    call = yelpFusionApi.getBusinessSearch(params);

                    //crashes with this line
                    // try{
                    //   Response<SearchResponse> response = call.execute();
                    //}catch(Throwable e){

                    //}
                    call.enqueue(callback);



//extra business details, add later
      /*  int totalNumberOfResult = searchResponse.getTotal();  // 3

        ArrayList<Business> businesses = searchResponse.getBusinesses();
        businessName = businesses.get(0).getName();  // "JapaCurry Truck"
        rating = businesses.get(0).getRating();  // 4.0
       // tv2.setText( businessName + rating);*/
           }

        }); //end of on click listener
    } //end of oncreate


//api key run in background
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
                yelpFusionApi = apiFactory.createAPI("G0VaUTQIx9OeJcfzYQuJ9dCfP-7Wk-7ajyA9GMYGWBoPogd7CdqNAouOyZWdg77Yt4WrTE_oBQ4BnccewTgATTjkcHwoKze6S99mKsPegy3NCkaoHlm7IzRijLW_XHYx");

            } catch (Throwable e) {
                Log.e("TAG", "yelp key");
            }

        }
    });

    //on button click, perform method send message

    Callback<SearchResponse> callback = new Callback<SearchResponse>() {
        @Override
        public void onResponse(retrofit2.Call<SearchResponse> call, Response<SearchResponse> response) {

            searchResponse = response.body();
            businesses = searchResponse.getBusinesses();
            if (searchResponse.getTotal() < 20) {
                numOfSearchResults = searchResponse.getTotal();
            } else {
                numOfSearchResults = 20;
            }

            for (int i = 0; i < numOfSearchResults; i++){
                businessName = businesses.get(i).getName();  // "JapaCurry Truck"
                rating = businesses.get(i).getRating();
                busiaddress = businesses.get(i).getLocation().getAddress1();

                tv4.setText("Name: " + businessName + " Rating: " + rating + " Address: " + busiaddress);

            }

        }

        @Override
        public void onFailure(retrofit2.Call<SearchResponse> call, Throwable t) {
            // HTTP error happened, do something to handle it.
        }

    };




      @Override
  public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        lat = location.getLatitude();
        lng = location.getLongitude();
        lati = Double.toString(lat);
        lngi = Double.toString(lng);

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    }





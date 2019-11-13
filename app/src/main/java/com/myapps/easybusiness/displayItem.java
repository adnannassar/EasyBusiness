package com.myapps.easybusiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;

import me.relex.circleindicator.CircleIndicator;

public class displayItem extends AppCompatActivity implements OnMapReadyCallback {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ItemsPager myPager;
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyBd3YFdw4hdmZ2JuikJlJFphPSmDpbdT34";
    TextView txtTitle , txtPreis, txtDescreption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_display_item);

        myPager = new ItemsPager(this,main_menu_Activity.objectsArrayList.get(getIntent().getIntExtra("id",0)).photosList);
        viewPager = findViewById(R.id.view_pager_item);
        viewPager.setAdapter(myPager);
        circleIndicator = findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);
        txtTitle = findViewById(R.id.txtTitle);
        txtPreis = findViewById(R.id.txtPreisDisplayItme);
        txtDescreption = findViewById(R.id.txtDescreption);

        Intent intent = getIntent();

        txtTitle.setText(intent.getStringExtra("title"));
        txtDescreption.setText(intent.getStringExtra("descreption"));
        txtPreis.setText(intent.getIntExtra("price",0)+" "+intent.getStringExtra("currency"));


        // Map Settings
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.mapview);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void message(View view) {

    }
}

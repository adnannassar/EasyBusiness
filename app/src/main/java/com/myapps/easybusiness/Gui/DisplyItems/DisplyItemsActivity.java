package com.myapps.easybusiness.Gui.DisplyItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myapps.easybusiness.Gui.Chat.Chat_Activity;
import com.myapps.easybusiness.R;
import com.myapps.easybusiness.Gui.MainMenu.Main_menu_Activity;
import com.parse.ParseFile;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class DisplyItemsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ItemsPager myPager;
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyBd3YFdw4hdmZ2JuikJlJFphPSmDpbdT34";
    private TextView txtTitle, txtPreis, txtDescreption, textUsername;
    private CircleImageView imageViewUserPhotoInDisplayItem;
    public static ScrollView scrollView;
    public static Button btnPrivateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_display_item);
        Fresco.initialize(this);
        scrollView = findViewById(R.id.scrollViewDisplyItem);
        btnPrivateMessage = findViewById(R.id.btnPrivateMessage);
        myPager = new ItemsPager(this, Main_menu_Activity.objectsMap.get(getIntent().getStringExtra("objectId")));
        viewPager = findViewById(R.id.view_pager_item);
        viewPager.setAdapter(myPager);
        viewPager.setPageTransformer(true, new ZoomOutTransformation());

        circleIndicator = findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);
        txtTitle = findViewById(R.id.txtTitle);
        txtPreis = findViewById(R.id.txtPreisDisplayItme);
        txtDescreption = findViewById(R.id.txtDescreption);
        textUsername = findViewById(R.id.textUserName);
        imageViewUserPhotoInDisplayItem = findViewById(R.id.imageViewUserPhotoInDisplayItem);


        txtTitle.setText(getIntent().getStringExtra("title"));
        txtDescreption.setText(getIntent().getStringExtra("descreption"));
        txtPreis.setText(getIntent().getStringExtra("price"));
        textUsername.setText(ParseUser.getCurrentUser().getUsername());
        ParseFile userPhoto = ParseUser.getCurrentUser().getParseFile("profilePhoto");
        if (userPhoto != null) {
            Glide.with(this).load(userPhoto.getUrl()).into(imageViewUserPhotoInDisplayItem);
        }


        // Map Settings
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.mapview);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }


    public static void makeViewPagerBig() {

    }

    public static void makeViewPagerSmall() {
    }


    class ZoomOutTransformation implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.65f;
        private static final float MIN_ALPHA = 0.3f;

        @Override
        public void transformPage(View page, float position) {

            if (position < -1) {  // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            } else if (position <= 1) { // [-1,1]

                page.setScaleX(Math.max(MIN_SCALE, 1 - Math.abs(position)));
                page.setScaleY(Math.max(MIN_SCALE, 1 - Math.abs(position)));
                page.setAlpha(Math.max(MIN_ALPHA, 1 - Math.abs(position)));

            } else {  // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


        }
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
        LatLng itemLocation = new LatLng(getIntent().getDoubleExtra("latitude", 0), getIntent().getDoubleExtra("longitude", 0));
        map.addMarker(new MarkerOptions().position(itemLocation).title("Item Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        map.animateCamera((CameraUpdateFactory.newLatLngZoom(itemLocation, 10)));

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
        goToChatActivity();
    }

    public void goToChatActivity() {
        Intent intent = new Intent(this, Chat_Activity.class);
        startActivity(intent);
    }
}

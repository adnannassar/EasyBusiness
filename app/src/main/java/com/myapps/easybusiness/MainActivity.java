package com.myapps.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseAnalytics;

public class MainActivity extends AppCompatActivity {
    StarterApplication parseStarter;
    Animation zoomin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseStarter = new StarterApplication();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        final ImageView myImageView = (ImageView) findViewById(R.id.imageView2);
        //  Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        //  myImageView.startAnimation(myFadeInAnimation); //Set animation to your ImageView

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        zoomin.setStartOffset(3000);
        myImageView.setAnimation(zoomin);

        zoomin.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);

            }
        });




        /*
        Animation a = new AlphaAnimation(1.00f, 0.00f);

        a.setDuration(3000);
        a.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            public void onAnimationEnd(Animation animation) {
                myImageView.setVisibility(View.GONE);

            }
        });

        myImageView.startAnimation(a);


         */

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void signUp(View view) {

    }
}

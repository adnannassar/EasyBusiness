package com.myapps.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseUser;

public class user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("User Overview");
        getSupportActionBar().hide();
    }

    public void logout(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
    }

    public void go_back_to_mainMenu(View view) {
        Intent intent = new Intent(this,main_menu_Activity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, main_menu_Activity.class);
        startActivity(intent);
    }
}

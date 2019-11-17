package com.myapps.easybusiness.Gui.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.myapps.easybusiness.R;
import com.myapps.easybusiness.Gui.Login_SignUp.login;
import com.myapps.easybusiness.Gui.MainMenu.Main_menu_Activity;
import com.parse.ParseUser;

public class User_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
        setTitle("User Overview");
        getSupportActionBar().hide();
    }

    public void logout(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void go_back_to_mainMenu(View view) {
        Intent intent = new Intent(this, Main_menu_Activity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main_menu_Activity.class);
        startActivity(intent);
    }


}

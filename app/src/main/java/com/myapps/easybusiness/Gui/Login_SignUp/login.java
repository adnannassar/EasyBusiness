package com.myapps.easybusiness.Gui.Login_SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myapps.easybusiness.Datenhaltung.StarterApplication;
import com.myapps.easybusiness.R;
import com.myapps.easybusiness.Gui.MainMenu.Main_menu_Activity;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    EditText userName, password;
    TextView ChangeSignUpMadeTextView;
    StarterApplication parseStarter;
    Boolean signUpModeActive = true;
    Button btnSignUp;
    LinearLayout mainLoginSignUpLayout;
    ImageView logo;
    Intent users_list_intent ;
    ArrayList<String> usersArrayList;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        parseStarter = new StarterApplication();

        btnSignUp = findViewById(R.id.btnSignUp);
        userName = findViewById(R.id.editTextUserName);
        password = findViewById(R.id.editTextPassword);
        password.setOnKeyListener(this);

        mainLoginSignUpLayout = findViewById(R.id.mainLoginSignUpLayout);
        mainLoginSignUpLayout.setOnClickListener(this);

        logo = findViewById(R.id.imageViewLogo);
        logo.setOnClickListener(this);

        ChangeSignUpMadeTextView = findViewById(R.id.ChangeSignupMedeTextView);
        ChangeSignUpMadeTextView.setOnClickListener(this);


        usersArrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersArrayList);


        // if User_Activity logged in
        if(ParseUser.getCurrentUser() != null){
            showMainmenu();
        }

    }

    public void signUp(View view) {

        if (userName.getText().toString().matches("") || password.getText().toString().matches("")) {
            Toast.makeText(this, "Username and Password are required", Toast.LENGTH_SHORT).show();
        } else {
            if (signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(userName.getText().toString());
                user.setPassword(password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            showMainmenu();
                            Toast.makeText(getApplicationContext(), "Sign up Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign up  Failed\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                            //query.whereEqualTo("myString", "yasin");
                            //query.setLimit(2);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            for (ParseObject object : objects) {
                                                usersArrayList.add(object.getString("username") + " , " + object.getString("password") + "\n");
                                            }

                                        }
                                    }
                                }
                            });
                            adapter.notifyDataSetChanged();
                            showMainmenu();
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ChangeSignupMedeTextView) {
            if (signUpModeActive) {
                btnSignUp.setText("Login");
                signUpModeActive = false;
                ChangeSignUpMadeTextView.setText("Or, Sign up");
            } else {
                btnSignUp.setText("Sign UP");
                signUpModeActive = true;
                ChangeSignUpMadeTextView.setText("Or, Login");
            }

        } else if (v.getId() == R.id.mainLoginSignUpLayout || v.getId() == R.id.imageViewLogo) {
            // hide the keyboard if we pressed on the main layout or the LOGO
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(v);
        }
        return false;
    }

    public void showMainmenu(){
        users_list_intent = new Intent(getApplicationContext(), Main_menu_Activity.class);
        startActivity(users_list_intent);
    }

}
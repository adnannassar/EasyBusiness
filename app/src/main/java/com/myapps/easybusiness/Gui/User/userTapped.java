package com.myapps.easybusiness.Gui.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapps.easybusiness.R;
import com.myapps.easybusiness.Gui.Login_SignUp.login;
import com.myapps.easybusiness.Gui.MainMenu.Main_menu_Activity;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class userTapped extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    CircleImageView profilePhoto;
    TextView txtUserName;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tapped);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //  FloatingActionButton fab = findViewById(R.id.fab);
        profilePhoto = findViewById(R.id.imageViewUserPhoto);
        txtUserName = findViewById(R.id.txtuserNameInUser);
        txtUserName.setText(ParseUser.getCurrentUser().getUsername());
        refreshPageAfterUpdateProfilePhoto();
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         */

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

    // Heir are the methods that we need to get Photos from the phone storage
    //
    public void changeUserProfilePhoto(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                getPhoto();

            }
        } else {
            getPhoto();

        }
    }

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImage);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                //Bitmap resizedBitMap = Bitmap.createScaledBitmap(bitmap, 143, 143, true);
                profilePhoto.setImageBitmap(bitmap);
                profilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

                //put the profile photo in server
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream); // 100 ist the quality
                byte[] bytesArray = stream.toByteArray();
                ParseFile file = new ParseFile("profilePhoto.WEBP", bytesArray);
                ParseUser.getCurrentUser().put("profilePhoto", file);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Profile photo updated !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error changing the Profile photo", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //
    public void refreshPageAfterUpdateProfilePhoto() {

        final ParseFile profilePhotoFile = (ParseFile) ParseUser.getCurrentUser().get("profilePhoto");
        if (profilePhotoFile != null) {
            profilePhotoFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null && data != null && data.length > 0) {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        profilePhoto.setImageBitmap(bitmap);
                    }
                }
            });
        }

    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                refreshPage();
            }
        }, 1000);
    }

    public void refreshPage() {
        finish();
        startActivity(this.getIntent());
    }
}



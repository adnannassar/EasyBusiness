package com.myapps.easybusiness.FachLogic;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class ItemForRecyclerView {
    String objectId, descreption , imageView , title;
    int preis;
    double latitude, longitude;

    public ItemForRecyclerView(String objectId, String descreption, String imageView, String title, int preis, double latitude, double longitude) {
        this.objectId = objectId;
        this.descreption = descreption;
        this.imageView = imageView;
        this.title = title;
        this.preis = preis;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getDescreption() {
        return descreption;
    }

    public String getImageView() {
        return imageView;
    }

    public String getTitle() {
        return title;
    }

    public int getPreis() {
        return preis;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

package com.example.d_kit.disasterkit;

import android.app.Application;
import android.location.Location;


public class Myapp extends Application{

    private double location;

    @Override
    public void onCreate() {

    }

    public Double getLongitude() {
        return location;
    }

    public void setLongitude(double str) {
        location = str;
    }

}

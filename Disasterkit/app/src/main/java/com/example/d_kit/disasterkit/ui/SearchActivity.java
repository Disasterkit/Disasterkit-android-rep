package com.example.d_kit.disasterkit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.rivchat.R;
import com.android.rivchat.model.Locations;
import com.android.rivchat.ui.DisplayMap.MapActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private double[] lat;
    private double[] lng;
    private int[] sit;
    private  int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        lat=new double[2];
        lng=new double[2];
        sit=new int[2];
        for (int j=0;j<2;j++){
            lat[j]=0.0;
            lng[j]=0.0;
            sit[j]=0;
        }




                for(int i=0;i<2;i++) {
                    mDatabase.child("location/"+i).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Locations location = snapshot.getValue(Locations.class);
                            Double lati = location.getLat();
                            Double lngn = location.getLng();
                            int situ = location.getSit();
                            lat[cnt]=lati;
                            lng[cnt]=lngn;
                            sit[cnt]=situ;
                            cnt++;


                            if(cnt==2) {
                                Intent intent = new Intent(SearchActivity.this, MapActivity.class);
                                intent.putExtra("lat", lat);
                                intent.putExtra("lng", lng);
                                intent.putExtra("sit", sit);
                                startActivity(intent);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }





        }


    }

    /*
    public static class Location {

        private Double lat;
        private Double lng;

        public Location() {}

        public Location(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setCode(Double lat) {
            this.lat = lat;
        }
        public void setName(Double lng) {
            this.lng = lng;
        }

    }
    */


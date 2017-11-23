package com.example.d_kit.disasterkit.model;


public class Locations {

    /*
    public User name;
    public Double lat;
    public Double lng;
    public int situation;
    */
    //public long timestamp;

    private Double lat;
    private Double lng;
    private int situation;

    public Locations() {}

    public Locations(Double lat, Double lng, int situation) {
        this.lat = lat;
        this.lng = lng;
        this.situation=situation;
    }

    public Double getLat() {
        return lat;
    }
    public Double getLng() {
        return lng;
    }
    public int getSit(){
        return situation;
    }

    public void setCode(Double lat) {
        this.lat = lat;
    }
    public void setName(Double lng) {
        this.lng = lng;
    }
    public void setSituation(int situation){
        this.situation=situation;
    }

}



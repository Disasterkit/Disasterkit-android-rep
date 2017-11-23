package com.example.d_kit.disasterkit.model;

import java.util.ArrayList;


public class Consersation {
    private ArrayList<Message> listMessageData;
    public Consersation(){

        listMessageData = new ArrayList<>();
    }

    public ArrayList<Message> getListMessageData() {

        return listMessageData;
    }
}

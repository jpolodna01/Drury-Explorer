package edu.drury.mcs.icarus.druryexplorer;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Tanya on 2/22/2015.
 */
public class Building {

    public int buildingNumber;
    public double bLongatude;
    public double bLatatude;
    private String buildingName;
    private String buildingFacts;
    private BitmapDrawable picture;

    public Building(int bnum, double blong, double bLat, String bName, String bFacts){
        this.buildingNumber=bnum;
        this.bLongatude = blong;
        this.bLatatude=bLat;
        this.buildingName=bName;
        this.buildingFacts=bFacts;
        this.picture=null;

    }

    public Building(){
        this.buildingNumber=0;
        this.bLongatude=0.0;
        this.bLatatude=0.0;
        this.buildingName="";
        this.buildingFacts="";
        this.picture=null;
    }

//object setters

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public void setbLongatude(double bLongatude) {
        this.bLongatude = bLongatude;
    }

    public void setbLatatude(double bLatatude) {
        this.bLatatude = bLatatude;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public void setBuildingFacts(String buildingFacts) {
        this.buildingFacts = buildingFacts;
    }

    public void setPicture(BitmapDrawable picture) {
        this.picture = picture;
    }

    //object getters


    public int getBuildingNumber() {
        return buildingNumber;
    }

    public double getbLongatude() {
        return bLongatude;
    }

    public double getbLatatude() {
        return bLatatude;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getBuildingFacts() {
        return buildingFacts;
    }

    public BitmapDrawable getPicture() {return picture;}
}

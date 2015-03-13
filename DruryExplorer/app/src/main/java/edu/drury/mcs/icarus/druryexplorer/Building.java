package edu.drury.mcs.icarus.druryexplorer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Daiv McBride on 2/22/2015.
 */
public class Building implements Parcelable {

    public int buildingNumber;
    public double bLongatude;
    public double bLatatude;
    private String buildingName;
    private String buildingFacts;
    private String picture;
    private String id;

    public Building(int bnum, double blong, double bLat, String bName, String bFacts, String img){
        this.buildingNumber=bnum;
        this.bLongatude = blong;
        this.bLatatude=bLat;
        this.buildingName=bName;
        this.buildingFacts=bFacts;
        this.picture=img;
    }


    public Building(){
        this.buildingNumber=0;
        this.bLongatude=0.0;
        this.bLatatude=0.0;
        this.buildingName="";
        this.buildingFacts="";
        this.picture="";

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

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPicture() {return picture;}

    public String getId(){return id;}



    @Override
    public String toString() {
        return buildingName;
    }

    //methods inorder to make building class Parcelable
    //obtained from example at stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-be-parcelable

    public Building(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.id=data[0];
        this.buildingName=data[1];
        this.buildingFacts=data[2];
        this.picture=data[3];
    }


    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeStringArray(new String[]{this.id,
                                            this.buildingName,
                                            this.buildingFacts,
                                            this.picture});
    }


    public static final Parcelable.Creator<Building> CREATOR = new Parcelable.Creator<Building>(){
        public Building createFromParcel(Parcel in){
            return new Building(in);
        }
        public Building[] newArray(int size){
            return new Building[size];
        }
    };

}

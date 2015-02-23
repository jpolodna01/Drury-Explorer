package edu.drury.mcs.icarus.druryexplorer;

/**
 * Created by Tanya on 2/22/2015.
 */
public class Building {

    private int buildingNumber;
    private double bLongatude;
    private double bLatatude;
    private String buildingName;
    private String buildingFacts;

    public Building(int bnum, double blong, double bLat, String bName, String bFacts){
        this.buildingNumber=bnum;
        this.bLongatude = blong;
        this.bLatatude=bLat;
        this.buildingName=bName;
        this.buildingFacts=bFacts;
    }

    public Building(){
        this.buildingNumber=0;
        this.bLongatude=0.0;
        this.bLatatude=0.0;
        this.buildingName="";
        this.buildingFacts="";
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
}

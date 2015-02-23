package edu.drury.mcs.icarus.druryexplorer;

/**
 * Created by Daiv McBride on 2/22/2015.
 * This is this the TourPoint class. This class represents a spot on the tour around Drury
 * allowing for a smooth tour
 * tnum-is the number representing the spot on the tour allowing for a smooth and natural tour
 * tlong - is the longatude of the tour point
 * tlat - is the laguitude of the tour point
 * build - is a boolean
 */
public class TourPoint {
    private int tourNumber;
    private double longatude;
    private double latatude;
    private Boolean stoppingPoint;
    private int buildingNumber;

    public TourPoint(int num, double lon, double lat, Boolean bui, int bld){
        tourNumber=num;
        longatude= lon;
        latatude= lat;
        stoppingPoint = bui;
        buildingNumber = bld;
    }

    public TourPoint(){
        tourNumber=0;
        longatude=0.0;
        latatude=0.0;
        stoppingPoint=false;
        buildingNumber = 0;
    }
//Object setters


    public void setTourNumber(int tourNumber) {
        this.tourNumber = tourNumber;
    }

    public void setLongatude(double longatude) {
        this.longatude = longatude;
    }

    public void setLatatude(double latatude) {
        this.latatude = latatude;
    }

    public void setStoppingPoint(Boolean stoppingPoint) {
        this.stoppingPoint = stoppingPoint;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

// object getters


    public int getTourNumber() {
        return tourNumber;
    }

    public double getLongatude() {
        return longatude;
    }

    public double getLatatude() {
        return latatude;
    }

    public Boolean getStoppingPoint() {
        return stoppingPoint;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }


}

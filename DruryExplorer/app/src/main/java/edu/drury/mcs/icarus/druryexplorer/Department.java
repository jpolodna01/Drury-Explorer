package edu.drury.mcs.icarus.druryexplorer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanya on 3/12/2015.
 */
public class Department implements Parcelable {

    private int departmentID, hallID;
    private String name, description, location;

    public Department(){}

    public int getHallID(){return this.hallID;}
    public int getDepartmentID(){return this.departmentID;}
    public String getName(){return this.name;}
    public String getLocation(){return this.location;}
    public String getDescription(){return this.description;}

    public void setHallID(int id){this.hallID = id;}
    public void setDepartmentID(int id){this.departmentID = id;}
    public void setName(String name){this.name = name;}
    public void setDescription(String description){this.description = description;}
    public void setLocation(String location){this.location = location;}


    public String toString(){
        return name;
    }

    public Department(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.name=data[0];
        this.description=data[1];
        this.location=data[2];
    }


    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeStringArray(new String[]{this.name,
                this.description,
                this.location});
    }

    public static final Parcelable.Creator<Department> CREATOR = new Parcelable.Creator<Department>(){
        public Department createFromParcel(Parcel in){
            return new Department(in);
        }
        public Department[] newArray(int size){
            return new Department[size];
        }
    };
}

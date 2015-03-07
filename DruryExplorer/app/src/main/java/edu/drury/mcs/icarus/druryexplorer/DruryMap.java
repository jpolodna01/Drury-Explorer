package edu.drury.mcs.icarus.druryexplorer;

import edu.drury.mcs.icarus.druryexplorer.Building;
import edu.drury.mcs.icarus.druryexplorer.TourPoint;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class DruryMap extends FragmentActivity  {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Building[] buildingArray;
    private TourPoint[] tours={};
    private Boolean firstTime=true;
    private Boolean closeToNext=false;
    private PolylineOptions tourRoute = new PolylineOptions();
    private PolylineOptions touring = new PolylineOptions();
    private int next=0;
    private int times=0;
    private Boolean  tour= false;
    private String jsonResult;
    private String Burl ="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Hall_Object.php";
    private String Turl="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Tour_Object.php";
    private String Tpurl="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Tour_info.php";
    private String Tnum="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Number_Tour.php";
    private int[] test= new int[10];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drury_map);
        firstTime=true;
        for(int i =0;i<10;i++){
            test[i]=i;
        }
        hallArray();


        setUpMapIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.219736, -93.285769), 18));
        //(example of interior map)mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.86997, 151.2089), 18));

        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();
        LocationManager manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                //mMap.addMarker(new MarkerOptions().position(bay).title("bay").snippet("Bay"));
                /*if(tour) {
                    if (firstTime) {
                        int i = closestBuilding(location, tour1);
                        toStart(location, tour1[i]);
                        mMap.addMarker(new MarkerOptions().position(tour1[i]).title(buildings[i]));
                        firstTime = false;
                        next = i;
                    }
                    if (close(location, tour1[next]) && location.hasAccuracy()) {
                        if (next == tour1.length - 1) {
                            next = 0;
                        } else {
                            next++;
                        }
                        mMap.addMarker(new MarkerOptions().position(tour1[next]).title(buildings[next]));
                        toStart(location, tour1[next]);
                    }
                }*/




            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,0,listener);
        //manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,listener);
        //manager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0,listener);
        boolean on =manager.isProviderEnabled(LocationManager.GPS_PROVIDER);



    }



    public void startTour(View view){
        tour = true;

    }


    private double trig(Location loc1, LatLng loc2){
        double lat1 = loc1.getLatitude();
        double lat2 = loc2.latitude;
        double long1 = loc1.getLongitude();
        double long2 = loc2.longitude;

        double dif =(((lat1-lat2)*(lat1-lat2))+((long1-long2)*(long1-long2)));

        return dif;
    }

    private int closestBuilding(Location loc, LatLng[] comp){
        int closest=0;
        double cDist= 100000;
        double dist;
        for(int i=0; i<comp.length; i++){
            dist=trig(loc,comp[i]);
            if(dist<cDist){
                cDist=dist;
                closest=i;
            }
        }
        return closest;
    }

    private void toStart(Location now, LatLng there){
       LatLng here = new LatLng(now.getLatitude(),now.getLongitude());
        touring.geodesic(true)
                    .add(here)
                    .add(there)
                    .width(15)
                    .color(Color.rgb(204,21,21)  );


        mMap.addPolyline(touring);

    }
    /*public void fullTour(View view){
        for(int i=0;i<tour1.length-1;i++){

            tourRoute.geodesic(true)
                    .add(tour1[i])
                    .add(tour1[i+1])
                    .width(15);
            mMap.addPolyline(tourRoute);

        }
    }*/

    public void clearMap(View view){
        mMap.clear();
        tour=false;
        firstTime=true;
    }



    public void buildingMarkers(View view){


        for(int i=0;i<buildingArray.length;i++){

            mMap.addMarker(new MarkerOptions().position(new LatLng(buildingArray[i].getbLatatude(),buildingArray[i].getbLongatude())).title(buildingArray[i].getBuildingName()).snippet(buildingArray[i].getBuildingFacts()));

        }
    }
    private boolean close(Location location, LatLng next){
        double dist = trig(location, next);
        if(dist*10000000<.5){
            return true;
        }
        return false;
    }
    private void selfTour(Location location,int next, LatLng[] tour){

        int i=0;
        while(i<tour.length){

            if(close(location,tour[next])){
                toStart(location,tour[next]);
                if(next ==tour.length-1){
                    next=0;
                }
                else{
                    next++;
                }
                i++;
            }
        }

    }
    /**
     * Sub-class of of the Department class, which is used for handling the asynchronous functionality.
     * Its nature forces sub-classing for implementation; several methods such as doInBackground are overwritten
     * to handle the asynchronous threads as they are appropriate for this app.
     * <p/>
     * Author: Josef Polodna
     */
    private class JsonReadHalls extends AsyncTask<String, Void, String> {

        /* Override the doInBackground method, which handles most of the background processing, to
            collect the data from the database.

            @param params - vararg, which allows for theoretically unlimited amount of string params.
                            params: the read-in json data, of undetermined length.
        */
        @Override
        protected String doInBackground(String... params) {
            //Create the http client and use the post to request the origin server accept the enclosed entity
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                // set response variable to the execution of the httppost
                HttpResponse response = httpclient.execute(httppost);
                // jsonResult (field) is equal to the content of the response converted to a string
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            }
            // catch various errors and print stack trace them
            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // this method is to return its computation for purposes of threading
            return null;
        }



        /* This method builds a string from the input stream

            @param is - input stream
        */
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = ""; //String for read-line
            StringBuilder answer = new StringBuilder(); //string builder, for using modifiable sequence of characters
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));// buffered reader to read from the input stream

            try {
                //while another line exists in the input stream, read it in and append it to the stringbuilder answer
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // presents the error to the user in an unobtrusive message utilizing toast
                Toast.makeText(getApplicationContext(), "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer; //return the built answer
        }


        /* The final method of the asynchronous sub-class, which posts the data to the ui thread. Overrode
			to utilize the ListDrwaer method defined in the outer-class

			@param result - String built in the stringbuilder method from the json data
		*/
        @Override
        protected void onPostExecute(String result) {
            hallDrawer();
        }
    }// End Async task

    // access the web service
    public void hallArray() {
        JsonReadHalls task = new JsonReadHalls();
        // passes values for the urls string array
        task.execute(new String[] { Burl });
    }
    public void hallDrawer() {
        //


        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("hall");
            buildingArray=new Building[jsonMainNode.length()];

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                int id = jsonChildNode.optInt("Hall_ID");
                String name = jsonChildNode.optString("name");
                String history = jsonChildNode.optString("history");
                double lat = jsonChildNode.optDouble("latitude");
                double lon = jsonChildNode.optDouble("longitude");
                buildingArray[i] = new Building();
                buildingArray[i].setbLongatude(lon);
                buildingArray[i].setbLatatude(lat);
                buildingArray[i].setBuildingName(name);
                buildingArray[i].setBuildingNumber(id);
                buildingArray[i].setBuildingFacts(history);
                Building temp = buildingArray[i];



            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        // android.R.layout.simple_list_item_1 is predefined in android libraries and not in local xml, and it specifies
        // to the listview how to display the data

    }

}


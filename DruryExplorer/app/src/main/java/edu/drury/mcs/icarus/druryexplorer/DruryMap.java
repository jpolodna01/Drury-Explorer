package edu.drury.mcs.icarus.druryexplorer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DruryMap extends FragmentActivity  {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Building[] buildingArray;
    private TourPoint[] tours;
    private TourPoint[] tourOne;
    private TourPoint[] tourTwo;
    private PolylineOptions tourRoute1 = new PolylineOptions();
    private PolylineOptions tourRoute2 = new PolylineOptions();
    private PolylineOptions selfTour1 = new PolylineOptions();
    private PolylineOptions selfTour2 = new PolylineOptions();
    private Polyline toured;
    private Polyline tourMarkers1;
    private Polyline tourMarkers2;
    private int next=0;
    private int level=0;
    private int tournum;
    private Boolean  tour1= false;
    private Boolean tour2 = false;
    private Boolean firstTime1=true;
    private Boolean firstTime2=true;
    private Boolean startOne=false;
    private Boolean startTwo = false;
    Boolean network;
    private String jsonResult;
    private String jsonResult2;
    private String Burl ="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Hall_Object.php";
    private String Turl="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Tour_Objects.php";
    private String Tpurl="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Tour_info.php";
    private String Tnum="http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Number_Tour.php";
    JSONObject jsonResponse;
    private ImageView dImage;
    private int[] pic={R.drawable.pearsons,R.drawable.shewmaker, R.drawable.springfield,R.drawable.tsc,R.drawable.hammons,
            R.drawable.breech,R.drawable.weiser,R.drawable.burnham,
            R.drawable.bay,R.drawable.oreilly,R.drawable.pac, R.drawable.stonechapel,R.drawable.olin,
            R.drawable.lay,R.drawable.mabee,R.drawable.fsc,R.drawable.freeman,
            R.drawable.rose,R.drawable.smith,R.drawable.wallace, R.drawable.sunderland,R.drawable.president,
            R.drawable.parsonage,R.drawable.congregational,R.drawable.summit,R.drawable.suites,
            R.drawable.collegepark,R.drawable.mac,R.drawable.jeff,R.drawable.quad,R.drawable.manley,
            R.drawable.harrison,R.drawable.drury,R.drawable.diversity};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drury_map);
        dImage=(ImageView)findViewById(R.id.imageView2);
        firstTime1=true;
        firstTime2=true;


        network = checkNetwork();
        if(network) {
            hallArray();
        }
        else{
            hallDrawer();
            tourDrawer();
        }



        setUpMapIfNeeded();


    }


    public Boolean checkNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if(tour1){
            tourMarkers1.isVisible();
        }

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



    }

    private void splitTourArray(){
        int place = 0;
        int count = 0;
        while(place<tours.length){
            if(tours[place].getTourNumber()==1){
                count++;
            }
            place++;
        }
        tourOne=new TourPoint[count];
        tourTwo=new TourPoint[tours.length-count];
        place = 0;
        int placeOne=0;
        int placeTwo = 0;
        while(place<tours.length){
            if(tours[place].getTourNumber()==1){
                tourOne[placeOne]= tours[place];
                placeOne++;
                place++;
            }
            else{
                tourTwo[placeTwo]=tours[place];
                place++;
                placeTwo++;
            }
        }


    }

    public void GPSAlert(){
        AlertDialog.Builder gps = new AlertDialog.Builder(this);
        gps.setTitle("GPS");
        gps.setMessage("Your GPS needs to be turned on for this function to work");
        gps.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int w){
                //do nothing but close the dialog
            }
        });
        AlertDialog gpsDialog = gps.create();
        gpsDialog.show();
    }

    public void stopTours(View view){
        if(startOne || startTwo){
            toured.remove();
        }
        startOne=false;
        startTwo=false;
        mMap.clear();
    }

    //allows the user to change between three different views of the map
    public void cMap(View view){
        if(level==2) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            level = 0;
        }
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            level = 2;
        }


    }

    private void selfTour(){

        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                    //mMap.addMarker(new MarkerOptions().position(bay).title("bay").snippet("Bay"));
                    if(startOne && tournum==1) {
                        if (mMap.getMyLocation() != null && mMap.getMyLocation().hasAccuracy()) {
                            if (firstTime1) {
                                int x = closestBuilding(mMap.getMyLocation(), tourOne);
                                toStart(mMap.getMyLocation(), new LatLng(tourOne[x].getLatatude(), tourOne[x].getLongatude()), selfTour1);
                                if (tourOne[x].getBuildingNumber() > 0) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(tourOne[x].getLatatude(), tourOne[x].getLongatude())).title(buildingArray[tourOne[x].getBuildingNumber() - 1].getBuildingName()));
                                    dImage.setImageResource(pic[tourOne[x].getBuildingNumber() - 1]);
                                }
                                firstTime1 = false;
                                next = x;

                            }
                            if (close(mMap.getMyLocation(), new LatLng(tourOne[next].getLatatude(), tourOne[next].getLongatude())) && mMap.getMyLocation().hasAccuracy()) {
                                if (next == tourOne.length - 1) {
                                    next = 0;
                                } else {
                                    next++;
                                }
                                if (tourOne[next].getBuildingNumber() > 0) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(tourOne[next].getLatatude(), tourOne[next].getLongatude())).title(buildingArray[tourOne[next].getBuildingNumber() - 1].getBuildingName()));
                                    dImage.setImageResource(pic[tourOne[next].getBuildingNumber() - 1]);
                                }
                                toContinue(new LatLng(tourOne[next].getLatatude(), tourOne[next].getLongatude()), selfTour1);
                            }
                        } else {
                            if (firstTime1) {
                                startOne = false;
                            }
                        }
                    }

                    if(startTwo && tournum==2) {
                        if (mMap.getMyLocation() != null && mMap.getMyLocation().hasAccuracy()) {
                            if (firstTime2) {
                                int x = closestBuilding(mMap.getMyLocation(), tourTwo);
                                toStart(mMap.getMyLocation(), new LatLng(tourTwo[x].getLatatude(), tourTwo[x].getLongatude()), selfTour2);
                                if (tourTwo[x].getBuildingNumber() > 0) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(tourTwo[x].getLatatude(), tourTwo[x].getLongatude())).title(buildingArray[tourTwo[x].getBuildingNumber() - 1].getBuildingName()));
                                    dImage.setImageResource(pic[tourTwo[x].getBuildingNumber() - 1]);
                                }
                                firstTime2 = false;
                                next = x;

                            }
                            if (close(mMap.getMyLocation(), new LatLng(tourTwo[next].getLatatude(), tourTwo[next].getLongatude())) && mMap.getMyLocation().hasAccuracy()) {
                                if (next == tourOne.length - 1) {
                                    next = 0;
                                } else {
                                    next++;
                                }
                                if (tourTwo[next].getBuildingNumber() > 0) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(tourOne[next].getLatatude(), tourTwo[next].getLongatude())).title(buildingArray[tourTwo[next].getBuildingNumber() - 1].getBuildingName()));
                                    dImage.setImageResource(pic[tourTwo[next].getBuildingNumber() - 1]);
                                }
                                toContinue(new LatLng(tourOne[next].getLatatude(), tourTwo[next].getLongatude()), selfTour2);
                            }
                        } else {
                            if (firstTime2) {
                                startOne = false;
                            }
                        }
                    }
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
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, listener);

        }
        else{
            startOne=false;
            GPSAlert();

        }
    }



    public void startTourOne(View view){
        if(!startOne) {
            startOne = true;
            firstTime1=true;
            tournum=1;
        }
        if(startTwo){
            startTwo=false;
        }

        selfTour();
    }

    public void startTourTwo(View view){
        if(!startTwo) {
            startTwo = true;
            firstTime2=true;
            tournum=2;
        }
        if(startOne){
            startOne=false;
        }
        selfTour();
    }

    private double trig(Location loc1, LatLng loc2){
        double lat1 = loc1.getLatitude();
        double lat2 = loc2.latitude;
        double long1 = loc1.getLongitude();
        double long2 = loc2.longitude;

        double dif =(((lat1-lat2)*(lat1-lat2))+((long1-long2)*(long1-long2)));

        return dif;
    }

    private int closestBuilding(Location loc, TourPoint[] comp){
        int closest=0;
        double cDist= 100000;
        double dist;
        for(int i=0; i<comp.length; i++){
            dist=trig(loc,new LatLng(comp[i].getLatatude(),comp[i].getLongatude()));
            if(dist<cDist){
                cDist=dist;
                closest=i;
            }
        }
        return closest;
    }



    private void toStart(Location now, LatLng there,PolylineOptions touring){
       LatLng here = new LatLng(now.getLatitude(),now.getLongitude());
        touring.geodesic(true)
                    .add(here)
                    .add(there)
                    .width(15)
                    .color(Color.rgb(204,21,21)  );


        toured = mMap.addPolyline(touring);

    }

    private void toContinue(LatLng there,PolylineOptions touring){
        touring.geodesic(true)
                .add(there)
                .width(15)
                .color(Color.rgb(204,21,21)  );


        toured = mMap.addPolyline(touring);

    }
    public void viewTourOne(View view){
        tour1=true;
        for(int i=0;i<tourOne.length-1;i++){

            tourRoute1.geodesic(true)
                    .add(new LatLng(tourOne[i].getLatatude(),tourOne[i].getLongatude()))
                    .add(new LatLng(tourOne[i+1].getLatatude(),tourOne[i+1].getLongatude()))
                    .width(15)
                    .color(Color.BLACK);
            tourMarkers1=mMap.addPolyline(tourRoute1);
            if(tourOne[i].getBuildingNumber()>0) {

                mMap.addMarker(new MarkerOptions().position(new LatLng(tourOne[i].getLatatude(), tourOne[i].getLongatude()))
                        .title(buildingArray[tourOne[i].getBuildingNumber() - 1].getBuildingName()));

            }

        }
    }

    public void viewTourTwo(View view){
        tour2=true;
        for(int i=0;i<tourTwo.length-1;i++){

            tourRoute2.geodesic(true)
                    .add(new LatLng(tourTwo[i].getLatatude(),tourTwo[i].getLongatude()))
                    .add(new LatLng(tourTwo[i+1].getLatatude(),tourTwo[i+1].getLongatude()))
                    .width(15)
                    .color(Color.GRAY);
            tourMarkers2=mMap.addPolyline(tourRoute2);
            if(tourTwo[i].getBuildingNumber()>0) {

                mMap.addMarker(new MarkerOptions().position(new LatLng(tourTwo[i].getLatatude(), tourTwo[i].getLongatude()))
                        .title(buildingArray[tourTwo[i].getBuildingNumber() - 1].getBuildingName()));

            }

        }
    }

    public void clearMap(View view){
        mMap.clear();
        if(tour1) {
            tourMarkers1.remove();
        }
        if(tour2){
            tourMarkers2.remove();
        }
        stopTours(view);
    }



    public void buildingMarkers(View view){


        for(int i=0;i<buildingArray.length;i++){

            mMap.addMarker(new MarkerOptions().position(new LatLng(buildingArray[i]
                    .getbLatatude(), buildingArray[i].getbLongatude())).title(buildingArray[i].getBuildingName()));

        }
    }
    private boolean close(Location location, LatLng next){
        double dist = trig(location, next);
        if(dist*10000000<.35){
            return true;
        }
        return false;
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
        //tourDrawer();
        JsonReadTour tTask = new JsonReadTour();
        tTask.execute(new String[] {Turl});

    }
    public void hallDrawer() {
        //


        try {
            if(network) {
                jsonResponse = new JSONObject(jsonResult);
            }
            else{
                jsonResponse = new JSONObject(getString(R.string.halls));
            }
            JSONArray jsonMainNode = jsonResponse.optJSONArray("hall");
            buildingArray=new Building[jsonMainNode.length()];

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                int id = jsonChildNode.optInt("Hall_ID");
                String name = jsonChildNode.optString("name");
                String history = jsonChildNode.optString("history");
                double lat = jsonChildNode.optDouble("latitude");
                double lon = jsonChildNode.optDouble("longitude");
                buildingArray[id-1] = new Building();
                buildingArray[id-1].setbLongatude(lon);
                buildingArray[id-1].setbLatatude(lat);
                buildingArray[id-1].setBuildingName(name);
                buildingArray[id-1].setBuildingNumber(id);
                buildingArray[id-1].setBuildingFacts(history);





            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        // android.R.layout.simple_list_item_1 is predefined in android libraries and not in local xml, and it specifies
        // to the listview how to display the data

    }
    private class JsonReadTour extends AsyncTask<String, Void, String> {

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
                jsonResult2 = inputStreamToString(response.getEntity().getContent()).toString();
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
            tourDrawer();
        }
    }// End Async task

    public void tourDrawer() {
        //this method will take the array of JSON objects and turn them into an array of TourPoint
        //objects so to allow the user to follow or see the tour along these routes
       // TextView text = (TextView)findViewById(R.id.textView2);
        //text.setText(jsonResult2);

        try {
            if(network) {
                jsonResponse = new JSONObject(jsonResult2);
            }
            else{
                jsonResponse = new JSONObject(getString(R.string.tours));
            }
            JSONArray jsonMainNode = jsonResponse.optJSONArray("Tour");
            tours=new TourPoint[jsonMainNode.length()];

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                int id = jsonChildNode.optInt("Tour ID");
                double lat = jsonChildNode.optDouble("Latitude");
                double lon = jsonChildNode.optDouble("Longitude");
                String hall_id=jsonChildNode.optString("Hall ID");
                String point = jsonChildNode.optString("Tour Point ID");


                tours[i]=new TourPoint();
                tours[i].setLatatude(lat);
                tours[i].setLongatude(lon);
                tours[i].setTourNumber(id);
                if(hall_id=="null"){
                    tours[i].setBuildingNumber(0);
                }
                else {
                    tours[i].setBuildingNumber(Integer.parseInt(hall_id));
                }





            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        splitTourArray();


    }
}


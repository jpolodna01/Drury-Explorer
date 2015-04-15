package edu.drury.mcs.icarus.druryexplorer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DruryMap extends FragmentActivity  {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Building[] buildingArray;
    private TourPoint[] tours;
    private TourPoint[] tourOne;
    private TourPoint[] tourTwo;
    private Marker[] markers;
    private List<Marker> setTourOneMarkers;
    private List<Marker> setTourTwoMarkers;
    private Marker newMarker;
    private Marker oldMarker;
    private List<LatLng> setTourOneRoute;
    private List<LatLng> setTourTwoRoute;
    private List<LatLng> tour;
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
    private String Burl ="http://mcs.drury.edu/duexplorer/DUE_PHP/DUE_Hall_Object.php";
    private String Turl="http://mcs.drury.edu/duexplorer/DUE_PHP/DUE_Tour_Objects.php";
    private String Tpurl="http://mcs.drury.edu/duexplorer/DUE_PHP/DUE_Tour_info.php";
    private String Tnum="http://mcs.drury.edu/duexplorer/DUE_PHP/DUE_Number_Tours.php";
    JSONObject jsonResponse;
    private ImageView dImage;
    private Button buildingMarker;
    private Button longTour;
    private Button startLongTour;
    private Button normalTour;
    private Button startNormalTour;
    private Button stop;
    private Button change;
    private SlidingDrawer drawer;
    private ImageButton handle;
    private LinearLayout content;
    //back up array of pictures
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
        //get the different xml objects we need to change
        buildingMarker = (Button)findViewById(R.id.buildingMarkers);
        longTour = (Button)findViewById(R.id.mainTour);
        startLongTour = (Button)findViewById(R.id.startMainTour);
        normalTour = (Button)findViewById(R.id.quickTour);
        startNormalTour = (Button)findViewById(R.id.startQuickTour);
        stop = (Button)findViewById(R.id.stop);
        change = (Button)findViewById(R.id.change);
        drawer = (SlidingDrawer)findViewById(R.id.slidingDrawer);
        handle = (ImageButton)findViewById(R.id.handle);
        content = (LinearLayout)findViewById(R.id.content);
        dImage=(ImageView)findViewById(R.id.imageView2);
        //set up tour booleans for first time use
        firstTime1=true;
        firstTime2=true;
        //set the color of the buttons for normal map view
        if(level==0) {
            change.setBackgroundColor(Color.TRANSPARENT);
            change.setTextColor(Color.BLACK);
            stop.setBackgroundColor(Color.TRANSPARENT);
            stop.setTextColor(Color.BLACK);
            startNormalTour.setBackgroundColor(Color.TRANSPARENT);
            startNormalTour.setTextColor(Color.BLACK);
            normalTour.setBackgroundColor(Color.TRANSPARENT);
            normalTour.setTextColor(Color.BLACK);
            longTour.setBackgroundColor(Color.TRANSPARENT);
            longTour.setTextColor(Color.BLACK);
            startLongTour.setBackgroundColor(Color.TRANSPARENT);
            startLongTour.setTextColor(Color.BLACK);
            buildingMarker.setBackgroundColor(Color.TRANSPARENT);
            buildingMarker.setTextColor(Color.BLACK);
            content.setBackgroundColor(Color.argb(120,250,250,250));
            handle.setImageResource(R.drawable.blackarrow);
        }
        tour = new ArrayList<LatLng>();
        drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener(){
            @Override
            public void onDrawerClosed(){
                if(level==0){
                    handle.setImageResource(R.drawable.blackarrow);
                }
                else{
                      handle.setImageResource(R.drawable.whitearrow);
                }

            }
        });

        drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                if(level==0){
                    handle.setImageResource(R.drawable.blackreverse);
                }
                else{
                    handle.setImageResource(R.drawable.whitereverse);
                }
            }
        });


        //check to see if we have a internet connection and to decided if we use it or the back up data
        network = checkNetwork();
        if(network) {
            hallArray();
        }
        else{
            hallDrawer();
            tourDrawer();
        }
        setUpMapIfNeeded();

         /*
            Changing the on click method for the info bobble above a marker to get the title of the marker
            and compare it to the list of buildings to get the building and send it to the building fact page
         */
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Building clickedHall=new Building();

                //look for the building object that the marker represents
                for(int i=0;i<buildingArray.length;i++){

                    if(buildingArray[i].getbLatatude()==marker.getPosition().latitude & buildingArray[i].getbLongatude()==marker.getPosition().longitude){

                        clickedHall.setBuildingName(buildingArray[i].getBuildingName());
                        clickedHall.setBuildingNumber(buildingArray[i].getBuildingNumber());
                        clickedHall.setBuildingFacts(buildingArray[i].getBuildingFacts());
                        clickedHall.setPicture(buildingArray[i].getPicture());
                        clickedHall.setId(buildingArray[i].getId());
                    }
                }

                Intent intent = new Intent(getApplicationContext(), HallFacts.class);

                //puts the clicked object in the bundle
                intent.putExtra("clickedHall", clickedHall);

                //start the HallFacts activity
                startActivity(intent);
            }
        });



    }

    //This method looks to see if the app is connected to a network or not and then returns a boolean
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
        //this sets up the map to be loaded up at Drury and zoomed into the middle of campus
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.219736, -93.285769), 18));
        //(example of interior map)mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.86997, 151.2089), 18));
        //allow the map to get the users location when gps is on
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();



    }



    /*
        takes the tour array obtained from the json string and turns it into the two different tours
     */

    private void splitTourArray(){
        int place = 0;
        int count = 0;
        //counts to see how big the first tour is
        while(place<tours.length){
            if(tours[place].getTourNumber()==1){
                count++;
            }
            place++;
        }
        //set the size of the two tour arrays
        tourOne=new TourPoint[count];
        tourTwo=new TourPoint[tours.length-count];
        //places the correct tours into the two differnt arrays
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

    /*
        this will create a alert box when called, this allert box is for when the users gps is not on
     */
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

    /*
        this function will stop, reset and erase the two start tour functions
     */
    public void stopTours(View view){
        //drawer.toggle();
        //checks to see if a tour is running and to see if there is anythign to erase
        if(startOne || startTwo){
            if(toured!=null) {
                toured.remove();
            }
            if(newMarker!=null){
                newMarker.remove();
            }
            if(oldMarker!=null){
                oldMarker.remove();
            }
        }
        startOne=false;
        firstTime1=true;
        startTwo=false;
        firstTime2=true;
        setButtonColor(startLongTour);
        setButtonColor(startNormalTour);
    }



    //allows the user to change between three different views of the map
    public void cMap(View view){
        //drawer.toggle();
        //if the map is on satalite view then it is changed to normal view and all buttons are changed to
        //that color scheme
        if(level==2) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            level = 0;
            change.setTextColor(Color.BLACK);
            stop.setTextColor(Color.BLACK);
            setButtonColorView(startTwo,startNormalTour,Color.rgb(155,0,13),Color.BLACK);
            setButtonColorView(tour2,normalTour,Color.rgb(155,0,13),Color.BLACK);
            setButtonColorView(tour1,longTour,Color.rgb(155,0,13),Color.BLACK);
            setButtonColorView(startOne,startLongTour,Color.rgb(155,0,13),Color.BLACK);
            if(markers!=null){
                if(markers[1].isVisible()){buildingMarker.setTextColor(Color.rgb(155,0,13));}
                else{buildingMarker.setTextColor(Color.BLACK);}
            }
            else{buildingMarker.setTextColor(Color.BLACK);}

            content.setBackgroundColor(Color.argb(120,250,250,250));
            handle.setImageResource(R.drawable.blackreverse
            );
            if(tourMarkers2!=null) {
                tourMarkers2.setColor(Color.rgb(51,153,255));
            }
            if(tourMarkers1!=null) {
                tourMarkers1.setColor(Color.YELLOW);
            }
        }
        //if the map is on normal view then the map is changed to satellite and all buttons are
        //changed to that color scheme
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            level = 2;
            change.setTextColor(Color.WHITE);
            stop.setTextColor(Color.WHITE);
            setButtonColorView(startTwo,startNormalTour,Color.rgb(255,0,13),Color.WHITE);
            setButtonColorView(tour2,normalTour,Color.rgb(255,0,13),Color.WHITE);
            setButtonColorView(tour1,longTour,Color.rgb(255,0,13),Color.WHITE);
            setButtonColorView(startOne,startLongTour,Color.rgb(255,0,13),Color.WHITE);
            if(markers!=null){if(markers[1].isVisible()){buildingMarker.setTextColor(Color.rgb(255,0,13));}
            else{buildingMarker.setTextColor(Color.WHITE);}}
            else{buildingMarker.setTextColor(Color.WHITE);}
            content.setBackgroundColor(Color.argb(120,0,0,0));
            handle.setImageResource(R.drawable.whitereverse);
            if(tourMarkers2!=null) {
                tourMarkers2.setColor(Color.rgb(51,153,255));
            }
            if(tourMarkers1!=null) {
                tourMarkers1.setColor(Color.YELLOW);
            }
        }


    }
    /*
        this takes the listener functions and overrides thme to allow the user to take a self quided
        tour of the campus. Depending on what is true and false when this method is called will
        decide what tour is used.
     */
    private void selfTour(){

        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if(startOne && tournum==1) {
                        if (mMap.getMyLocation() != null) {
                            if (firstTime1) {
                                int x = closestBuilding(mMap.getMyLocation(), tourOne);
                                toStart(mMap.getMyLocation(), new LatLng(tourOne[x].getLatatude(), tourOne[x].getLongatude()));
                                if (tourOne[x].getBuildingNumber() > 0) {
                                    newMarker=mMap.addMarker(new MarkerOptions().position(new LatLng(tourOne[x].getLatatude(), tourOne[x].getLongatude())).title(buildingArray[tourOne[x].getBuildingNumber() - 1].getBuildingName()));
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
                                if(newMarker==null & oldMarker!=null){
                                    oldMarker.remove();
                                }

                                if(newMarker!=null){
                                    if(oldMarker!=null){
                                        oldMarker.remove();
                                    }
                                    oldMarker=newMarker;
                                    newMarker=null;
                                }


                                if (tourOne[next].getBuildingNumber() > 0) {

                                    newMarker=mMap.addMarker(new MarkerOptions().position(new LatLng(tourOne[next].getLatatude(), tourOne[next].getLongatude())).title(buildingArray[tourOne[next].getBuildingNumber() - 1].getBuildingName()));
                                    dImage.setImageResource(pic[tourOne[next].getBuildingNumber() - 1]);

                                }

                                toContinue(new LatLng(tourOne[next].getLatatude(), tourOne[next].getLongatude()));
                            }
                        } else {
                            if (firstTime1) {
                                startOne = false;
                                setButtonColor(startLongTour);

                            }
                        }
                    }

                    if(startTwo && tournum==2) {
                        if (mMap.getMyLocation() != null) {
                            if (firstTime2) {
                                Log.e("first","time");
                                int x = closestBuilding(mMap.getMyLocation(), tourTwo);
                                toStart(mMap.getMyLocation(), new LatLng(tourTwo[x].getLatatude(), tourTwo[x].getLongatude()));
                                if (tourTwo[x].getBuildingNumber() > 0) {
                                    newMarker=mMap.addMarker(new MarkerOptions().position(new LatLng(tourTwo[x].getLatatude(), tourTwo[x].getLongatude())).title(buildingArray[tourTwo[x].getBuildingNumber() - 1].getBuildingName()));
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
                                if(newMarker==null & oldMarker !=null){
                                    oldMarker.remove();
                                }
                                if(newMarker!=null){
                                    if(oldMarker!=null){
                                        oldMarker.remove();
                                    }
                                    oldMarker=newMarker;
                                    newMarker=null;
                                }
                                if (tourTwo[next].getBuildingNumber() > 0) {



                                    newMarker=mMap.addMarker(new MarkerOptions().position(new LatLng(tourTwo[next].getLatatude(), tourTwo[next].getLongatude())).title(buildingArray[tourTwo[next].getBuildingNumber() - 1].getBuildingName()));
                                    dImage.setImageResource(pic[tourTwo[next].getBuildingNumber() - 1]);

                                }

                                toContinue(new LatLng(tourTwo[next].getLatatude(), tourTwo[next].getLongatude()));
                            }
                        } else {
                            if (firstTime2) {
                                startTwo = false;
                                setButtonColor(startNormalTour);
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

                    startOne=false;
                    startTwo=false;
                    setButtonColor(startLongTour);
                    setButtonColor(startNormalTour);
                    GPSAlert();

                }
            };
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, listener);

        }
        else{
            startOne=false;
            startTwo=false;
            setButtonColor(startLongTour);
            setButtonColor(startNormalTour);
            GPSAlert();

        }
    }

    /*
        setButtonColor takes a look at a button a decides if the map is normal or satalite and sets the
        off color accordingly
     */
    private void setButtonColor(Button but){
        if(level==0){
            but.setTextColor(Color.BLACK);
        }
        else{
            but.setTextColor(Color.WHITE);
        }
    }
    /*
        setOnButtonColor takes a look at a button a decides if the map is normal or satalite and sets the
        on color accordingly
     */
    private void setOnButtonColor(Button but){
        if(level==0){
            but.setTextColor(Color.rgb(155,0,13));
        }
        else{
            but.setTextColor(Color.rgb(255,0,13));
        }
    }

    /*
        setButtonColorView takes a look at if a button is on or off and sets it's color
     */

    private void setButtonColorView(Boolean on,Button but, int onColor, int offColor){
        if(on){
            but.setTextColor(onColor);
        }
        else{
            but.setTextColor(offColor);
        }
    }
/*
    this method starts the self guided tour for tour one while stopping any other tours in action, it also
    stops tour two if it is pressed while the tour is in action
     */

    public void startTourOne(View view){
        //drawer.toggle();

        if(startTwo){
            stopTours(view);
        }
        if(!startOne) {
            startOne = true;
            firstTime1=true;
            tournum=1;
            setOnButtonColor(startLongTour);
            selfTour();
        }
        else{
            startOne=false;
            firstTime1=true;
            if(toured!=null) {
                toured.remove();
            }
            if(newMarker!=null){
                newMarker.remove();
            }
            if(oldMarker!=null){
                oldMarker.remove();
            }
            setButtonColor(startLongTour);
        }


    }
    /*
    this method starts the self guided tour for tour two while stopping any other tours in action, it also
    stops tour two if it is pressed while the tour is in action
     */
    public void startTourTwo(View view){
       //drawer.toggle();
        if(startOne){
            stopTours(view);
        }
        if(!startTwo) {
            startTwo = true;
            firstTime2=true;
            tournum=2;
            setOnButtonColor(startNormalTour);
            selfTour();
        }
        else{
            startTwo = false;
            firstTime2= true;
            if(toured!=null) {
                toured.remove();
            }
            setButtonColor(startNormalTour);
            if(newMarker!=null){
                newMarker.remove();
            }
            if(oldMarker!=null){
                oldMarker.remove();
            }

        }

    }
    /*
    this method is used to caculated the distance between to LatLng points on a map
     */
    private double trig(Location loc1, LatLng loc2){
        double lat1 = loc1.getLatitude();
        double lat2 = loc2.latitude;
        double long1 = loc1.getLongitude();
        double long2 = loc2.longitude;

        double dif =(((lat1-lat2)*(lat1-lat2))+((long1-long2)*(long1-long2)));

        return dif;
    }
    /*
    this method decides which building in a tour the user is closestest to.
     */
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


    /*
    this method sets up the polyline for a selfguided tour
     */
    private void toStart(Location now, LatLng there){
        toured = mMap.addPolyline(tourRoute1);
        toured.setWidth(12);
        toured.setColor(Color.rgb(155,0,13));
        LatLng here = new LatLng(now.getLatitude(),now.getLongitude());
        if(tour.size()==2) {
            tour.set(0, here);
            tour.set(1, there);
        }
        else {
            tour.add(here);
            tour.add(there);
        }
        toured.setPoints(tour);
        toured.setVisible(true);

    }
    /*
    this method is used to continue a self guided tour seting the polyline of the tour to the next
    part of the tour
     */
    private void toContinue(LatLng there){
        tour.set(0,tour.get(1));
        tour.set(1,there);


        toured.setPoints(tour);

    }

    /*
    this method takes the array of tourpoints for tour one and makes a polyline with markers
    at the buildings along the route. it will remove the polyline and markers if they are already
    displayed it also changes the color of the button to corespond with being in use or out of use
    as well as compensating for the different types of map views
     */
    public void viewTourOne(View view){
        //drawer.toggle();
        if(tour2){
            tour2=false;
            tourMarkers2.setVisible(false);
            for(int y=0;y<setTourTwoMarkers.size();y++){
                setTourTwoMarkers.get(y).setVisible(false);
            }
            if(level==0){
                normalTour.setTextColor(Color.BLACK);
            }
            else{
                normalTour.setTextColor(Color.WHITE);
            }
        }
        if(tourMarkers1!=null & tour1) {
            tour1=false;
            tourMarkers1.setVisible(false);
            for(int y=0;y<setTourOneMarkers.size();y++){
                setTourOneMarkers.get(y).setVisible(false);
            }
            if(level==0){
                longTour.setTextColor(Color.BLACK);
            }
            else{
                longTour.setTextColor(Color.WHITE);
            }
        }
        else if(tourMarkers1==null){
            tour1=true;
            setTourOneRoute= new ArrayList<LatLng>();
            setTourOneMarkers= new ArrayList<Marker>();

            for (int i = 0; i < tourOne.length ; i++) {
                setTourOneRoute.add(new LatLng(tourOne[i].getLatatude(), tourOne[i].getLongatude()));


                if (tourOne[i].getBuildingNumber() > 0) {

                   setTourOneMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(tourOne[i].getLatatude(), tourOne[i].getLongatude()))
                           .title(buildingArray[tourOne[i].getBuildingNumber() - 1].getBuildingName())));


                }

            }

            tourMarkers1 = mMap.addPolyline(tourRoute1);
            tourMarkers1.setPoints(setTourOneRoute);
            if(level==0){
                tourMarkers1.setColor(Color.YELLOW);
                longTour.setTextColor(Color.rgb(155,0,13));
            }
            else{
                tourMarkers1.setColor(Color.YELLOW);
                longTour.setTextColor(Color.rgb(255,0,13));
            }
            tourMarkers1.setWidth(12);

        }
        else{
            tourMarkers1.setVisible(true);
            for(int y=0;y<setTourOneMarkers.size();y++){
                setTourOneMarkers.get(y).setVisible(true);
            }
            if(level==0){
                tourMarkers1.setColor(Color.YELLOW);
                longTour.setTextColor(Color.rgb(155,0,13));
            }
            else{
                tourMarkers1.setColor(Color.YELLOW);
                longTour.setTextColor(Color.rgb(255,0,13));
            }

            tour1=true;
        }


    }
    /*
    this method takes the array of tourpoints for tour two and makes a polyline with markers
    at the buildings along the route. it will remove the polyline and markers if they are already
    displayed it also changes the color of the button to corespond with being in use or out of use
    as well as compensating for the different types of map views
     */
    public void viewTourTwo(View view){
        //drawer.toggle();
        if(tour1){
            tour1=false;
            tourMarkers1.setVisible(false);
            for(int y=0;y<setTourOneMarkers.size();y++){
                setTourOneMarkers.get(y).setVisible(false);
            }
            if(level==0){
                longTour.setTextColor(Color.BLACK);
            }
            else{
                longTour.setTextColor(Color.WHITE);
            }
        }
        if(tourMarkers2!=null & tour2) {
            tour2=false;
            tourMarkers2.setVisible(false);
            for(int y=0;y<setTourTwoMarkers.size();y++){
                setTourTwoMarkers.get(y).setVisible(false);
            }
            if(level==0){
                normalTour.setTextColor(Color.BLACK);
            }
            else{
                normalTour.setTextColor(Color.WHITE);
            }

        }
        else if(tourMarkers2==null){
            tour2 = true;
            setTourTwoRoute= new ArrayList<LatLng>();
            setTourTwoMarkers= new ArrayList<Marker>();

            for (int i = 0; i < tourTwo.length ; i++) {
                setTourTwoRoute.add(new LatLng(tourTwo[i].getLatatude(), tourTwo[i].getLongatude()));


                if (tourTwo[i].getBuildingNumber() > 0) {

                    setTourTwoMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(tourTwo[i].getLatatude(), tourTwo[i].getLongatude()))
                            .title(buildingArray[tourTwo[i].getBuildingNumber() - 1].getBuildingName())));


                }

            }

            tourMarkers2 = mMap.addPolyline(tourRoute2);
            tourMarkers2.setPoints(setTourTwoRoute);
            if(level==0){
                tourMarkers2.setColor(Color.rgb(51,153,255));
                normalTour.setTextColor(Color.rgb(155,0,13));
            }
            else{
                tourMarkers2.setColor(Color.rgb(51,153,255));
                normalTour.setTextColor(Color.rgb(255,0,13));
            }
            tourMarkers2.setWidth(12);
        }
        else{
            tourMarkers2.setVisible(true);
            for(int y=0;y<setTourTwoMarkers.size();y++){
                setTourTwoMarkers.get(y).setVisible(true);
            }
            if(level==0){
                tourMarkers2.setColor(Color.rgb(51,153,255));
                normalTour.setTextColor(Color.rgb(155,0,13));
            }
            else{
                tourMarkers2.setColor(Color.rgb(51,153,255));
                normalTour.setTextColor(Color.rgb(255,0,13));
            }

            tour2=true;

        }
    }



    /*
        The buildingMarkers method takes the array of buildings on campus and makes and array of markers
        when a building is pressed the first time and displaying thouse markers. After that it checks if the
        markers are visible or not, if they are it will remove them and if they are will display them. It will also
        toggle the sliding drawer closed with the button press.
        view-is the current view the button is in
     */

    public void buildingMarkers(View view){
       //drawer.toggle();
        if(markers==null) {
            markers = new Marker[buildingArray.length];
            for (int i = 0; i < buildingArray.length; i++) {

                markers[i] = mMap.addMarker(new MarkerOptions().position(new LatLng(buildingArray[i]
                        .getbLatatude(), buildingArray[i].getbLongatude())).title(buildingArray[i].getBuildingName()));



            }
            if(level==2) {
                buildingMarker.setTextColor(Color.rgb(255, 0, 13));
            }
            else{
                buildingMarker.setTextColor(Color.rgb(155,0,13));
            }
        }
        else if (markers[1].isVisible()){
            for (int i = 0; i<markers.length;i++){
                markers[i].setVisible(false);
                if(level==0){
                    buildingMarker.setTextColor(Color.BLACK);
                }
                else{
                    buildingMarker.setTextColor(Color.WHITE);
                }
            }
        }
        else{
            for (int i = 0; i<markers.length;i++){
                markers[i].setVisible(true);
                if(level==2) {
                    buildingMarker.setTextColor(Color.rgb(255, 0, 13));
                }
                else{
                    buildingMarker.setTextColor(Color.rgb(155,0,13));
                }

            }
        }
    }

    //checks to see if user is current to a certain location
    private boolean close(Location location, LatLng next){
        double dist = trig(location, next);
        if(dist*10000000<.35){
            return true;
        }
        return false;
    }

    /**
     * Sub-class of of the DruryMap class, which is used for handling the asynchronous functionality.
     * Its nature forces sub-classing for implementation; several methods such as doInBackground are overwritten
     * to handle the asynchronous threads as they are appropriate for this app.
     * <p/>
     * Author: Josef Polodna/Daiv McBride
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
			to utilize the hallDrawer method defined in the outer-class

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
        //this method takes a json string and turns them into an array of building objects


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
                String pic = jsonChildNode.optString("ImageURL");
                String sID = ""+id;
                buildingArray[id-1] = new Building();
                buildingArray[id-1].setbLongatude(lon);
                buildingArray[id-1].setbLatatude(lat);
                buildingArray[id-1].setBuildingName(name);
                buildingArray[id-1].setBuildingNumber(id);
                buildingArray[id-1].setBuildingFacts(history);
                buildingArray[id-1].setPicture(pic);
                buildingArray[id-1].setId(sID);





            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }


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
			to utilize the tourDrawer method defined in the outer-class

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


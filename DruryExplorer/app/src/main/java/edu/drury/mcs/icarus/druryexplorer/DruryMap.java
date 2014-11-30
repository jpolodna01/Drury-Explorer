package edu.drury.mcs.icarus.druryexplorer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

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
    private LatLng bay = new LatLng(37.218116,-93.286308);
    private LatLng shewmaker = new LatLng(37.217034,-93.286043);
    private LatLng WBC = new LatLng(37.216265,-93.286067);
    private LatLng TSC = new LatLng(37.215741,-93.286208);
    private LatLng Hammons = new LatLng(37.215694,-93.285476);
    private LatLng Breech = new LatLng(37.217888,-93.285671);
    private LatLng Springfield = new LatLng(37.218524,-93.285630);
    private LatLng Gym = new LatLng(37.219232,-93.285612);
    private LatLng FSC = new LatLng(37.221037, -93.285309);
    private LatLng freeman = new LatLng(37.220804, -93.284343);
    private LatLng smith = new LatLng(37.221645, -93.284861);
    private LatLng wallace = new LatLng(37.221364, -93.285687);
    private LatLng sunderland = new LatLng(37.221336, -93.286516);
    private LatLng president = new LatLng(37.221800, -93.287127);
    private LatLng manley = new LatLng(37.221874, -93.287682);
    private LatLng congregational = new LatLng(37.221875, -93.287684);
    private LatLng parsonage = new LatLng(37.222208, -93.286704);
    private LatLng collegepark = new LatLng(37.222532, -93.288681);
    private LatLng studentcenter = new LatLng(37.222856, -93.288742);
    private LatLng mabee = new LatLng(37.220449, -93.287076);
    private LatLng philosopher = new LatLng(37.219969, -93.287103);
    private LatLng lay = new LatLng(37.219512, -93.286778);
    private LatLng olin = new LatLng(37.219318, -93.286156);
    private LatLng[] tour1 = {bay,shewmaker,WBC,TSC,Hammons,Breech,Springfield,Gym,FSC,freeman,smith,wallace,
            sunderland,president,manley,congregational,parsonage,collegepark,studentcenter,mabee,philosopher,lay,olin};
    private Boolean firstTime=true;
    private Boolean closeToNext=false;
    private PolylineOptions tourRoute = new PolylineOptions();
    private PolylineOptions touring = new PolylineOptions();
    private int next=0;
    private int times=0;
    private Boolean  tour= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drury_map);
        firstTime=true;
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
                if(tour) {
                    if (firstTime) {
                        int i = closestBuilding(location, tour1);
                        toStart(location, tour1[i]);
                        firstTime = false;
                        next = i;
                    }
                    if (close(location, tour1[next]) && location.hasAccuracy()) {
                        if (next == tour1.length - 1) {
                            next = 0;
                        } else {
                            next++;
                        }
                        toStart(location, tour1[next]);
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
    public void fullTour(View view){
        for(int i=0;i<tour1.length-1;i++){

            tourRoute.geodesic(true)
                    .add(tour1[i])
                    .add(tour1[i+1])
                    .width(15);
            mMap.addPolyline(tourRoute);

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

}


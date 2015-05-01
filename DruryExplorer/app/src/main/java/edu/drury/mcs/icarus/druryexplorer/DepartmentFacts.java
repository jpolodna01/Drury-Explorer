package edu.drury.mcs.icarus.druryexplorer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DepartmentFacts extends Activity {

    private TextView name;
    private TextView location;
    private TextView discription;
    private Building building;
    private int buildingNumber;
    private String jsonResult;
    JSONObject jsonResponse;

    private String Burl ="http://mcs.drury.edu/duexplorer/DUE_PHP/DUE_Hall_Object.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_facts);

        name = (TextView) findViewById(R.id.dNameView);
        location =(TextView) findViewById(R.id.locationView);
        discription =(TextView) findViewById(R.id.descriptionView);
        location.setTextColor(Color.BLUE);

        ActionBar bar = getActionBar();

        //creates a string to place the clicked object
        String newDName;
        String newLocation;
        String newDescription;

        if(savedInstanceState == null)
        {
            //get the bundle from Departments.java
            Bundle extras = getIntent().getExtras();
            Department depar = (Department)extras.getParcelable("clickedDept");

            //if there is nothing there then the string is empty
            if(extras == null)
            {
                newDName = null;
                newLocation = null;
                newDescription = null;
            }

            //if there is something there then get the string from the bundle
            else
            {
                newDName = depar.getName();
                newLocation = "Located in: " + depar.getLocation();
                newDescription = depar.getDescription();
                buildingNumber= Integer.parseInt(depar.getNumber());
            }
        }
        else
        {
            Department depar = (Department) savedInstanceState.getSerializable("clickedDept");
            newDName = depar.getName();
            newLocation = "Located in: "+depar.getLocation();
            newDescription = depar.getDescription();
            buildingNumber= Integer.parseInt(depar.getNumber());
        }

        //display the name of the clicked department
        name.setText(newDName);
        location.setText(newLocation);
        discription.setText(newDescription);
        retrieveBuilding();
        bar.setTitle(newDName + " Facts");
    }

    ////////////////methods to set building name clickable and got to building factpage//////////

    public void openBuildingFact(View v){
        String bname = building.getBuildingName();
        Log.e("building",bname);
        Intent intent = new Intent(getApplicationContext(), BuildingFacts.class);

        //puts the clicked object in the bundle
        intent.putExtra("clickedHall", building);

        //start the HallFacts activity
        startActivity(intent);
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
            retrieveHall();
        }
    }// End Async task

    public void retrieveBuilding() {
        JsonReadHalls task = new JsonReadHalls();
        // passes values for the urls string array
        task.execute(new String[] { Burl });

    }

    public void retrieveHall() {
        //this method takes a json string and turns them into an array of building objects


        try {
            if(checkNetwork()) {
                jsonResponse = new JSONObject(jsonResult);
            }
            else{
                jsonResponse = new JSONObject(getString(R.string.halls));
            }

            JSONArray jsonMainNode = jsonResponse.optJSONArray("hall");
            boolean found = false;
            int i =0;
            while (found!=true) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                int id = jsonChildNode.optInt("Hall_ID");
                String num = ""+buildingNumber;
                if(id==buildingNumber){
                    building = new Building();
                    String name = jsonChildNode.optString("name");
                    String history = jsonChildNode.optString("history");
                    double lat = jsonChildNode.optDouble("latitude");
                    double lon = jsonChildNode.optDouble("longitude");
                    String pic = jsonChildNode.optString("ImageURL");
                    String sID = ""+id;
                    building.setbLongatude(lon);
                    building.setbLatatude(lat);
                    building.setBuildingName(name);
                    building.setBuildingNumber(id);
                    building.setBuildingFacts(history);
                    building.setPicture(pic);
                    building.setId(sID);
                    found=true;
                }
                i++;





            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }


    }


    /////////////methods for android menu /////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent about = new Intent(this, About_Page.class);
            startActivity(about);
            return true;
        }
        if (id == R.id.about_drury) {
            Intent drury = new Intent(this, about_drury.class);
            startActivity(drury);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package edu.drury.mcs.icarus.druryexplorer;
/*
* Author: Daniel Chick && Josef Polodna
* */

/*********** original sqlite imports *************/
// import android.app.Activity;
// import android.content.Intent;
// import android.os.Bundle;
// import android.view.Menu;
// import android.view.MenuItem;
   import android.content.Context;
   import android.net.ConnectivityManager;
   import android.net.NetworkInfo;
   import android.widget.ArrayAdapter;
// import android.widget.ListView;
// import android.widget.Toast;

//import java.io.IOException;
//import java.util.List;
/************************************************/

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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
   import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
   import android.widget.Toast;

/**
 Author(s): Josef Polodna (Current Version), Daniel Chick and Josef Polodna (Old Version)
 Version: 0.7 - Josef Polodna, working on adding in functionality and support for mysql
 */
public class Halls extends Activity {

    //instantiate variables for table elements
    private String name, history;
    private double latitude, longitude;
    private int id;
    private JSONObject jsonResponse;
    private Boolean net;


    // This is the list in which all of the hall objects will be stored. Muy importante
    public List<Building> hallList;

    private String jsonResult; // string to store the json result
    private String url = "http://mcs.drury.edu/duexplorer/DUE_PHP/DUE_Hall_Object.php"; //url to the php echo'ed data
    private ListView listView; // listview variable

     /* This is the onCreate method required via the extension. It is the operations preformed on the creation of the app
        @param savedInstanceState - This bundle type parameter is used to take the data from the saved instance state
                                    as a bundle for passing between states and activities
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Use parent (onCreate) with the bundle
        setContentView(R.layout.activity_halls);
        listView = (ListView) findViewById(R.id.hallView); // same as above
        net=checkNetwork();
        if(net) {
            accessWebService();
        }
        else{
            populateListView();
        }


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


    /* Overrides the parent functionality for the method of the same name to inflate the action bar items
        if the action bar is present

        @param menu - Represents the menu to be inflated
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu); // inflate the menu, adding items to action bar if present
        return true; // return true to indicate usage
    }

    /**
     * Sub-class of of the Department class, which is used for handling the asynchronous functionality.
     * Its nature forces sub-classing for implementation; several methods such as doInBackground are overwritten
     * to handle the asynchronous threads as they are appropriate for this app.
     * <p/>
     * Author: Josef Polodna
     */
    private class JsonReadTask extends AsyncTask<String, Void, String> {

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
            populateListView();
        }
    }// End Async task

    // access the web service
    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url});
    }

    // build hash set for list view

    /**
     * This method will be altered to draw the data from the database and insert it in a list
     */
    public void ListDrwaer() {

         hallList = new ArrayList<Building>();

        try {
            if(net) {
                jsonResponse = new JSONObject(jsonResult);
            }
            else{
                jsonResponse = new JSONObject(getString(R.string.halls
                ));
            }
            JSONArray jsonMainNode = jsonResponse.optJSONArray("hall");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                Building hallObject = new Building();
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                hallObject.setBuildingNumber(Integer.parseInt(jsonChildNode.optString("Hall_ID")));
                hallObject.setBuildingName(jsonChildNode.optString("name"));
                hallObject.setBuildingFacts(jsonChildNode.optString("history"));
                hallObject.setPicture(jsonChildNode.optString("ImageURL"));
                String id =""+Integer.parseInt(jsonChildNode.optString("Hall_ID"));
                hallObject.setId(id);
                hallList.add(hallObject);
               //String outPut = name;
               // hallList.add(createDepartment("halls", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void populateListView()
    {
        ListDrwaer();
//        ListView mainListView;
//        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.activity_halls, namesList);

        if(hallList.size()>0) // check if list contains items.
        {
            ListView lv = (ListView) findViewById(R.id.hallView);
            final ArrayAdapter<Building> arrayAdapter = new ArrayAdapter<Building>(this, android.R.layout.simple_list_item_1, hallList);

            lv.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Selected item in the list
                    Building clickedHall = arrayAdapter.getItem(position);

                    //creates a new intent that will open the HallFacts activity
                    Intent i = new Intent(getApplicationContext(), BuildingFacts.class);

                    //puts the clicked object in the bundle
                    i.putExtra("clickedHall", clickedHall);

                    //start the HallFacts activity
                    startActivity(i);
                }
            });
        }
        else
        {
            Toast.makeText(Halls.this, "Daniel says no data from DB", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String toString(){
        return name;
    }

}
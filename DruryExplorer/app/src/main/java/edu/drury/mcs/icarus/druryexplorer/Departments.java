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
// import android.widget.ArrayAdapter;
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 Author(s): Josef Polodna (Current Version), Daniel Chick and Josef Polodna (Old Version)
 Version: 0.7 - Josef Polodna, working on adding in functionality and support for mysql
 */
public class Departments extends Activity {

    //instantiate variables for table elements
/*     public String _name, _description, _location;
    public int _id; */

    private String jsonResult; // string to store the json result
    private String url = "http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_DataManager_Departments.php"; //url to the php echo'ed data
    private ListView listView; // listview variable

    /* This is the onCreate method required via the extension. It is the operations preformed on the creation of the app
        @param savedInstanceState - This bundle type parameter is used to take the data from the saved instance state
                                    as a bundle for passing between states and activities
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Use parent (onCreate) with the bundle
        setContentView(R.layout.activity_departments);
        listView = (ListView) findViewById(R.id.deptView); // same as above
        accessWebService(); //
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
            ListDrwaer();
        }
    }// End Async task

    // access the web service
    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // build hash set for list view
    public void ListDrwaer() {
        //
        List<Map<String, String>> departmentList = new ArrayList<Map<String, String>>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("department");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String outPut = name;
                departmentList.add(createDepartment("departments", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        // android.R.layout.simple_list_item_1 is predefined in android libraries and not in local xml, and it specifies
        // to the listview how to display the data
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, departmentList, android.R.layout.simple_list_item_1,
                new String[]{"departments"}, new int[]{android.R.id.text1}); //android.R.id.text1 defined by android and not locally
        listView.setAdapter(simpleAdapter);// set the listview to the previously created adapter (write data to screen)
    }

    /* As a hashmap, this associates data with a certain key in such a way that each piece has a unique key

    */
    private HashMap<String, String> createDepartment(String name, String number) {
        HashMap<String, String> departmentName = new HashMap<String, String>();
        departmentName.put(name, number);
        return departmentName;
    }
}
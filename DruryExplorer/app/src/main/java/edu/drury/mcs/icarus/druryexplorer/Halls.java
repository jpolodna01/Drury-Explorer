package edu.drury.mcs.icarus.druryexplorer;
/*
* Author: Daniel Chick
* */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/*
The Hall class contains the get and set methods to set each of the variables required for a hall.
It also contains the methods to connect to the SQLite database to pull information for the halls.
 */
public class Halls extends Activity {

    //////////////////////////////Variables///////////////////////////////

    //instantiate variables for table elements
    public String _name, _history;
    public int _id, _year;

    /////////////////////////////Constructors//////////////////////////////////

    //empty constructor
    public Halls(){}

    //constructor with all table elements
    public Halls(int id, String name, int year, String history)
    {
        this._id=id;
        this._name = name;
        this._year = year;
        this._history = history;
    }

    //constructor with all table elements minus id
    public Halls(String name, int year, String history)
    {
        this._name = name;
        this._year = year;
        this._history = history;
    }

    /////////////////////////Methods/////////////////////////////

    //Get Methods
    public int getID(){
        return this._id;
    }
    public String getName(){return this._name;}
    public int getYear(){return this._year;}
    public String getHistory(){return this._history;}

    //set Methods
    public void setID(int ID){this._id = ID;}
    public void setName(String name){this._name=name;}
    public void setYear(int year){this._year=year;}
    public void setHistory(String history){this._history=history;}

    DataManager db = new DataManager(Halls.this);

    //on create it will move the db from assets folder to sdcard0 then fills the list view with the halls
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halls);

        preLoadDb();
        populateListView();
    }

    //populateListView pulls the list of hall names from the database and puts them on the screen in a list
    public void populateListView()
    {
        List<Halls> all = db.getHallList();
        if(all.size()>0) // check if list contains items.
        {
            ListView lv = (ListView) findViewById(R.id.hallView);
            ArrayAdapter<Halls> arrayAdapter = new ArrayAdapter<Halls>(this, android.R.layout.simple_list_item_1, all);

            lv.setAdapter(arrayAdapter);
        }
        else
        {
            Toast.makeText(Halls.this, "Daniel says no data from DB", Toast.LENGTH_LONG).show();
        }
    }

    //tostring function turns the address of name to a string
    @Override
    public String toString() {
        return _name;
    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    //
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
        return super.onOptionsItemSelected(item);
    }

    //
    public void preLoadDb()
    {
        try
        {
            db.create();
        }
        catch(IOException ioe)
        {
            throw new Error("Unable to create Database");
        }
    }

    /*public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);


    }*/

}

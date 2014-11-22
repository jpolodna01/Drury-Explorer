package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


public class Halls extends Activity {

    public String _name, _history;
    public int _id, _year;

    //empty constructor
    public Halls(){

    }


    public Halls(int id, String name, int year, String history)
    {
        this._id=id;
        this._name = name;
        this._year = year;
        this._history = history;
    }

    public Halls(String name, int year, String history)
    {
        this._name = name;
        this._year = year;
        this._history = history;
    }


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

    DataManager db=new DataManager(Halls.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halls);

        preLoadDb();
        populateListView();
    }

    public void populateListView()
    {
        DataManager entry = new DataManager(Halls.this);

        List<Halls> all = entry.getHallList();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.halls, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void preLoadDb()
    {
        DataManager db = new DataManager(this);

        try
        {
            db.create();
        }
        catch(IOException ioe)
        {
            throw new Error("Unable to create Database");
        }
    }

}

package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


public class Departments extends Activity {

    public String _name, _description, _location;
    public int _id;

    //empty constructor
    public Departments(){}

    //constructor with all table elements
    public Departments(int id, String name, String description, String location)
    {
        this._id=id;
        this._name=name;
        this._description=description;
        this._location=location;
    }

    //constructor with all table elements minus id
    public Departments(String name, String description, String location)
    {
        this._name=name;
        this._description=description;
        this._location=location;
    }

    //Get methods
    public int getID() {return this._id;}
    public String getName() {return this._name;}
    public String getDescription() {return this._description;}
    public String getLocation() {return this._location;}

    public void setID(int ID){this._id=ID;}
    public void setName(String name){this._name=name;}
    public void setDescription(String description){this._description=description;}
    public void setLocation(String location){this._location=location;}

    DataManager db = new DataManager(Departments.this);

    //on create it will move the db from assets folder to sdcard0 then fills the list view with the departments
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        preLoadDb();
        populateListView();
    }

    //populateListView pulls the list of department names from the database and puts them on the screen in a list
    public void populateListView()
    {
        List<Departments> all = db.getDeptList();
        if(all.size()>0) //check if list contains items.
        {
            ListView lv = (ListView) findViewById(R.id.deptView);
            ArrayAdapter<Departments> arrayAdapter = new ArrayAdapter<Departments>(this, android.R.layout.simple_list_item_1, all);

            lv.setAdapter(arrayAdapter);
        }
        else
        {
            Toast.makeText(Departments.this, "Daniel says no data from DB", Toast.LENGTH_LONG).show();
        }
    }

    //toString function turns the address of name to a string
    @Override
    public String toString() {return _name;}

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

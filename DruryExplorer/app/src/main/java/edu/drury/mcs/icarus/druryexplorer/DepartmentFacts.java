package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


public class DepartmentFacts extends Activity {

    private TextView name;
    private TextView location;
    private TextView discription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_facts);

        name = (TextView) findViewById(R.id.dNameView);
        location =(TextView) findViewById(R.id.locationView);
        discription =(TextView) findViewById(R.id.descriptionView);

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
            }
        }
        else
        {
            Department depar = (Department) savedInstanceState.getSerializable("clickedDept");
            newDName = depar.getName();
            newLocation = "Located in: "+depar.getLocation();
            newDescription = depar.getDescription();
        }

        //display the name of the clicked department
        name.setText(newDName);
        location.setText(newLocation);
        discription.setText(newDescription);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_department_facts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

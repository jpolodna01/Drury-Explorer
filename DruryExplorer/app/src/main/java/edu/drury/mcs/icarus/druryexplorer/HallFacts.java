package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HallFacts extends Activity {

    public TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_facts);
        textView1 = (TextView) findViewById(R.id.hNameView);

        //creates a string to place the clicked object
        String newName;
        if(savedInstanceState == null)
        {
            //get the bundle from Halls.java
            Bundle extras = getIntent().getExtras();

            //if there is nothing there then the string is empty
            if(extras == null)
            {
                newName = null;
            }

            //if there is something there the get the string from the bundle
            else
            {
                newName = extras.getString("clickedHall");
            }
        }
        else
        {
            newName = (String) savedInstanceState.getSerializable("clickedHall");
        }

        //display the name of the clicked hall
        textView1.setText(newName);
    }


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent about = new Intent(this, About_Page.class);
            startActivity(about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

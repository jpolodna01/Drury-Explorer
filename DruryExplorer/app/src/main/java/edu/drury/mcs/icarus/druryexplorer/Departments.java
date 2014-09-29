package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Departments extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
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
        if(id==R.id.action_Buildings){
            Intent intent = new Intent(this, Buildings.class);
            startActivity(intent);
        }
        if(id==R.id.action_map){
            Intent intent = new Intent(this, DruryMap.class);
            startActivity(intent);
        }
        if(id==R.id.action_departments){
            Intent intent = new Intent(this, Departments.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

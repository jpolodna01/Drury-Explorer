package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Buildings extends Activity {
private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings);

        lv =(ListView) findViewById(R.id.buildinglist);

        List<String> buildingArray = new ArrayList<String>();
        buildingArray.add("Pearson Hall");
        buildingArray.add("Smith Hall");
        buildingArray.add("Bay Hall");
        buildingArray.add("Stone Chapel");
        buildingArray.add("Burnham Hall");
        buildingArray.add("Breech");
        buildingArray.add("Springfield Hall");
        buildingArray.add("Barabra Fitness Center");
        buildingArray.add("Trustee Science Center");
        buildingArray.add("Washington Baptist Church");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildingArray);
        lv.setAdapter(arrayAdapter);
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

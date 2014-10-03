package com.example.dmcbride03.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class buildings extends Activity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings);
        lv=(ListView) findViewById(R.id.listView);

        List<String> buildingArray = new ArrayList<String>();
        buildingArray.add("Math");
        buildingArray.add("Computer Science");
        buildingArray.add("Englis");
        buildingArray.add("Philosophy");
        buildingArray.add("Academics");
        buildingArray.add("Admissions");
        buildingArray.add("Athletics");
        buildingArray.add("Science");
        buildingArray.add("Art");
        buildingArray.add("Communication");
        buildingArray.add("Security");
        buildingArray.add("Business");
        buildingArray.add("Teaching");
        buildingArray.add("History");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildingArray);
        lv.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.buildings, menu);
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
}

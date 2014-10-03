package com.example.dmcbride03.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class halls extends Activity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halls);
        lv=(ListView) findViewById(R.id.listView);

        List<String> buildingArray = new ArrayList<String>();
        buildingArray.add("Pearsons Hall");
        buildingArray.add("Springfield Hall");
        buildingArray.add("Bay Hall");
        buildingArray.add("Stone Chapel");
        buildingArray.add("Burnham");
        buildingArray.add("Breech");
        buildingArray.add("Barabra Fitness Center");
        buildingArray.add("Trustee Science Center");
        buildingArray.add("Washingto Baptist Church");
        buildingArray.add("Pool Art Center");
        buildingArray.add("Oriely Center");
        buildingArray.add("Findley Student Center");
        buildingArray.add("Freeman");
        buildingArray.add("Shewmaker");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildingArray);
        lv.setAdapter(arrayAdapter);
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
}

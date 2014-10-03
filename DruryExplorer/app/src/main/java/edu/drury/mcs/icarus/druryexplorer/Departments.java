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


public class Departments extends Activity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        lv =(ListView) findViewById(R.id.department_list);

        List<String> departmentArray = new ArrayList<String>();
        departmentArray.add("Math and Computer Science");
        departmentArray.add("English");
        departmentArray.add("History");
        departmentArray.add("Business");
        departmentArray.add("Theater");
        departmentArray.add("Information Technology");
        departmentArray.add("Athletics");
        departmentArray.add("Science");
        departmentArray.add("Communication");
        departmentArray.add("Admissions");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, departmentArray);
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

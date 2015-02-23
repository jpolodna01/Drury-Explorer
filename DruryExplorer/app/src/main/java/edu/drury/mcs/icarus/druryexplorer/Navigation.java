package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;


public class Navigation extends TabActivity {
    TabHost th;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        th=(TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec spec = th.newTabSpec("tab1");
        spec.setContent(new Intent(this,DruryMap.class));
        spec.setIndicator("Drury Map");
        th.addTab(spec);

        TabHost.TabSpec spec2 = th.newTabSpec("tab2");
        spec2.setContent(new Intent(this,Halls.class));
        spec2.setIndicator("Halls");
        th.addTab(spec2);

        TabHost.TabSpec spec3 = th.newTabSpec("tab3");
        spec3.setContent(new Intent(this,Departments.class));
        spec3.setIndicator("Departments");
        th.addTab(spec3);

<<<<<<< HEAD
        //hides the actionbar above the tabs
=======
>>>>>>> f768bd9f76dcd94e5372c344b13305e4ca5fb88d
        getActionBar().hide();
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
        if (id == R.id.action_about) {
            Intent about = new Intent(this, About_Page.class);
            startActivity(about);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        spec2.setIndicator("Buildings");
        th.addTab(spec2);

        TabHost.TabSpec spec3 = th.newTabSpec("tab3");
        spec3.setContent(new Intent(this,Departments.class));
        spec3.setIndicator("Departments");
        th.addTab(spec3);
        getTabHost().animate();


        for(int i=0; i<getTabHost().getTabWidget().getChildCount();i++){
            View v = getTabHost().getTabWidget().getChildAt(i);
            View w = getTabHost().getRootView();
            w.setBackgroundColor(Color.WHITE);

            TextView tv = (TextView) getTabHost().getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.WHITE);
        }

        Boolean net = checkNetwork();
        if(!net){
            networkAlert();
        }

    }

    public void networkAlert(){
        AlertDialog.Builder network = new AlertDialog.Builder(this);
        network.setTitle("No Network");
        network.setMessage("For a better Drury Tour experence please connect to a network");
        network.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int w) {

            }
        });
        AlertDialog networkDialog = network.create();
        networkDialog.show();
    }

    public Boolean checkNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
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
        if (id == R.id.about_drury) {
            Intent drury = new Intent(this, about_drury.class);
            startActivity(drury);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

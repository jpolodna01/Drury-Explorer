package edu.drury.mcs.icarus.druryexplorer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import edu.drury.mcs.icarus.druryexplorer.Building;


public class BuildingFacts extends Activity {

    private TextView textView1;
    private TextView history;
    private ImageView hImage;
    private ImageView rLayout;
    private Boolean net;
    private Bundle extras;

    private String jsonReturn;
    private String url = "http://mcs.drury.edu/duexplorer/DUE_PHP/DUE_Facts_Halls.php"; //url to the php echo'ed data
    private TextView testView; // listview variable
    private int[] pic={R.drawable.pearsons,R.drawable.shewmaker, R.drawable.springfield,R.drawable.tsc,R.drawable.hammons,
            R.drawable.breech,R.drawable.weiser,R.drawable.burnham,
            R.drawable.bay,R.drawable.oreilly,R.drawable.pac, R.drawable.stonechapel,R.drawable.olin,
            R.drawable.lay,R.drawable.mabee,R.drawable.fsc,R.drawable.freeman,
            R.drawable.rose,R.drawable.smith,R.drawable.wallace, R.drawable.sunderland,R.drawable.president,
            R.drawable.parsonage,R.drawable.congregational,R.drawable.summit,R.drawable.suites,
            R.drawable.collegepark,R.drawable.mac,R.drawable.jeff,R.drawable.quad,R.drawable.manley,
            R.drawable.harrison,R.drawable.drury,R.drawable.diversity};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_facts);
        textView1 = (TextView) findViewById(R.id.hNameView);
        history = (TextView) findViewById(R.id.historyView);
        hImage = (ImageView) findViewById(R.id.hImageView);
        rLayout = (ImageView) findViewById(R.id.hBG);
        net = checkNetwork();
        ActionBar bar = getActionBar();

        //creates a string to place the clicked object
        String newName;
        String newHistory;
        String newImage;
        String bgImage;

        getImageFromUrl getIMG = new getImageFromUrl(hImage, rLayout);

        if(savedInstanceState == null)
        {

        String id;


            if (savedInstanceState == null) {

            //get the bundle from Halls.java
            extras = getIntent().getExtras();
            Building hall = (Building) extras.getParcelable("clickedHall");


                //if there is nothing there then the string is empty
                if (extras == null) {
                    newName = null;

                    newHistory=null;
                    newImage=null;
                    id= "0";

                    newHistory = null;
                    newImage = null;
                    id = "0";

                }

                //if there is something there the get the string from the bundle
                else {
                    newName = hall.getBuildingName();
                    newHistory=hall.getBuildingFacts();
                    newImage=hall.getPicture();
                    id=hall.getId();

                    newHistory = hall.getBuildingFacts();
                    newImage = hall.getPicture();
                    id = hall.getId();

                }
            } else {
                Building hall = (Building) savedInstanceState.getSerializable("clickedHall");
                newName = hall.getBuildingName();
                newHistory=hall.getBuildingFacts();
                newImage=hall.getPicture();
                bgImage=hall.getPicture();

                id=hall.getId();

                newHistory = hall.getBuildingFacts();
                newImage = hall.getPicture();
                id = hall.getId();

                }
            //checks to see if the network is up, uses pics from network if it is other wise uses pic from app
            if (checkNetwork()) {
                getIMG.execute(new String[]{newImage});
                AssetManager manager = getAssets();

            }
            else {
                rLayout.setImageResource(pic[Integer.parseInt(id) - 1]);
                hImage.setImageResource(pic[Integer.parseInt(id) - 1]);
            }

           /* else {
                hImage.setImageResource(pic[Integer.parseInt(id) - 1]);

            }*/

            //display the name of the clicked hall
            textView1.setText(newName);
            history.setText(newHistory);
            bar.setTitle(newName + " Facts");


            AssetManager manager = getAssets();

        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();

        //View root = findViewById(R.id.hImageView);
        //View bg = findViewById(R.id.rLayout);
        //setContentView(new View(this));
        //unbindDrawables(bg);
        //unbindDrawables(root);

        //used
      //  getIntent().getExtras().clear();
        //extras.clear();

        //used
      //  System.gc();
    }

/*    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //extras.clear();
        //System.gc();
    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }*/


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

    /**
     *
     *
     */
    private class getImageFromUrl extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bannerImage;
        ImageView bgImage;

        public getImageFromUrl(ImageView hImage, ImageView rLayout)
        {
            this.bannerImage = hImage;
            this.bgImage = rLayout;
        }
        @Override
        protected Bitmap doInBackground(String...url)
        {
            String urldisplay = url[0];
            Bitmap map = null;

            try
            {
                InputStream in = new java.net.URL(urldisplay).openStream();
                map = BitmapFactory.decodeStream(in);
            } catch (Exception e)
            {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }

            return map;
        }

        //sets the bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result)
        {
            bannerImage.setImageBitmap(result);
            bgImage.setImageBitmap(result);

        }


    }
}

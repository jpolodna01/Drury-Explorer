package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import edu.drury.mcs.icarus.druryexplorer.Building;


public class HallFacts extends Activity {

    private TextView textView1;
    private TextView history;
    private ImageView hImage;

    private String jsonReturn;
    private String url = "http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Facts_Halls.php"; //url to the php echo'ed data
    private TextView testView; // listview variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_facts);
        textView1 = (TextView) findViewById(R.id.hNameView);
        history = (TextView) findViewById(R.id.historyView);
        hImage = (ImageView) findViewById(R.id.hImageView);

        //creates a string to place the clicked object
        String newName;
        String newHistory;
        String newImage;

        getImageFromUrl getIMG = new getImageFromUrl(hImage);

        if(savedInstanceState == null)
        {
            //get the bundle from Halls.java
            Bundle extras = getIntent().getExtras();
            Building hall = (Building) extras.getParcelable("clickedHall");


            //if there is nothing there then the string is empty
            if(extras == null)
            {
                newName = null;
                newHistory=null;
                newImage=null;
            }

            //if there is something there the get the string from the bundle
            else
            {
                newName = hall.getBuildingName();
                newHistory=hall.getBuildingFacts();
                newImage=hall.getPicture();
            }
        }
        else
        {
            Building hall = (Building) savedInstanceState.getSerializable("clickedHall");
            newName = hall.getBuildingName();
            newHistory=hall.getBuildingFacts();
            newImage=hall.getPicture();
        }

        getIMG.execute(new String[] {newImage});

        //display the name of the clicked hall
        textView1.setText(newName);
        history.setText(newHistory);


        AssetManager manager = getAssets();

        /*try
        {
            InputStream open = manager.open("test.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(open);

            hImage.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/
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

    private class getImageFromUrl extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;
        public getImageFromUrl(ImageView hImage)
        {
            this.bmImage = hImage;
        }
        @Override
        protected Bitmap doInBackground(String...imgUrl)
        {
            String urldisplay = imgUrl[0];
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
            bmImage.setImageBitmap(result);
        }

    }
}

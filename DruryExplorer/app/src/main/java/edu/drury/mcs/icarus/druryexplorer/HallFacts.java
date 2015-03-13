package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
=======
>>>>>>> e777c8ed0944a8a891ad44887d777b0eb487c5f0
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import edu.drury.mcs.icarus.druryexplorer.Building;

=======
>>>>>>> e777c8ed0944a8a891ad44887d777b0eb487c5f0

public class HallFacts extends Activity {

    private TextView textView1;
    private TextView history;
    private ImageView hImage;

    private String jsonReturn;
    private String url = "http://mcs.drury.edu/jpolodna01/DUE_PHP/DUE_Facts_Halls.php"; //url to the php echo'ed data
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

        //creates a string to place the clicked object
        String newName;
        String newHistory;
<<<<<<< HEAD
        String newImage;

        getImageFromUrl getIMG = new getImageFromUrl(hImage);

=======
        String id;
>>>>>>> e777c8ed0944a8a891ad44887d777b0eb487c5f0
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
<<<<<<< HEAD
                newImage=null;
=======
                id= "0";
>>>>>>> e777c8ed0944a8a891ad44887d777b0eb487c5f0
            }

            //if there is something there the get the string from the bundle
            else
            {
                newName = hall.getBuildingName();
                newHistory=hall.getBuildingFacts();
<<<<<<< HEAD
                newImage=hall.getPicture();
=======
                id=hall.getId();
>>>>>>> e777c8ed0944a8a891ad44887d777b0eb487c5f0
            }
        }
        else
        {
            Building hall = (Building) savedInstanceState.getSerializable("clickedHall");
            newName = hall.getBuildingName();
            newHistory=hall.getBuildingFacts();
<<<<<<< HEAD
            newImage=hall.getPicture();
=======
            id=hall.getId();
>>>>>>> e777c8ed0944a8a891ad44887d777b0eb487c5f0
        }

        getIMG.execute(new String[] {newImage});

        //display the name of the clicked hall
        textView1.setText(newName);
        history.setText(newHistory);
        hImage.setImageResource(pic[Integer.parseInt(id)-1]);

<<<<<<< HEAD

        AssetManager manager = getAssets();

        /*try
=======
        //AssetManager manager = getAssets();

       /* try
>>>>>>> e777c8ed0944a8a891ad44887d777b0eb487c5f0
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

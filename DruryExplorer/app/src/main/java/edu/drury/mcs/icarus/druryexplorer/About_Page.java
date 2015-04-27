package edu.drury.mcs.icarus.druryexplorer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class About_Page extends Activity {

    private ImageView aImage;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__page);


        aImage = (ImageView) findViewById(R.id.aImageView);
        aImage.setImageResource(R.drawable.logo);

        text = (TextView) findViewById(R.id.textView);
        /*text.setText("AppName: Drury Explorer\\n Company Name: Icuras Software\\n\n" +
                "    Team Name: ASCII U\\n Team Members:\\n    Daiv McBride\\n\n" +
                "        Daniel Chick\\n\n" +
                "        Josef Polodna\\n\n" +
                "    Version: 1.1\\n\n" +
                "    Last Update:");*/

    }

    public void close(View view){
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about__page, menu);
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

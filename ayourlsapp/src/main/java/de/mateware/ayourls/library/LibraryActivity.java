package de.mateware.ayourls.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.settings.SettingsActivity;

public class LibraryActivity extends AppCompatActivity {

    private static Logger log = LoggerFactory.getLogger(LibraryActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.blank_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_frame, new LinkLibraryFragment())
                                       .commit();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_link_lib, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
}

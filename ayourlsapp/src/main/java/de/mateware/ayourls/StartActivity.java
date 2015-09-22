package de.mateware.ayourls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
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
            showPreferenceFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPreferenceFragment() {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.content_frame, new SettingsFragment())
                                   .commit();
    }
}

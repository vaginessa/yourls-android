package de.mateware.ayourls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mate on 23.09.2015.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_frame, new SettingsFragment())
                                       .commit();
        }
    }
}

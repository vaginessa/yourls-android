package de.mateware.ayourls;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by mate on 22.09.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }


}

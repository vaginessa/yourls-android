package de.mateware.ayourls;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mate on 22.09.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private static Logger log = LoggerFactory.getLogger(SettingsFragment.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_server_url)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_server_token)));
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        log.info(rootKey);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                                                                                              .getString(preference.getKey(), ""));
    }


    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String valueString = value.toString();
            if (valueString != null) {
                preference.setSummary(valueString);
            }
            return false;
        }
    };


}

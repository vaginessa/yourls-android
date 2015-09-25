package de.mateware.ayourls;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.TwoStatePreference;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.android.volley.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.dialog.Dialog;
import de.mateware.ayourls.yourslapi.Volley;
import de.mateware.ayourls.yourslapi.YourlsRequest;

/**
 * Created by mate on 22.09.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    private static Logger log = LoggerFactory.getLogger(SettingsFragment.class);
    private Preference serverCheckPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.preferences);

        Preference serverUrlPreference = findPreference(getString(R.string.pref_key_server_url));
        Preference serverTokenPreference = findPreference(getString(R.string.pref_key_server_token));
        serverCheckPreference = findPreference(getString(R.string.pref_key_server_check));

        bindPreferenceSummaryToValue(serverUrlPreference);
        bindPreferenceSummaryToValue(serverTokenPreference);
        bindPreferenceSummaryToValue(serverCheckPreference);

        //Check for enabling server check and register for check on change
        checkServerCheckEnabled();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                         .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                             @Override
                             public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                                 if (!TextUtils.isEmpty(key)) {
                                     if (key.equals(getString(R.string.pref_key_server_url)) || key.equals(getString(R.string.pref_key_server_token))) {
                                         checkServerCheckEnabled();
                                     } else if (key.equals(getString(R.string.pref_key_server_check))) {
                                         new Dialog().withMessage("TEST")
                                                     .withTitle("TEST")
                                                     .show(getFragmentManager(), "TEST");
                                     }
                                 }
                             }
                         });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        //Workaround for not working xml prefences
        log.info(preference.toString());

        final String DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";
        if (preference.getKey()
                      .equals(getString(R.string.pref_key_server_url)) || preference.getKey()
                                                                                    .equals(getString(R.string.pref_key_server_token))) {
            DialogFragment f = EditTextPreferenceDialogFragmentCompatSingleLine.newInstance(preference.getKey());
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void checkServerCheckEnabled() {
        String url = PreferenceManager.getDefaultSharedPreferences(getContext())
                                      .getString(getString(R.string.pref_key_server_url), null);
        String token = PreferenceManager.getDefaultSharedPreferences(getContext())
                                        .getString(getString(R.string.pref_key_server_token), null);
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(token) && (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url))) {
            serverCheckPreference.setEnabled(true);
            return;
        }
        serverCheckPreference.setEnabled(false);
    }

    private void checkServer() {
        YourlsRequest request = new YourlsRequest(getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                log.info(response.toString());
            }
        }, new YourlsRequest.ErrorListener() {
            @Override
            public void onErrorResponse(YourlsRequest.Error error) {
                log.info(error.toString());
            }
        });
        Volley.getInstance(getContext())
              .addToRequestQueue(request);
        //        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest()
        //
        //        Volley.getInstance(getContext()).addToRequestQueue();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        log.info(rootKey);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        if (preference != null) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
            // Trigger the listener immediately with the preference's
            // current value.
            if (!(preference instanceof TwoStatePreference)) {
                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                                                                                                      .getString(preference.getKey(), ""));
            }
        }
    }


    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Context context = preference.getContext();
            String valueString = value.toString();

            if (valueString != null) {
                String summaryKey = preference.getKey() + "_summary";
                int summaryId = context.getResources()
                                       .getIdentifier(summaryKey, "string", context.getPackageName());
                if (TextUtils.isEmpty(valueString)) {
                    int summaryEmptyId = context.getResources()
                                                .getIdentifier(summaryKey + "_empty", "string", context.getPackageName());
                    if (summaryEmptyId != 0) summaryId = summaryEmptyId;
                }
                if (summaryId != 0) preference.setSummary(context.getString(summaryId, valueString));
                else preference.setSummary(valueString);
            }
            return true;
        }
    };

    public static class WorkerFragment extends Fragment {

        private static final String TAG_WORKER_FRAGMENT = "task_fragment";
        private WorkerCallback callback;

        public WorkerFragment() {
        }

        public static WorkerFragment findOrCreateFragment(FragmentManager fm, WorkerCallback callback) {
            WorkerFragment fragment = (WorkerFragment) fm.findFragmentByTag(TAG_WORKER_FRAGMENT);
            if (fragment == null) {
                fragment = new WorkerFragment();
                fm.beginTransaction()
                  .add(fragment, TAG_WORKER_FRAGMENT)
                  .commit();
            }
            fragment.callback = callback;
            return fragment;
        }

        public interface WorkerCallback {

        }
    }


}

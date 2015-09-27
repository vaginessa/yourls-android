package de.mateware.ayourls.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.webkit.URLUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.dialog.Dialog;
import de.mateware.ayourls.yourslapi.YourlsRequest;
import de.mateware.ayourls.yourslapi.action.DbStats;
import de.mateware.ayourls.yourslapi.action.YourlsAction;

/**
 * Created by mate on 22.09.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SettingsWorkerFragment.SettingsWorkerCallback {

    private static final String TAG_DIALOG_CHECK_SERVER = "TDCS";
    private static final String TAG_DIALOG_CHECK_SUCCESS = "TDCSS";
    private static final String TAG_DIALOG_CHECK_ERROR = "TDCE";


    private static Logger log = LoggerFactory.getLogger(SettingsFragment.class);

    private static OnPreferenceChangeListenerImpl sPreferenceChangeListenerStandard = new OnPreferenceChangeListenerImpl();


    private CheckBoxPreference serverCheckPreference;
    private SharedPreferences prefs;
    private SettingsWorkerFragment workerFragment;

    private OnPreferenceChangeListenerImpl preferenceChangeListenerForUrlAndToken = new OnPreferenceChangeListenerImpl() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String url = null;
            String token = null;
            if (preference.getKey()
                          .equals(getString(R.string.pref_key_server_url))) {
                url = (String) o;
                token = preference.getSharedPreferences()
                                  .getString(getString(R.string.pref_key_server_token), null);
            } else if (preference.getKey()
                                 .equals(getString(R.string.pref_key_server_token))) {
                url = preference.getSharedPreferences()
                                .getString(getString(R.string.pref_key_server_url), null);
                token = (String) o;
            }
            checkServerCheckEnabled(url, token);
            return super.onPreferenceChange(preference, o);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workerFragment = SettingsWorkerFragment.findOrCreateFragment(getFragmentManager(), this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        addPreferencesFromResource(R.xml.preferences);

        Preference serverUrlPreference = findPreference(getString(R.string.pref_key_server_url));
        Preference serverTokenPreference = findPreference(getString(R.string.pref_key_server_token));
        serverCheckPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_key_server_check));

        bindPreference(serverUrlPreference, preferenceChangeListenerForUrlAndToken);
        bindPreference(serverTokenPreference, preferenceChangeListenerForUrlAndToken);
        bindPreference(serverCheckPreference, new OnPreferenceChangeListenerImpl() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                log.trace(o.toString());
                if ((Boolean) o) {
                    checkServer();
                }
                return super.onPreferenceChange(preference, o);
            }
        });


        //Check for enabling server check and register for check on change

        checkServerCheckEnabled(prefs.getString(getString(R.string.pref_key_server_url), null), prefs.getString(getString(R.string.pref_key_server_token), null));

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

    private void checkServerCheckEnabled(String url, String token) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(token) && (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url))) {
            serverCheckPreference.setEnabled(true);
            return;
        }
        prefs.edit()
             .putBoolean(getString(R.string.pref_key_server_check), false)
             .commit();
        serverCheckPreference.setEnabled(false);
        serverCheckPreference.setChecked(false);
        serverCheckPreference.callChangeListener(false);
    }


    private static void setPreferenceSummaryToValue(Preference preference, Object value) {

        Context context = preference.getContext();

        String valueString = ((value != null) ? value.toString() : "");

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


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        log.info(rootKey);
    }

    private static void bindPreference(Preference preference, Preference.OnPreferenceChangeListener changeListener) {

        if (preference != null) {
            Context context = preference.getContext();
            if (changeListener != null)
                preference.setOnPreferenceChangeListener(changeListener);


            Object value = null;
            try {
                value = PreferenceManager.getDefaultSharedPreferences(context)
                                         .getString(preference.getKey(), null);
            } catch (ClassCastException ignored) {

            }
            setPreferenceSummaryToValue(preference, value);
        }
    }

    private void checkServer() {
        new Dialog().withTitle(R.string.dialog_check_server_message)
                    .withMessage(R.string.dialog_check_server_message)
                    .show(getFragmentManager(), TAG_DIALOG_CHECK_SERVER);
        workerFragment.checkServer();
    }

    @Override
    public void onServerCheckSuccess(YourlsAction yourlsAction) {
        DbStats dbStats = (DbStats) yourlsAction;
        Dialog.dismissDialog(getFragmentManager(), TAG_DIALOG_CHECK_SERVER);
        new Dialog().withTitle(R.string.dialog_check_server_success_title)
                    .withMessage(getString(R.string.dialog_check_server_success_message,dbStats.getTotalLinks(),dbStats.getTotalClicks()))
                    .withPositiveButton()
                    .show(getFragmentManager(), TAG_DIALOG_CHECK_SUCCESS);
    }

    @Override
    public void onServerCheckFail(YourlsRequest.Error error) {
        serverCheckPreference.setChecked(false);
        Dialog.dismissDialog(getFragmentManager(), TAG_DIALOG_CHECK_SERVER);
        new Dialog().withTitle(R.string.dialog_error_title)
                    .withMessage(error.getMessage())
                    .withPositiveButton()
                    .show(getFragmentManager(), TAG_DIALOG_CHECK_ERROR);
    }

    private static class OnPreferenceChangeListenerImpl implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            setPreferenceSummaryToValue(preference, o);
            return true;
        }
    }


}
package de.mateware.ayourls.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.service.ClipboardService;
import de.mateware.ayourls.utils.UrlValidator;
import de.mateware.ayourls.yourslapi.YourlsError;
import de.mateware.ayourls.yourslapi.action.DbStats;
import de.mateware.dialog.Dialog;
import de.mateware.dialog.DialogIndeterminateProgress;

/**
 * Created by mate on 22.09.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SettingsWorkerFragment.SettingsWorkerCallback {

    private static final String TAG_DIALOG_CHECK_SERVER = "TDCS";
    private static final String TAG_DIALOG_CHECK_SUCCESS = "TDCSS";
    private static final String TAG_DIALOG_CHECK_ERROR = "TDCE";

    private static Logger log = LoggerFactory.getLogger(SettingsFragment.class);

    private CheckBoxPreference serverCheckPreference;
    private SharedPreferences prefs;
    private SettingsWorkerFragment workerFragment;

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workerFragment = SettingsWorkerFragment.findOrCreateFragment(getFragmentManager(), this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                log.debug("sharedPref {} changed", key);
                if (getString(R.string.pref_key_server_url).equals(key) || getString(R.string.pref_key_server_token).equals(key)) {
                    if (sharedPreferences.getBoolean(getString(R.string.pref_key_server_check), false)) {
                        sharedPreferences.edit()
                                         .putBoolean(getString(R.string.pref_key_server_check), false)
                                         .commit();
                        serverCheckPreference.setChecked(false);
                        serverCheckPreference.callChangeListener(false);
                    }
                }
                if (getString(R.string.pref_key_server_check).equals(key) || getString(R.string.pref_key_app_clipboard_monitor).equals(key)) {
                    ClipboardService.checkClipboardServiceActivation(getContext());
                    //ClipboardHelper.checkClipboardActivation(getContext());
                }
            }
        };

        addPreferencesFromResource(R.xml.preferences);

        Preference serverUrlPreference = findPreference(getString(R.string.pref_key_server_url));
        Preference serverTokenPreference = findPreference(getString(R.string.pref_key_server_token));
        serverCheckPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_key_server_check));

        OnPreferenceChangeListenerImpl preferenceChangeListenerForUrlAndToken = new OnPreferenceChangeListenerImpl() {
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

        bindPreference(serverUrlPreference, preferenceChangeListenerForUrlAndToken);
        bindPreference(serverTokenPreference, preferenceChangeListenerForUrlAndToken);
        bindPreference(serverCheckPreference, new OnPreferenceChangeListenerImpl() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                log.trace(o.toString());
                if ((Boolean) o) {
                    checkServer();
                } else {
                    enableAppPreferenceCategory((Boolean) o);
                }
                return super.onPreferenceChange(preference, o);
            }
        });


        //Check for enabling server checkClipboardActivation and register for checkClipboardActivation on change

        checkServerCheckEnabled(prefs.getString(getString(R.string.pref_key_server_url), null), prefs.getString(getString(R.string.pref_key_server_token), null));
        enableAppPreferenceCategory(prefs.getBoolean(getString(R.string.pref_key_server_check), false));
    }

    @Override
    public void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        prefs.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        super.onPause();
    }

    private void enableAppPreferenceCategory(boolean value) {
        CheckBoxPreference appClipboardPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_key_app_clipboard_monitor));
        bindPreference(appClipboardPreference, new OnPreferenceChangeListenerImpl());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            appClipboardPreference.setEnabled(value);
        else appClipboardPreference.setEnabled(false);
        CheckBoxPreference replaceClipboardPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_key_app_always_replace_clipboard));
        bindPreference(replaceClipboardPreference, new OnPreferenceChangeListenerImpl());
        CheckBoxPreference appDeleteServerDefaultPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_key_app_delete_server_default));
        bindPreference(appDeleteServerDefaultPreference, new OnPreferenceChangeListenerImpl());
        appDeleteServerDefaultPreference.setEnabled(value);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        //Workaround for not working xml prefences
        log.info(preference.toString());

        final String DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";
        if (preference.getKey()
                      .equals(getString(R.string.pref_key_server_url)) || preference.getKey()
                                                                                    .equals(getString(R.string.pref_key_server_token))) {
            DialogFragment f;
            if (preference.getKey()
                          .equals(getString(R.string.pref_key_server_url))) {
                f = EditTextPreferenceDialogFragmentCompatSingleLine.newInstance(preference.getKey(), EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_URI);
            } else if (preference.getKey()
                                 .equals(getString(R.string.pref_key_server_token))) {
                f = EditTextPreferenceDialogFragmentCompatSingleLine.newInstance(preference.getKey(), EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                f = EditTextPreferenceDialogFragmentCompatSingleLine.newInstance(preference.getKey());
            }
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void checkServerCheckEnabled(String url, String token) {

        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(token)) {
            try {
                UrlValidator.getValidUrl(url, true);
                serverCheckPreference.setEnabled(true);
                return;
            } catch (UrlValidator.NoValidUrlExpception ignored) {

            }

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
            if (changeListener != null) preference.setOnPreferenceChangeListener(changeListener);


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

        new DialogIndeterminateProgress.Builder().setMessage(R.string.dialog_check_server_message)
                                                 .setCancelable(false)
                                                 .build()
                                                 .show(getFragmentManager(), TAG_DIALOG_CHECK_SERVER);
        workerFragment.checkServer();
    }

    @Override
    public void onServerCheckSuccess(DbStats dbStats) {
        Dialog.dismissDialog(getFragmentManager(), TAG_DIALOG_CHECK_SERVER);
        new Dialog.Builder().setTitle(R.string.dialog_check_server_success_title)
                            .setMessage(getString(R.string.dialog_check_server_success_message, dbStats.getTotalLinks(), dbStats.getTotalClicks()))
                            .setPositiveButton()
                            .build()
                            .show(getFragmentManager(), TAG_DIALOG_CHECK_SUCCESS);
        enableAppPreferenceCategory(true);
    }

    @Override
    public void onServerCheckFail(YourlsError error) {
        serverCheckPreference.setChecked(false);
        Dialog.dismissDialog(getFragmentManager(), TAG_DIALOG_CHECK_SERVER);
        new Dialog.Builder().setTitle(R.string.dialog_error_title)
                            .setMessage(error.getMessage())
                            .setPositiveButton()
                            .build()
                            .show(getFragmentManager(), TAG_DIALOG_CHECK_ERROR);
        enableAppPreferenceCategory(false);
    }

    private static class OnPreferenceChangeListenerImpl implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            setPreferenceSummaryToValue(preference, o);
            return true;
        }
    }


}

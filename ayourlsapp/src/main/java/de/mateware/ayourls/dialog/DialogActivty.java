package de.mateware.ayourls.dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.service.DeleteService;
import de.mateware.ayourls.service.ShortUrlService;
import de.mateware.ayourls.settings.SettingsActivity;
import de.mateware.ayourls.utils.UrlValidator;
import de.mateware.dialog.Dialog;
import de.mateware.dialog.listener.DialogButtonListener;
import de.mateware.dialog.listener.DialogCancelListener;
import de.mateware.dialog.listener.DialogDismissListener;

/**
 * Created by mate on 05.10.2015.
 */
public class DialogActivty extends AppCompatActivity implements DialogDismissListener, DialogCancelListener, DialogButtonListener {

    private static Logger log = LoggerFactory.getLogger(DialogActivty.class);

    public static final String EXTRA_DIALOG = "extraDialog";
    public static final String EXTRA_MESSAGE = "extraMessage";
    public static final String EXTRA_TITLE = "extraTitle";

    public static final String DIALOG_CLIPBOARD_CONFIRM = "confirmClipboardDialog";
    public static final String DIALOG_ERROR_SHORTENING = "shorteningErrorDialog";
    public static final String DIALOG_ADD = "addDialog";
    public static final String DIALOG_DELETE_CONFIRM = "confirmDeleteDialog";
    public static final String DIALOG_ERROR = "errorDialog";
    public static final String DIALOG_NOSETUP = "noSetupDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            //            if (getIntent().getAction().equals(Intent.ACTION_SEND)) {
            //
            //                String text_data = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            //                log.debug("received from send: {}",text_data);
            //                // and now you can handle this text here what you want to do.
            //            }

            String dialogType = getIntent().getStringExtra(EXTRA_DIALOG);
            String url = getIntent().getStringExtra(ShortUrlService.EXTRA_URL);
            String title = getIntent().getStringExtra(ShortUrlService.EXTRA_TITLE);
            String keyword = getIntent().getStringExtra(ShortUrlService.EXTRA_KEYWORD);
            String message = getIntent().getStringExtra(EXTRA_MESSAGE);
            if (TextUtils.isEmpty(message)) message = getString(R.string.unknown);

            if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
                try {
                    url = UrlValidator.getValidUrl(getIntent().getStringExtra(Intent.EXTRA_TEXT), false);
                    dialogType = DIALOG_ADD;
                } catch (UrlValidator.NoValidUrlExpception noValidUrlExpception) {
                    message = getString(R.string.dialog_error_no_valid_url);
                    dialogType = DIALOG_ERROR;
                }
            }

            if (!TextUtils.isEmpty(dialogType)) {

                if (DIALOG_CLIPBOARD_CONFIRM.equals(dialogType)) {
                    if (!TextUtils.isEmpty(url)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ShortUrlService.EXTRA_URL, url);
                        new Dialog.Builder().setMessage(getString(R.string.dialog_confirm_shortening_message, url))
                                            .setCancelable(true)
                                            .setTitle(R.string.dialog_confirm_shortening_title)
                                            .setTimer(15000)
                                            .setNegativeButton()
                                            .setStyle(R.style.Dialog)
                                            .setPositiveButton()
                                            .setNeutralButton(R.string.edit)
                                            .addBundle(bundle)
                                            .build()
                                            .show(getSupportFragmentManager(), DIALOG_CLIPBOARD_CONFIRM);
                    }
                } else if (DIALOG_DELETE_CONFIRM.equals(dialogType)) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

                    long id = getIntent().getLongExtra(DeleteService.EXTRA_ID, -1);
                    Bundle bundle = new Bundle();
                    bundle.putLong(DeleteService.EXTRA_ID, id);

                    new DeleteLinkDialog.Builder().setLinkId(id)
                                                  .setCheckBoxChecked(prefs.getBoolean(getString(R.string.pref_key_app_delete_server_default), false))
                                                  .setMessage(message)
                                                  .setTitle(R.string.dialog_confirm_delete_title)
                                                  .setPositiveButton()
                                                  .setStyle(R.style.Dialog)
                                                  .setNegativeButton()
                                                  .addBundle(bundle)
                                                  .build()
                                                  .show(getSupportFragmentManager(), DIALOG_DELETE_CONFIRM);
                } else if (DIALOG_ERROR.equals(dialogType)) {
                    new Dialog.Builder().setTitle(R.string.dialog_error_title)
                                        .setMessage(getString(R.string.dialog_error_message, message))
                                        .setPositiveButton()
                                        .build()
                                        .show(getSupportFragmentManager(), DIALOG_ERROR);
                } else if (DIALOG_ERROR_SHORTENING.equals(dialogType)) {
                    Dialog.Builder builder = new Dialog.Builder().setCancelable(true)
                                                                 .setTitle(R.string.dialog_error_shortening_title)
                                                                 .setStyle(R.style.Dialog)
                                                                 .setMessage(getString(R.string.dialog_error_shortening_message, message))
                                                                 .setNegativeButton();
                    Bundle bundle = new Bundle();
                    bundle.putString(ShortUrlService.EXTRA_URL, url);
                    bundle.putString(ShortUrlService.EXTRA_TITLE, title);
                    bundle.putString(ShortUrlService.EXTRA_KEYWORD, keyword);
                    builder.addBundle(bundle)
                           .setNeutralButton(R.string.edit);
                    if (getIntent().hasExtra(ShortUrlService.EXTRA_URL)) {
                        builder.setPositiveButton(R.string.retry);
                    }
                    builder.build()
                           .show(getSupportFragmentManager(), DIALOG_ERROR_SHORTENING);
                } else if (DIALOG_ADD.equals(dialogType)) {
                    if (PreferenceManager.getDefaultSharedPreferences(this)
                                         .getBoolean(getString(R.string.pref_key_server_check), false)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ShortUrlService.EXTRA_URL, url);
                        bundle.putString(ShortUrlService.EXTRA_TITLE, title);
                        bundle.putString(ShortUrlService.EXTRA_KEYWORD, keyword);
                        new AddLinkDialog.Builder().setPositiveButton(R.string.send)
                                                   .setStyle(R.style.Dialog)
                                                   .setNegativeButton()
                                                   .addBundle(bundle)
                                                   .build()
                                                   .show(getSupportFragmentManager(), DIALOG_ADD);

                    } else {
                        showNoSetupDialog();
                    }
                } else if (DIALOG_NOSETUP.equals(dialogType)) {
                    showNoSetupDialog();
                } else {
                    closeActivity();
                }
            } else {
                closeActivity();
            }
        }
    }

    private void showNoSetupDialog() {
        new Dialog.Builder().setTitle(R.string.dialog_error_nosetup_title)
                            .setMessage(R.string.dialog_error_nosetup_message)
                            .setPositiveButton(R.string.action_settings)
                            .setNegativeButton()
                            .setStyle(R.style.Dialog)
                            .build()
                            .show(getSupportFragmentManager(), DIALOG_NOSETUP);
    }

    @Override
    public void onDialogClick(String tag, Bundle arguments, int which) {
        log.debug(tag, which);

        Intent restartIntent = null; //Keep it null unless you want to restart this activity;

        String url = arguments.getString(ShortUrlService.EXTRA_URL);
        String title = arguments.getString(ShortUrlService.EXTRA_TITLE);
        String keyword = arguments.getString(ShortUrlService.EXTRA_KEYWORD);
        if ((DIALOG_CLIPBOARD_CONFIRM.equals(tag) && which == Dialog.BUTTON_POSITIVE) || (DIALOG_ERROR_SHORTENING.equals(tag) && which == Dialog.BUTTON_POSITIVE) || (DIALOG_ADD.equals(tag) && which == Dialog.BUTTON_POSITIVE)) {
            if (!TextUtils.isEmpty(url)) {
                Intent shortServiceIntent = new Intent(this, ShortUrlService.class);
                shortServiceIntent.putExtra(ShortUrlService.EXTRA_URL, url);
                shortServiceIntent.putExtra(ShortUrlService.EXTRA_TITLE, title);
                shortServiceIntent.putExtra(ShortUrlService.EXTRA_KEYWORD, keyword);
                shortServiceIntent.putExtra(ShortUrlService.EXTRA_CONFIRMED, true);
                startService(shortServiceIntent);
            }
        } else if ((DIALOG_ERROR_SHORTENING.equals(tag) && which == Dialog.BUTTON_NEUTRAL) || (DIALOG_CLIPBOARD_CONFIRM.equals(tag) && which == Dialog.BUTTON_NEUTRAL)) {
            restartIntent = new Intent(this, DialogActivty.class);
            restartIntent.putExtra(DialogActivty.EXTRA_DIALOG, DialogActivty.DIALOG_ADD);
            restartIntent.putExtra(ShortUrlService.EXTRA_TITLE, title);
            restartIntent.putExtra(ShortUrlService.EXTRA_URL, url);
            restartIntent.putExtra(ShortUrlService.EXTRA_KEYWORD, keyword);
        } else if (DIALOG_DELETE_CONFIRM.equals(tag)) {
            if (which == Dialog.BUTTON_POSITIVE) {
                Intent deleteServiceIntent = new Intent(this, DeleteService.class);
                deleteServiceIntent.putExtra(DeleteService.EXTRA_ID, arguments.getLong(DeleteLinkDialog.ARG_LONG_LINKID));
                deleteServiceIntent.putExtra(DeleteService.EXTRA_CONFIRMED, true);
                deleteServiceIntent.putExtra(DeleteService.EXTRA_DELETE_ON_SERVER, arguments.getBoolean(DeleteLinkDialog.ARG_BOOL_DELETEONSERVER, false));
                startService(deleteServiceIntent);
            }
        } else if (DIALOG_NOSETUP.equals(tag) && which == Dialog.BUTTON_POSITIVE) {
            Intent setupIntent = new Intent(this, SettingsActivity.class);
            startActivity(setupIntent);
        }
        closeActivity();
        if (restartIntent != null) startActivity(restartIntent);
    }

    @Override
    public void onDialogCancel(String tag, Bundle arguments) {
        log.debug(tag);
        closeActivity();
    }

    @Override
    public void onDialogDismiss(String tag, Bundle arguments) {
        log.debug(tag);
        closeActivity();
    }

    private void closeActivity() {
        finish();
    }
}

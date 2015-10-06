package de.mateware.ayourls;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.dialog.Dialog;
import de.mateware.ayourls.service.ShortUrlService;

/**
 * Created by mate on 05.10.2015.
 */
public class DialogActivty extends AppCompatActivity implements Dialog.DialogDismissListener, Dialog.DialogCancelListener, Dialog.DialogButtonListener {

    private static Logger log = LoggerFactory.getLogger(DialogActivty.class);

    public static final String EXTRA_DIALOG = "extraDialog";
    public static final String EXTRA_ERROR_MESSAGE = "extraErrorMessage";

    public static final String DIALOG_CONFIRM = "confirmDialog";
    public static final String DIALOG_ERROR_SHORTENING = "shorteningErrorDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (getIntent().hasExtra(EXTRA_DIALOG)) {
                String dialogType = getIntent().getStringExtra(EXTRA_DIALOG);
                if (DIALOG_CONFIRM.equals(dialogType)) {
                    if (getIntent().hasExtra(ShortUrlService.EXTRA_URL)) {
                        String url = getIntent().getStringExtra(ShortUrlService.EXTRA_URL);
                        Bundle bundle = new Bundle();
                        bundle.putString(ShortUrlService.EXTRA_URL, url);
                        new Dialog().withMessage(getString(R.string.dialog_confirm_shortening_message, url))
                                    .withCancelable(true)
                                    .withTitle(R.string.dialog_confirm_shortening_title)
                                    .withNegativeButton()
                                    .withPositiveButton()
                                    .withBundle(bundle)
                                    .show(getSupportFragmentManager(), DIALOG_CONFIRM);
                    }
                } else if (DIALOG_ERROR_SHORTENING.equals(dialogType)) {
                    String errorMessage;
                    if (getIntent().hasExtra(EXTRA_ERROR_MESSAGE)) errorMessage = getIntent().getStringExtra(EXTRA_ERROR_MESSAGE);
                    else errorMessage = getString(R.string.unknown);

                    Dialog dialog = new Dialog().withCancelable(true)
                                                .withTitle(R.string.dialog_error_shortening_title)
                                                .withMessage(getString(R.string.dialog_error_shortening_message, errorMessage))
                                                .withPositiveButton();
                    if (getIntent().hasExtra(ShortUrlService.EXTRA_URL)) {
                        String url = getIntent().getStringExtra(ShortUrlService.EXTRA_URL);
                        Bundle bundle = new Bundle();
                        bundle.putString(ShortUrlService.EXTRA_URL, url);
                        dialog.withNeutralButton(R.string.retry)
                              .withBundle(bundle);
                    }
                    dialog.show(getSupportFragmentManager(), DIALOG_ERROR_SHORTENING);
                }
            }
        }
    }

    @Override
    public void onDialogClick(String tag, Bundle arguments, int which) {
        log.debug(tag, which);
        if ((DIALOG_CONFIRM.equals(tag) && which == Dialog.BUTTON_POSITIVE) || (DIALOG_ERROR_SHORTENING.equals(tag) && which == Dialog.BUTTON_NEUTRAL)) {
            String url = arguments.getString(ShortUrlService.EXTRA_URL);
            Intent shortServiceIntent = new Intent(this, ShortUrlService.class);
            shortServiceIntent.putExtra(ShortUrlService.EXTRA_URL, url);
            shortServiceIntent.putExtra(ShortUrlService.EXTRA_CONFIRMED, true);
            startService(shortServiceIntent);
        }
        closeActivity();
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

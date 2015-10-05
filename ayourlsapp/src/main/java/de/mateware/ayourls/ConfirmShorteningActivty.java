package de.mateware.ayourls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.dialog.Dialog;
import de.mateware.ayourls.service.ShortUrlService;

/**
 * Created by mate on 05.10.2015.
 */
public class ConfirmShorteningActivty extends AppCompatActivity implements Dialog.DialogDismissListener, Dialog.DialogCancelListener, Dialog.DialogButtonListener {

    private static Logger log = LoggerFactory.getLogger(ConfirmShorteningActivty.class);

    private static String DIALOG_CONFIRM = "confirmDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (getIntent().hasExtra(ShortUrlService.EXTRA_URL)) {
                String url = getIntent().getStringExtra(ShortUrlService.EXTRA_URL);
                new Dialog().withMessage(getString(R.string.dialog_confirm_shortening_message, url))
                            .withCancelable(true)
                            .withTitle(R.string.dialog_confirm_shortening_title)
                            .withNegativeButton()
                            .withPositiveButton()
                            .show(getSupportFragmentManager(), DIALOG_CONFIRM);
            }
        }
    }

    @Override
    public void onDialogClick(String tag, Bundle arguments, int which) {
        log.debug(tag, which);
        if (DIALOG_CONFIRM.equals(tag)){
            if (which == Dialog.BUTTON_POSITIVE){

            }
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
        overridePendingTransition(0, 0);
    }
}

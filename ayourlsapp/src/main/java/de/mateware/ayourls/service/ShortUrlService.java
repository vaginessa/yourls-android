package de.mateware.ayourls.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.ConfirmShorteningActivty;

/**
 * Created by mate on 01.10.2015.
 */
public class ShortUrlService extends IntentService {

    Logger log = LoggerFactory.getLogger(ShortUrlService.class);

    public static final String EXTRA_URL = "urlExtra";
    public static final String EXTRA_CONFIRMED = "confirmExtra";

    public ShortUrlService() {
        super(ShortUrlService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        log.debug(intent.toString());
        if (intent.hasExtra(EXTRA_URL)) {
            String url = intent.getStringExtra(EXTRA_URL);
            log.debug(url);
            if (!TextUtils.isEmpty(url)) {
                UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"},UrlValidator.ALLOW_2_SLASHES);
                if (urlValidator.isValid(url)) {
                    if (intent.hasExtra(EXTRA_CONFIRMED) && intent.getBooleanExtra(EXTRA_CONFIRMED, false)) {
                        log.debug("start url shortening");
                    } else {
                        Intent confirmIntent = new Intent(this, ConfirmShorteningActivty.class);
                        confirmIntent.putExtra(EXTRA_URL, url);
                        confirmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(confirmIntent);
                    }
                } else if (!url.startsWith("http")) {
                    url = "http://" + url;
                    Intent retryIntent = new Intent(this, ShortUrlService.class);
                    retryIntent.putExtra(EXTRA_URL, url);
                    startService(retryIntent);
                }
            } else {
                throw new IllegalArgumentException("Service have to be called with extra '" + EXTRA_URL + "'");
            }
        } else {
            throw new IllegalArgumentException("Service have to be called with extra '" + EXTRA_URL + "'");
        }

    }
}

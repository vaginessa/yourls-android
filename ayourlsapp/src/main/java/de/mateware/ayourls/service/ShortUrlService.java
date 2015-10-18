package de.mateware.ayourls.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.mateware.ayourls.dialog.DialogActivty;
import de.mateware.ayourls.network.NetworkHelper;
import de.mateware.ayourls.R;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.yourslapi.Volley;
import de.mateware.ayourls.yourslapi.YourlsRequest;
import de.mateware.ayourls.yourslapi.action.ShortUrl;
import de.mateware.ayourls.yourslapi.action.YourlsAction;

/**
 * Created by mate on 01.10.2015.
 */
public class ShortUrlService extends IntentService {

    Logger log = LoggerFactory.getLogger(ShortUrlService.class);

    public static final String EXTRA_URL = "urlExtra";
    public static final String EXTRA_TITLE = "titleExtra";
    public static final String EXTRA_KEYWORD = "keywordExtra";
    public static final String EXTRA_CONFIRMED = "confirmExtra";

    public ShortUrlService() {
        super(ShortUrlService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        log.debug(intent.toString());
        if (intent.hasExtra(EXTRA_URL)) {
            String url = intent.getStringExtra(EXTRA_URL);
            String title = intent.getStringExtra(EXTRA_TITLE);
            String keyword = intent.getStringExtra(EXTRA_KEYWORD);
            log.debug(url);
            if (!TextUtils.isEmpty(url)) {
                UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"}, UrlValidator.ALLOW_2_SLASHES);
                if (urlValidator.isValid(url)) {
                    if (intent.hasExtra(EXTRA_CONFIRMED) && intent.getBooleanExtra(EXTRA_CONFIRMED, false)) {
                        log.debug("start url shortening");
                        RequestFuture<ShortUrl> future = RequestFuture.newFuture();
                        try {
                            if (!NetworkHelper.isConnected(this))
                                throw new VolleyError(getString(R.string.dialog_error_no_connection_message));
                            try {
                                ShortUrl shortUrl = new ShortUrl(url);
                                if (!TextUtils.isEmpty(title))
                                    shortUrl.setTitle(title);
                                if (!TextUtils.isEmpty(keyword))
                                    shortUrl.setKeyword(keyword);
                                YourlsRequest<ShortUrl> request = new YourlsRequest<>(this, new ShortUrl(url), future, future);
                                Volley.getInstance(this)
                                      .addToRequestQueue(request);
                                ShortUrl action = future.get(20, TimeUnit.SECONDS);

                                Link link = new Link();
                                link.load(action);
                                link.save(this);
                                if (action.getStatus() != YourlsAction.STATUS_SUCCESS) {
                                    throw new VolleyError(action.getMessage());
                                }
                            } catch (InterruptedException | ExecutionException | TimeoutException | UnsupportedEncodingException e) {
                                throw new VolleyError(e);
                            }
                        } catch (VolleyError e) {
                            Intent errorIntent = new Intent(this, DialogActivty.class);
                            errorIntent.putExtra(DialogActivty.EXTRA_DIALOG,DialogActivty.DIALOG_ERROR_SHORTENING);
                            errorIntent.putExtra(DialogActivty.EXTRA_ERROR_MESSAGE,e.getMessage() != null ? e.getMessage():e.getCause().getClass().getSimpleName());
                            errorIntent.putExtra(EXTRA_URL, url);
                            errorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(errorIntent);
                        }
                    } else {
                        Intent confirmIntent = new Intent(this, DialogActivty.class);
                        confirmIntent.putExtra(DialogActivty.EXTRA_DIALOG,DialogActivty.DIALOG_CONFIRM);
                        confirmIntent.putExtra(EXTRA_URL, url);
                        confirmIntent.putExtra(EXTRA_TITLE, title);
                        confirmIntent.putExtra(EXTRA_KEYWORD, keyword);
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

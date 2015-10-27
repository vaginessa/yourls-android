package de.mateware.ayourls.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.google.zxing.WriterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.mateware.ayourls.R;
import de.mateware.ayourls.clipboard.NotificationClipboardReceiver;
import de.mateware.ayourls.dialog.DialogActivty;
import de.mateware.ayourls.linkdetail.LinkDetailActivity;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.network.NetworkHelper;
import de.mateware.ayourls.utils.QrCodeHelper;
import de.mateware.ayourls.utils.UrlValidator;
import de.mateware.ayourls.yourslapi.Volley;
import de.mateware.ayourls.yourslapi.YourlsRequest;
import de.mateware.ayourls.yourslapi.action.ShortUrl;
import de.mateware.ayourls.yourslapi.action.YourlsAction;

/**
 * Created by mate on 01.10.2015.
 */
public class ShortUrlService extends IntentService {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlService.class);

    public static final String EXTRA_URL = "urlExtra";
    public static final String EXTRA_TITLE = "titleExtra";
    public static final String EXTRA_KEYWORD = "keywordExtra";
    public static final String EXTRA_CONFIRMED = "confirmExtra";

    public ShortUrlService() {
        super(ShortUrlService.class.getSimpleName());
    }

    public static final int NOTIFICATION_ID = 10002;

    @Override
    protected void onHandleIntent(Intent intent) {

        log.debug(intent.toString());
        if (intent.hasExtra(EXTRA_URL)) {
            String url = intent.getStringExtra(EXTRA_URL);
            String title = intent.getStringExtra(EXTRA_TITLE);
            String keyword = intent.getStringExtra(EXTRA_KEYWORD);
            boolean confirmed = intent.getBooleanExtra(EXTRA_CONFIRMED, false);
            log.debug(url);
            if (!TextUtils.isEmpty(url)) {
                try {
                    url = UrlValidator.getValidUrl(url, true);
                    if (confirmed) {
                        log.debug("start url shortening");

                        try {
                            if (!NetworkHelper.isConnected(this))
                                throw new VolleyError(getString(R.string.dialog_error_no_connection_message));
                            try {
                                ShortUrl shortUrl = new ShortUrl(url);
                                if (!TextUtils.isEmpty(title)) shortUrl.setTitle(title);
                                if (!TextUtils.isEmpty(keyword)) shortUrl.setKeyword(keyword);
                                RequestFuture<ShortUrl> future = RequestFuture.newFuture();
                                YourlsRequest<ShortUrl> request = new YourlsRequest<>(this, shortUrl, future, future);
                                Volley.getInstance(this)
                                      .addToRequestQueue(request);
                                ShortUrl action = future.get(20, TimeUnit.SECONDS);
                                if (action.getStatus() != YourlsAction.STATUS_SUCCESS) {
                                    throw new VolleyError(action.getMessage());
                                } else {
                                    Link link = new Link();
                                    link.load(action);
                                    try {
                                        QrCodeHelper.getInstance(this)
                                                    .generateQr(link.getShorturl());
                                    } catch (IOException | WriterException e) {
                                        log.error("Error generating QrCode", e);
                                    }
                                    switch (link.save(this)) {
                                        case ERROR:
                                            QrCodeHelper.getInstance(this)
                                                        .deleteQrFile(link.getShorturl());
                                            break;
                                        default:
                                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                            shareIntent.putExtra(Intent.EXTRA_TEXT, link.getShorturl());
                                            shareIntent.setType("text/plain");
                                            PendingIntent pendingShareIntent = PendingIntent.getActivity(this, 0, Intent.createChooser(shareIntent, getString(R.string.action_share)), PendingIntent.FLAG_UPDATE_CURRENT);

                                            Intent copyIntent = new Intent(NotificationClipboardReceiver.ACTION_COPY);
                                            copyIntent.putExtra(NotificationClipboardReceiver.ARG_TEXT, link.getShorturl());
                                            PendingIntent pendingCopyIntent = PendingIntent.getBroadcast(this, 0, copyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            Intent openIntent = new Intent(this, LinkDetailActivity.class);
                                            openIntent.putExtra(LinkDetailActivity.EXTRA_LINK_ID, link.getId());
                                            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContentTitle(link.getTitle())
                                                                                                                     .setContentText(link.getShorturl())
                                                                                                                     .setStyle(new NotificationCompat.BigTextStyle().bigText(link.getShorturl() + "\n\n" + link.getUrl()))
                                                                                                                     .setSmallIcon(R.drawable.ic_link_24dp)
                                                                                                                     .setColor(ContextCompat.getColor(this, R.color.primary))
                                                                                                                     .setContentIntent(contentIntent)
                                                                                                                     .setAutoCancel(true)
                                                                                                                     .addAction(R.drawable.ic_share_24dp, getString(R.string.action_share), pendingShareIntent)
                                                                                                                     .addAction(R.drawable.ic_content_copy_24dp, getString(R.string.action_copy), pendingCopyIntent);

                                            try {
                                                int largeIconSize = getResources().getDimensionPixelSize(android.support.v7.appcompat.R.dimen.notification_large_icon_height);
                                                Bitmap largeIcon = QrCodeHelper.getInstance(this)
                                                                               .getQrBitmapFromFile(link.getShorturl(), largeIconSize);
                                                builder.setLargeIcon(largeIcon);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();

                                            try {
                                                int bigPictureSize = 400;
                                                Bitmap bigPicture = QrCodeHelper.getInstance(this)
                                                                                .generateQrBitmap(link.getShorturl(), bigPictureSize, 3, ContextCompat.getColor(this, android.R.color.black), ContextCompat.getColor(this, android.R.color.white));

                                                Notification pageTwo = new NotificationCompat.WearableExtender().setBackground(bigPicture)
                                                                                                                .setHintShowBackgroundOnly(true)
                                                                                                                .setHintAvoidBackgroundClipping(true)
                                                                                                                .setHintScreenTimeout(60000)
                                                                                                                .extend(new NotificationCompat.Builder(this))
                                                                                                                .build();
                                                wearableExtender.addPage(pageTwo);
                                            } catch (WriterException e) {
                                                log.error("error:", e);
                                            }

                                            builder.extend(wearableExtender);

                                            Notification notification = builder.build();

                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                                            notificationManager.notify(link.getShorturl(), NOTIFICATION_ID, notification);
                                            break;
                                    }
                                }
                            } catch (InterruptedException | ExecutionException | TimeoutException | UnsupportedEncodingException e) {
                                throw new VolleyError(e);
                            }
                        } catch (VolleyError e) {
                            Intent errorIntent = new Intent(this, DialogActivty.class);
                            errorIntent.putExtra(DialogActivty.EXTRA_DIALOG, DialogActivty.DIALOG_ERROR_SHORTENING);
                            errorIntent.putExtra(DialogActivty.EXTRA_MESSAGE, e.getMessage() != null ? e.getMessage() : e.getCause()
                                                                                                                         .getClass()
                                                                                                                         .getSimpleName());
                            errorIntent.putExtra(EXTRA_URL, url);
                            errorIntent.putExtra(EXTRA_TITLE, title);
                            errorIntent.putExtra(EXTRA_KEYWORD, keyword);
                            errorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(errorIntent);
                        }
                    } else {
                        Intent confirmIntent = new Intent(this, DialogActivty.class);
                        confirmIntent.putExtra(DialogActivty.EXTRA_DIALOG, DialogActivty.DIALOG_CLIPBOARD_CONFIRM);
                        confirmIntent.putExtra(EXTRA_URL, url);
                        confirmIntent.putExtra(EXTRA_TITLE, title);
                        confirmIntent.putExtra(EXTRA_KEYWORD, keyword);
                        confirmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(confirmIntent);
                    }
                } catch (UrlValidator.NoValidUrlExpception noValidUrlExpception) {
                    log.warn(noValidUrlExpception.getMessage());
                }
                return;
            }
        }
        throw new IllegalArgumentException("Service have to be called with extra '" + EXTRA_URL + "'");
    }
}

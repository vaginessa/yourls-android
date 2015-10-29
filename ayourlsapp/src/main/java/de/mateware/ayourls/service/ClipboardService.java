package de.mateware.ayourls.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.clipboard.ClipboardHelper;
import de.mateware.ayourls.library.LibraryActivity;

/**
 * Created by mate on 20.10.2015.
 */
public class ClipboardService extends Service {

    private static final Logger log = LoggerFactory.getLogger(ClipboardService.class);

    private ClipboardHelper clipboardHelper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.debug("{} {} {}", intent, flags, startId);
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onCreate() {
        log.debug("creating clipboard service ...");
        clipboardHelper = ClipboardHelper.getInstance(this);
        clipboardHelper.registerClipBoardListener();

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LibraryActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this).setContentTitle(getString(R.string.app_name))
                                                                        .setContentText(getString(R.string.service_clipboard_notification_text))
                                                                        .setSmallIcon(R.drawable.ic_link_24dp)
                                                                        .setContentIntent(contentIntent)
                                                                        .setColor(ContextCompat.getColor(this, R.color.primary))
                                                                        .setPriority(NotificationCompat.PRIORITY_MIN)
                                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.service_clipboard_notification_text_long, getString(R.string.service_clipboard_notification_text))))
                                                                        .setShowWhen(false)
                                                                        .build();


        startForeground(1337, notification);
    }

    @Override
    public void onDestroy() {
        log.debug("destroying clipboard service ...");
        clipboardHelper.unregisterClipboardListener();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void checkClipboardServiceActivation(Context context) {
        context = context.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean serverCheck = prefs.getBoolean(context.getString(R.string.pref_key_server_check), false);
        boolean clipboardMonitor = prefs.getBoolean(context.getString(R.string.pref_key_app_clipboard_monitor), false);

        Intent intent = new Intent(context, ClipboardService.class);
        if (clipboardMonitor && serverCheck) {
            context.startService(intent);
        } else {
            context.stopService(intent);
        }
    }
}

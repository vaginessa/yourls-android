package de.mateware.ayourls.clipboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mate on 09.11.2015.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private static Logger log = LoggerFactory.getLogger(BootCompleteReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        log.debug("Boot completed");
        // Just to receive BootCompleted Broadcast. Init is done in application class.
    }
}

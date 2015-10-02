package de.mateware.ayourls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mate on 02.10.2015.
 */
public class ClipboardChangeReceiver extends BroadcastReceiver {

    public static final String ACTION = "de.mateware.ayourls.action.CLIP_CHANGE";

    Logger log = LoggerFactory.getLogger(ClipboardChangeReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        log.debug("Recevied intent");
        Toast.makeText(context, ClipboardHelper.getInstance(context)
                                               .getClipContent(), Toast.LENGTH_LONG).show();
    }
}

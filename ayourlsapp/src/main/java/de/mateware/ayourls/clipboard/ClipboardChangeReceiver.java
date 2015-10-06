package de.mateware.ayourls.clipboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.service.ShortUrlService;

/**
 * Created by mate on 02.10.2015.
 */
public class ClipboardChangeReceiver extends BroadcastReceiver {

    public static final String ACTION = "de.mateware.ayourls.action.CLIP_CHANGE";

    private static Logger log = LoggerFactory.getLogger(ClipboardChangeReceiver.class);

    private static String lastClipboardContent;

    @Override
    public void onReceive(Context context, Intent intent) {
        log.debug("Recevied intent:"+intent.toString());
        String clipboardContent = ClipboardHelper.getInstance(context)
                       .getClipContent().toString();
        if (!TextUtils.isEmpty(clipboardContent)) {
            if (!clipboardContent.equals(lastClipboardContent)) {
                lastClipboardContent = clipboardContent;
                Intent serviceIntent = new Intent(context, ShortUrlService.class);
                serviceIntent.putExtra(ShortUrlService.EXTRA_URL, clipboardContent);
                context.startService(serviceIntent);
            }
        }
    }
}

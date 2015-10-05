package de.mateware.ayourls;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mate on 02.10.2015.
 */
public class ClipboardHelper {

    private static Logger log = LoggerFactory.getLogger(ClipboardHelper.class);

    private static ClipboardHelper instance;

    public static ClipboardHelper getInstance(Context context) {
        if (instance == null) instance = new ClipboardHelper(context.getApplicationContext());
        return instance;
    }

    private ClipboardManager clipboardManager;
    private android.text.ClipboardManager clipboardManagerCompat;

    private ClipboardManager.OnPrimaryClipChangedListener listener;

    private ClipboardHelper(final Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            listener = new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    log.debug("clipboard content changed, listener:"+this.toString());
                    Intent intent = new Intent(ClipboardChangeReceiver.ACTION);
                    context.sendBroadcast(intent);
                }
            };

        } else {
            clipboardManagerCompat = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }
    }

    public void registerClipBoardListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (clipboardManager != null) {
                clipboardManager.addPrimaryClipChangedListener(listener);
            }
        } else {
            //TODO find solution for pre Honeycomb
        }
    }

    public void unregisterClipboardListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (clipboardManager != null) {
                clipboardManager.removePrimaryClipChangedListener(listener);
            }
        } else {
            //TODO find solution for pre Honeycomb
        }
    }


    public CharSequence getClipContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (clipboardManager != null) {
                if (clipboardManager.hasPrimaryClip()) {
                    ClipData clip = clipboardManager.getPrimaryClip();
                    ClipData.Item item = clip.getItemAt(0);
                    return item.getText();
                }
            }
        } else {
            if (clipboardManagerCompat != null) {
                if (clipboardManagerCompat.hasText()) {
                    return clipboardManagerCompat.getText();
                }
            }
        }
        return null;
    }


}

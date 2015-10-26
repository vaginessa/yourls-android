package de.mateware.ayourls.clipboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import de.mateware.ayourls.R;

/**
 * Created by Mate on 26.10.2015.
 */
public class NotificationClipboardReceiver extends BroadcastReceiver {

    public static final String ACTION_COPY = "de.mateware.ayourls.action.ACTION_COPY";
    public static final String ARG_TEXT = "argText";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_COPY.equals(intent.getAction())) {
            String text = intent.getStringExtra(ARG_TEXT);
            if (!TextUtils.isEmpty(text)) {
                ClipboardHelper clipboardHelper = ClipboardHelper.getInstance(context);
                if (clipboardHelper.setClipContent(text)) {
                    Toast.makeText(context, context.getString(R.string.toast_added_to_clipboard, text), Toast.LENGTH_LONG)
                         .show();
                }
            }
        }
    }

}

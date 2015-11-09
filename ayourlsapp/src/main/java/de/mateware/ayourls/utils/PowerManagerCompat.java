package de.mateware.ayourls.utils;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

/**
 * Created by mate on 09.11.2015.
 */
public class PowerManagerCompat {
    public static boolean isInteractive(Context context) {
        //Only handle
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else {
            // If you use less than API20:
            return powerManager.isScreenOn();
        }
    }
}

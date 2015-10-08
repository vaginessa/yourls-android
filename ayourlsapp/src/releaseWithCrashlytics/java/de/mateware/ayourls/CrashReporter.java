package de.mateware.ayourls;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mate on 08.10.2015.
 */
public class CrashReporter {
    public static void init (Context context) {
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(context, new Crashlytics.Builder().core(core)
                                                   .build());
    }
}

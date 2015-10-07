package de.mateware.ayourls;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import de.mateware.ayourls.clipboard.ClipboardHelper;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mate on 30.09.2015.
 */
public class AyourlsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core)
                                                   .build());
        ClipboardHelper.checkClipboardActivation(this);
        //new Link();
    }
}

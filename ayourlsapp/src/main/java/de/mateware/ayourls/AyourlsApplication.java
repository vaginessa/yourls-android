package de.mateware.ayourls;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;



import de.mateware.ayourls.service.ClipboardService;


/**
 * Created by mate on 30.09.2015.
 */
public class AyourlsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                                                                        .penaltyLog()
                                                                        .build());
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
                                                                                .penaltyLog()
                                                                                .penaltyDeathOnNetwork()
                                                                                .build());
            }
        }

        CrashReporter.init(this);

        ClipboardService.checkClipboardServiceActivation(this);
    }
}

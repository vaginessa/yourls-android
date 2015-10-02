package de.mateware.ayourls;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by mate on 30.09.2015.
 */
public class AyourlsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        ClipboardHelper.getInstance(this).registerClipBoardListener();
        ClipboardHelper.getInstance(this).unregisterClipboardListener();
    }
}

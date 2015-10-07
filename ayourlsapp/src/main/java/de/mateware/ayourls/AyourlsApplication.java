package de.mateware.ayourls;

import android.app.Application;

import de.mateware.ayourls.clipboard.ClipboardHelper;
import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 30.09.2015.
 */
public class AyourlsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ClipboardHelper.checkClipboardActivation(this);
        new Link();
    }
}

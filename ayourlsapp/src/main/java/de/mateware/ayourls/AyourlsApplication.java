package de.mateware.ayourls;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import de.mateware.ayourls.clipboard.ClipboardHelper;


/**
 * Created by mate on 30.09.2015.
 */
public class AyourlsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CrashReporter.init(this);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(BuildConfig.DEBUG);
        built.setLoggingEnabled(BuildConfig.DEBUG);
        Picasso.setSingletonInstance(built);

        ClipboardHelper.checkClipboardActivation(this);
    }
}

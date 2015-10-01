package de.mateware.ayourls.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mate on 01.10.2015.
 */
public class ClipboardService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

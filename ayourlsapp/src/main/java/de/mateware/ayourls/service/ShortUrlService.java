package de.mateware.ayourls.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by mate on 01.10.2015.
 */
public class ShortUrlService extends IntentService {


    public ShortUrlService() {
        super(ShortUrlService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

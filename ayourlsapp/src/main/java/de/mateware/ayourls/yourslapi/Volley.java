package de.mateware.ayourls.yourslapi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class Volley {

    private static Volley mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private Volley(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Volley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Volley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}

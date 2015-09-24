package de.mateware.ayourls.yourslapi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by mate on 24.09.2015.
 */
public class YourlsRequest extends JsonObjectRequest {

    Logger log = LoggerFactory.getLogger(YourlsRequest.class);

    public YourlsRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        log.info("Params :)");

        return super.getParams();
    }
}

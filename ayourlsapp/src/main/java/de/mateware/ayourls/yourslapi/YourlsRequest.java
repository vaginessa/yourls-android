package de.mateware.ayourls.yourslapi;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;


import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.ayourls.R;
import de.mateware.ayourls.yourslapi.action.YourlsAction;

/**
 * Created by mate on 24.09.2015.
 */
public class YourlsRequest extends Request<JSONObject> {

    private static final String API_URL_PART = "/yourls-api.php";

    public static final String PARAM_FORMAT = "format";
    public static final String PARAM_SIGNATURE = "signature";
    public static final String PARAM_TIMESTAMP = "timestamp";


    Logger log = LoggerFactory.getLogger(YourlsRequest.class);
    private Response.Listener<YourlsAction> listener;
    //    private Response.ErrorListener errorListener;
    Map<String, String> params = new HashMap<>();
    private YourlsAction action;


    public YourlsRequest(Context context, YourlsAction action, Response.Listener<YourlsAction> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, getApiUrl(context), errorListener);
        this.listener = responseListener;
        //this.errorListener = errorListener;

        setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        String signature = PreferenceManager.getDefaultSharedPreferences(context)
                                            .getString(context.getString(R.string.pref_key_server_token), null);
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            String unixTime = String.valueOf(System.currentTimeMillis() / 1000L);
            String data = unixTime + signature;
            signature = convertByteArrayToHexString(digest.digest(data.getBytes()));
            params.put(PARAM_TIMESTAMP, unixTime);
        } catch (NoSuchAlgorithmException e) {

        }
        params.put(PARAM_SIGNATURE, signature);
        params.put(PARAM_FORMAT, "json");
        params.putAll(action.getParams());
        this.action = action;
    }

    private static String getApiUrl(Context context) {
        String serverUrl = PreferenceManager.getDefaultSharedPreferences(context)
                                            .getString(context.getString(R.string.pref_key_server_url), null);

        if (serverUrl == null) throw new IllegalStateException("Cannot make request without server url");

        while (serverUrl.endsWith("/")) serverUrl = serverUrl.substring(0, serverUrl.length() - 1);

        serverUrl += API_URL_PART;

        return serverUrl;
    }

    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        try {
            action.setResult(response);
            listener.onResponse(action);
        } catch (JSONException e) {
            deliverError(new VolleyError(e));
        }

    }


    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte arrayByte : arrayBytes) {
            stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16)
                                       .substring(1));
        }
        return stringBuffer.toString();
    }
}

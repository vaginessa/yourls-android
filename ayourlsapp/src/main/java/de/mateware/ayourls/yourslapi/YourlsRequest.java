package de.mateware.ayourls.yourslapi;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;

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
    public static final String PARAM_ACTION = "action";

    Logger log = LoggerFactory.getLogger(YourlsRequest.class);
    private Response.Listener<YourlsAction> listener;
    private ErrorListener errorListener;
    Map<String, String> params = new HashMap<>();
    private  YourlsAction action;

    public YourlsRequest(Context context, YourlsAction action, Response.Listener<YourlsAction> responseListener, ErrorListener errorListener) {
        super(Method.POST, getApiUrl(context), null);
        this.listener = responseListener;
        this.errorListener = errorListener;

        params.put(PARAM_SIGNATURE, PreferenceManager.getDefaultSharedPreferences(context)
                                                     .getString(context.getString(R.string.pref_key_server_token), null));
        params.put(PARAM_FORMAT, "json");
        params.putAll(action.getParams());
        this.action = action;
    }

    private static String getApiUrl(Context context) {
        String serverUrl = PreferenceManager.getDefaultSharedPreferences(context)
                                            .getString(context.getString(R.string.pref_key_server_url), null);

        if (serverUrl == null)
            throw new IllegalStateException("Cannot make request without server url");

        while (serverUrl.endsWith("/"))
            serverUrl = serverUrl.substring(0, serverUrl.length() - 1);

        serverUrl += API_URL_PART;

        return serverUrl;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        try {
            action.setResult(response);
            listener.onResponse(action);
        } catch (JSONException e) {
            deliverError(new VolleyError(e));
        }

    }

    @Override
    public void deliverError(VolleyError error) {
        if (errorListener != null) {
            errorListener.onErrorResponse(new Error(error));
        }
    }

    public interface ErrorListener {
        void onErrorResponse(Error error);
    }


    public class Error {

        private static final String JSON_MESSAGE = "message";
        private static final String JSON_ERROR_CODE = "errorCode";

        private VolleyError error;
        private String message;
        private int errorCode = -1;

        public Error(VolleyError error) {
            this.error = error;
            if (error.networkResponse != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));
                    message = jsonObject.getString(JSON_MESSAGE);
                    errorCode = jsonObject.getInt(JSON_ERROR_CODE);
                } catch (JSONException e) {
                    log.warn("Error parsing json, catch HttpStatus");
                    HttpStatus httpStatus = HttpStatus.getByCode(error.networkResponse.statusCode);
                    message = httpStatus.getName() + ": " + httpStatus.getDescription();
                    errorCode = httpStatus.getCode();
                }
            } else {
                message = error.getCause()
                               .getMessage();
            }
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }
    }
}

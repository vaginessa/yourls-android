package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mate on 26.09.2015.
 */
public abstract class YourlsAction  {

    public static String CHARSET = "UTF-8";

    public static int STATUS_UNKNOWN = 0;
    public static int STATUS_FAIL = 1;
    public static int STATUS_SUCCESS = 2;

    public static final String PARAM_ACTION = "action";
    public static final String PARAM_URL = "url";
    public static final String PARAM_KEYWORD = "keyword";
    public static final String PARAM_TITLE = "title";

    Map<String, String> params = new HashMap<>();
    private int status = STATUS_UNKNOWN;
    private String message;

    public YourlsAction(String actionName) {
        addParam(PARAM_ACTION, actionName);
    }

    public void setResult(JSONObject result) throws JSONException {
        if (result.has("status")) {
            String statusString = result.getString("status");
            if ("success".equalsIgnoreCase(statusString))
                status = STATUS_SUCCESS;
            else if ("fail".equalsIgnoreCase(statusString))
                status = STATUS_FAIL;
        }
        if (result.has("message"))
            message = result.getString("message");


        performResultData(result);
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public abstract void performResultData(JSONObject data) throws JSONException;

}

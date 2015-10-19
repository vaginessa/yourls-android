package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mate on 26.09.2015.
 */
public abstract class YourlsAction {

    private static final Logger log = LoggerFactory.getLogger(YourlsAction.class);

    public static String CHARSET = "UTF-8";

    public static int STATUS_UNKNOWN = 0;
    public static int STATUS_FAIL = 1;
    public static int STATUS_SUCCESS = 2;

    public static final String PARAM_ACTION = "action";
    public static final String PARAM_URL = "url";
    public static final String PARAM_KEYWORD = "keyword";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_SHORTURL = "shorturl";

    Map<String, String> params = new HashMap<>();
    private int status = STATUS_UNKNOWN;
    private String message;
    private String code;

    public YourlsAction(String actionName) {
        addParam(PARAM_ACTION, actionName);
    }

    public void setResult(JSONObject result) throws JSONException {
        if (result.has("status")) {
            String statusString = result.getString("status");
            if ("success".equalsIgnoreCase(statusString)) status = STATUS_SUCCESS;
            else if ("fail".equalsIgnoreCase(statusString)) status = STATUS_FAIL;
        }
        if (result.has("message")) message = result.getString("message");
        if (result.has("code")) code = result.getString("code");

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

    public String getJsonString(JSONObject jsonObject, String name) {
        if (jsonObject.has(name)) {
            try {
                return jsonObject.getString(name);
            } catch (JSONException e) {
                log.warn("Problem with json: ",e);
            }
        }
        return null;
    }

    public Long getJsonLong(JSONObject jsonObject, String name) {
        if (jsonObject.has(name)) {
            try {
                return jsonObject.getLong(name);
            } catch (JSONException e) {
                log.warn("Problem with json: ",e);
            }
        }
        return null;
    }

}

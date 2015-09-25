package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.mateware.ayourls.yourslapi.YourlsRequest;

/**
 * Created by Mate on 26.09.2015.
 */
public abstract class YourlsAction  {

    Map<String, String> params = new HashMap<>();
    private String action;
    private JSONObject data;

    public YourlsAction(String action) {
        this.action = action;
        addParam(YourlsRequest.PARAM_ACTION, action);
    }

    public void setResult(JSONObject result) throws JSONException {
        this.data = result.getJSONObject(action);
        performData(data);
    }

    public JSONObject getData() {
        return data;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public abstract void performData(JSONObject data) throws JSONException;

}

package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by mate on 19.10.2015.
 */
public class Delete extends YourlsAction {

    private String shorturl;

    public Delete(String shorturl) throws UnsupportedEncodingException {
        super("delete");
        addParam(YourlsAction.PARAM_SHORTURL, shorturl);
        this.shorturl = shorturl;
    }

    @Override
    public void performResultData(JSONObject data) throws JSONException {

    }

    public String getShorturl() {
        return shorturl;
    }
}

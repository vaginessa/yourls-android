package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Mate on 26.09.2015.
 */
public class ShortUrl extends YourlsAction {

    String keyword;
    String url;
    String title;
    String date;
    String ip;
    String shorturl;
    long clicks;

    public ShortUrl(String url) throws UnsupportedEncodingException {
        super("shorturl");
        addParam(YourlsAction.PARAM_URL, URLEncoder.encode(url, YourlsAction.CHARSET));
    }

    public void setKeyword(String keyword) throws UnsupportedEncodingException {
        addParam(YourlsAction.PARAM_KEYWORD, URLEncoder.encode(keyword, YourlsAction.CHARSET));
    }

    public void setTitle(String title) throws UnsupportedEncodingException {
        addParam(YourlsAction.PARAM_TITLE, URLEncoder.encode(title, YourlsAction.CHARSET));
    }


    @Override
    public void performResultData(JSONObject data) throws JSONException {
        shorturl = getJsonString(data,"shorturl");
        if (data.has("url")) {
            JSONObject details = data.getJSONObject("url");
            url = getJsonString(details,"url");
            title = getJsonString(details, "title");
            date = getJsonString(details, "date");
            ip = getJsonString(details, "ip");
            keyword = getJsonString(details, "keyword");
            if (details.has("clicks"))
                clicks = getJsonLong(details,"clicks");
        }
    }

    public String getKeyword() {
        return keyword;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getIp() {
        return ip;
    }

    public long getClicks() {
        return clicks;
    }

    public String getShorturl() {
        return shorturl;
    }
}

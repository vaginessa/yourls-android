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
        addParam(YourlsAction.PARAM_TITLE, URLEncoder.encode(keyword, YourlsAction.CHARSET));
    }


    @Override
    public void performResultData(JSONObject data) throws JSONException {
        shorturl = data.getString("shorturl");
        JSONObject details = data.getJSONObject("url");
        url = details.getString("url");
        title = details.getString("title");
        date = details.getString("date");
        ip = details.getString("ip");
        keyword = details.getString("keyword");
        if (data.has("clicks"))
            clicks = data.getLong("clicks");
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

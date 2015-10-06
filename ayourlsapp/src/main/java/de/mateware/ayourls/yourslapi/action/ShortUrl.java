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
    long clicks;

    public ShortUrl(String url) throws UnsupportedEncodingException {
        super("shorturl", "url");
        addParam(YourlsAction.PARAM_URL, URLEncoder.encode(url, YourlsAction.CHARSET));
    }

    public void setKeyword(String keyword) throws UnsupportedEncodingException {
        addParam(YourlsAction.PARAM_KEYWORD, URLEncoder.encode(keyword, YourlsAction.CHARSET));
    }

    public void setTitle(String title) throws UnsupportedEncodingException {
        addParam(YourlsAction.PARAM_TITLE, URLEncoder.encode(keyword, YourlsAction.CHARSET));
    }


    @Override
    public void performData(JSONObject data) throws JSONException {
        url = data.getString("url");
        title = data.getString("title");
        date = data.getString("date");
        ip = data.getString("ip");
        keyword = data.getString("keyword");
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
}

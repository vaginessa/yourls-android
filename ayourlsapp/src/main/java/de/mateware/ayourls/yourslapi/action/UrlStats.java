package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mate on 26.09.2015.
 */
public class UrlStats extends YourlsAction {

    String keyword;
    String url;
    String title;
    String date;
    String ip;
    String shorturl;
    long clicks;

    public UrlStats(String keyword)  {
        super("url-stats");
        this.keyword = keyword;
        addParam(YourlsAction.PARAM_SHORTURL, keyword);
    }


    @Override
    public void performResultData(JSONObject data) throws JSONException {

        JSONObject details = data.getJSONObject("link");
        shorturl = details.getString("shorturl");
        url = details.getString("url");
        title = details.getString("title");
        date = details.getString("timestamp");
        ip = details.getString("ip");
        clicks = Long.valueOf(details.getString("clicks"));
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

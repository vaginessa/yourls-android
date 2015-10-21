package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.mateware.ayourls.model.Link;

/**
 * Created by Mate on 21.10.2015.
 */
public class Stats extends YourlsAction {

    private long totalLinks;
    private long totalClicks;
    private List<Link> links;

    public Stats(int start, int limit) {
        super("stats");
        addParam(PARAM_FILTER, "last");
        addParam(PARAM_START, String.valueOf(start));
        addParam(PARAM_LIMIT, String.valueOf(limit));
    }

    @Override
    public void performResultData(JSONObject data) throws JSONException {
        if (data.has("stats")) {
            JSONObject statsObject = data.getJSONObject("stats");
            totalLinks = statsObject.getLong("total_links");
            totalClicks = statsObject.getLong("total_clicks");
        }
        if (data.has("links")) {
            JSONObject linksJson = data.getJSONObject("links");
            if (linksJson.length() > 0) {
                links = new ArrayList<>(linksJson.length());
                for (Iterator<String> i = linksJson.keys(); i.hasNext(); ) {
                    String key = i.next();
                    JSONObject linkJson = linksJson.getJSONObject(key);
                    Link link = new Link();
                    link.setShorturl(linkJson.getString("shorturl"));
                    link.setUrl(linkJson.getString("url"));
                    link.setTitle(linkJson.getString("title"));
                    link.setDate(linkJson.getString("timestamp"));
                    link.setIp(linkJson.getString("ip"));
                    link.setClicks(linkJson.getLong("clicks"));
                    links.add(link);
                }
            }
        }
    }

    public long getTotalLinks() {
        return totalLinks;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public List<Link> getLinks() {
        return links;
    }
}

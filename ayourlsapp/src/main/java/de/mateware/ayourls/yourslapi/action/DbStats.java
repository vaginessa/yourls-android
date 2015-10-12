package de.mateware.ayourls.yourslapi.action;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mate on 26.09.2015.
 */
public class DbStats extends YourlsAction {
    private long totalLinks;
    private long totalClicks;

    public DbStats() {
        super("db-stats");
    }

    @Override
    public void performResultData(JSONObject data) throws JSONException {
        JSONObject details = data.getJSONObject("db-stats");
        totalLinks = details.getLong("total_links");
        totalClicks = details.getLong("total_clicks");
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public long getTotalLinks() {
        return totalLinks;
    }
}

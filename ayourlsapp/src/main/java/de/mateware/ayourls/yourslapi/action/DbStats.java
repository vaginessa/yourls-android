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
        super("db-stats", "db-stats");
    }

    @Override
    public void performData(JSONObject data) throws JSONException {
        totalLinks = data.getLong("total_links");
        totalClicks = data.getLong("total_clicks");
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public long getTotalLinks() {
        return totalLinks;
    }
}

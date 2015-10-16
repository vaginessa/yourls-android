package de.mateware.ayourls.linkdetail;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.network.NetworkHelper;
import de.mateware.ayourls.yourslapi.Volley;
import de.mateware.ayourls.yourslapi.YourlsError;
import de.mateware.ayourls.yourslapi.YourlsRequest;
import de.mateware.ayourls.yourslapi.action.UrlStats;

/**
 * Created by mate on 16.10.2015.
 */
public class LinkDetailWorkerFragment extends Fragment {

    private static Logger log = LoggerFactory.getLogger(LinkDetailWorkerFragment.class);

    private static final String TAG_WORKER_FRAGMENT = "linkdetail_worker_fragment";
    private LinkDetailWorkerCallback callback;

    public LinkDetailWorkerFragment() {
    }

    public static LinkDetailWorkerFragment findOrCreateFragment(FragmentManager fm, LinkDetailWorkerCallback callback) {
        LinkDetailWorkerFragment fragment = (LinkDetailWorkerFragment) fm.findFragmentByTag(TAG_WORKER_FRAGMENT);
        if (fragment == null) {
            fragment = new LinkDetailWorkerFragment();
            fm.beginTransaction()
              .add(fragment, TAG_WORKER_FRAGMENT)
              .commit();
        }
        fragment.callback = callback;
        return fragment;
    }

    public void refreshLinkData(String keyword) {
        if (NetworkHelper.isConnected(getContext())) {
            YourlsRequest<UrlStats> request = new YourlsRequest<>(getContext(), new UrlStats(keyword), new Response.Listener<UrlStats>() {
                @Override
                public void onResponse(UrlStats response) {
                    log.info(response.toString());

                    Cursor cursor = getContext().getContentResolver()
                                                .query(Link.getContentUri(), null, Link.Columns.KEYWORD + " LIKE '" + response.getKeyword() + "'", null, null);
                    if (cursor != null) {
                        try {
                            while (cursor.moveToNext()) {
                                Link link = new Link();
                                link.load(cursor);
                                link.load(response);
                                getContext().getContentResolver()
                                            .update(Link.getContentUri(link.getId()), link.getContentValues(), null, null);
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                    callback.onRefreshFinished();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    log.info(error.toString());
                    callback.onError(new YourlsError(error));
                    callback.onRefreshFinished();
                }
            });
            Volley.getInstance(getContext())
                  .addToRequestQueue(request);
        } else {
            callback.onError(new YourlsError(new VolleyError(getString(R.string.dialog_error_no_connection_message))));
        }
        callback.onRefreshFinished();
    }

    public interface LinkDetailWorkerCallback {
        public void onRefreshFinished();
        public void onError(YourlsError error);
    }


}

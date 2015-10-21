package de.mateware.ayourls.imports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.network.NetworkHelper;
import de.mateware.ayourls.yourslapi.Volley;
import de.mateware.ayourls.yourslapi.YourlsError;
import de.mateware.ayourls.yourslapi.YourlsRequest;
import de.mateware.ayourls.yourslapi.action.DbStats;

/**
 * Created by mate on 21.10.2015.
 */
public class ImportWorkerFragment extends Fragment {

    private static Logger log = LoggerFactory.getLogger(ImportWorkerFragment.class);

    private static final String TAG_WORKER_FRAGMENT = "import_worker_fragment";
    private ImportWorkerCallback callback;

    private static long totalLinksOnServer = 0;

    public ImportWorkerFragment() {
    }

    public static ImportWorkerFragment findOrCreateFragment(FragmentManager fm, ImportWorkerCallback callback) {
        ImportWorkerFragment fragment = (ImportWorkerFragment) fm.findFragmentByTag(TAG_WORKER_FRAGMENT);
        if (fragment == null) {
            fragment = new ImportWorkerFragment();
            fm.beginTransaction()
              .add(fragment, TAG_WORKER_FRAGMENT)
              .commit();
        }
        fragment.callback = callback;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void callDbStats() {
        if (NetworkHelper.isConnected(getContext())) {
            YourlsRequest<DbStats> request = new YourlsRequest<>(getContext(), new DbStats(), new Response.Listener<DbStats>() {
                @Override
                public void onResponse(DbStats response) {
                    log.info(response.toString());
                    totalLinksOnServer = response.getTotalLinks();

                    //callback.onServerCheckSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    log.info(error.toString());
                    //callback.onServerCheckFail(new YourlsError(error));
                }
            });
            Volley.getInstance(getContext())
                  .addToRequestQueue(request);
        } else {
            callback.onNetworkError(new YourlsError(new VolleyError(getString(R.string.dialog_error_no_connection_message))));
        }
    }

    public static long getTotalLinksOnServer() {
        return totalLinksOnServer;
    }

    public interface ImportWorkerCallback {
        void onNetworkError(YourlsError error);
        void showWaitDialog();
        void hideWaitDialog();
    }




}

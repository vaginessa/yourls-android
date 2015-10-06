package de.mateware.ayourls.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.NetworkHelper;
import de.mateware.ayourls.R;
import de.mateware.ayourls.yourslapi.Volley;
import de.mateware.ayourls.yourslapi.YourlsError;
import de.mateware.ayourls.yourslapi.YourlsRequest;
import de.mateware.ayourls.yourslapi.action.DbStats;
import de.mateware.ayourls.yourslapi.action.YourlsAction;

/**
 * Created by Mate on 25.09.2015.
 */
public class SettingsWorkerFragment extends Fragment {

    private static Logger log = LoggerFactory.getLogger(SettingsWorkerFragment.class);

    private static final String TAG_WORKER_FRAGMENT = "task_fragment";
    private SettingsWorkerCallback callback;

    public SettingsWorkerFragment() {
    }

    public static SettingsWorkerFragment findOrCreateFragment(FragmentManager fm, SettingsWorkerCallback callback) {
        SettingsWorkerFragment fragment = (SettingsWorkerFragment) fm.findFragmentByTag(TAG_WORKER_FRAGMENT);
        if (fragment == null) {
            fragment = new SettingsWorkerFragment();
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

    public void checkServer() {
        if (NetworkHelper.isConnected(getContext())) {
            YourlsRequest request = new YourlsRequest(getContext(), new DbStats(), new Response.Listener<YourlsAction>() {
                @Override
                public void onResponse(YourlsAction response) {

                    log.info(response.toString());
                    callback.onServerCheckSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    log.info(error.toString());
                    callback.onServerCheckFail(new YourlsError(error));
                }
            });
            Volley.getInstance(getContext())
                  .addToRequestQueue(request);
        } else {
            callback.onServerCheckFail(new YourlsError(new VolleyError(getString(R.string.dialog_error_no_connection_message))));
        }
    }


    public interface SettingsWorkerCallback {
        void onServerCheckSuccess(YourlsAction yourlsAction);

        void onServerCheckFail(YourlsError error);
    }

}

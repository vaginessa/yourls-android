package de.mateware.ayourls.service;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.mateware.ayourls.R;
import de.mateware.ayourls.dialog.DialogActivty;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.yourslapi.Volley;
import de.mateware.ayourls.yourslapi.YourlsRequest;
import de.mateware.ayourls.yourslapi.action.Delete;

/**
 * Created by mate on 19.10.2015.
 */
public class DeleteService extends IntentService {

    public static final String EXTRA_ID = "idExtra";
    public static final String EXTRA_CONFIRMED = "confirmExtra";
    public static final String EXTRA_DELETE_ON_SERVER = "deleteOnServerExtra";

    Logger log = LoggerFactory.getLogger(DeleteService.class);

    public DeleteService() {
        super(DeleteService.class.getSimpleName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        log.debug(intent.toString());
        long linkId = intent.getLongExtra(EXTRA_ID, -1);
        if (linkId > -1) {
            Link link = new Link();
            link.load(this, linkId);
            boolean confirmed = intent.getBooleanExtra(EXTRA_CONFIRMED, false);
            if (confirmed) {

                boolean deleteOnServer = intent.getBooleanExtra(EXTRA_DELETE_ON_SERVER, false);
                try {

                    if (deleteOnServer) {
                        try {
                            Delete deleteAction = new Delete(link.getShorturl());
                            RequestFuture<Delete> future = RequestFuture.newFuture();
                            YourlsRequest<Delete> request = new YourlsRequest<>(this, deleteAction, future, future);
                            Volley.getInstance(this)
                                  .addToRequestQueue(request);
                            deleteAction = future.get(20, TimeUnit.SECONDS);
                        } catch (UnsupportedEncodingException | InterruptedException | TimeoutException | ExecutionException e) {
                            if (e.getCause() != null && e.getCause()
                                                         .getMessage() != null)
                                try {
                                    JSONObject jsonObject = new JSONObject(e.getCause()
                                                                            .getMessage());
                                    int errorCode = jsonObject.getInt("errorCode");
                                    if (errorCode == 400) {
                                        //Asume that delete api is not installed
                                        throw new VolleyError(getString(R.string.dialog_error_delete_api_missing));
                                    }
                                } catch (JSONException e1) {
                                    throw new VolleyError(e.getCause()
                                                           .getMessage());
                                }

                            else if (e.getCause() != null)
                                throw new VolleyError(e.getCause());
                            else throw new VolleyError(e);

                        }
                    }

                    if (!link.delete(this)) {
                        throw new VolleyError("Cannot delete in database");
                    }
                } catch (VolleyError e) {
                    Intent errorIntent = new Intent(this, DialogActivty.class);
                    errorIntent.putExtra(DialogActivty.EXTRA_DIALOG, DialogActivty.DIALOG_ERROR);
                    errorIntent.putExtra(DialogActivty.EXTRA_MESSAGE, e.getMessage() != null ? e.getMessage() : e.getCause()
                                                                                                                 .getClass()
                                                                                                                 .getSimpleName());
                    errorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(errorIntent);
                }
            } else {
                Intent confirmIntent = new Intent(this, DialogActivty.class);
                confirmIntent.putExtra(DialogActivty.EXTRA_DIALOG, DialogActivty.DIALOG_DELETE_CONFIRM);
                confirmIntent.putExtra(EXTRA_ID, linkId);
                confirmIntent.putExtra(DialogActivty.EXTRA_MESSAGE, getString(R.string.dialog_confirm_delete_message));
                confirmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(confirmIntent);
            }
        } else
            throw new IllegalArgumentException("Service have to be called with extra '" + EXTRA_ID + "'");
    }
}

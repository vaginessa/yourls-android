package de.mateware.ayourls.yourslapi;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mate on 06.10.2015.
 */
public class YourlsError {

    private static final Logger log = LoggerFactory.getLogger(YourlsError.class);

    private static final String JSON_MESSAGE = "message";
    private static final String JSON_ERROR_CODE = "errorCode";

    private VolleyError error;
    private String message;
    private int errorCode = -1;

    public YourlsError(VolleyError error) {
        this.error = error;
        if (error.networkResponse != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(new String(error.networkResponse.data));
                message = jsonObject.getString(JSON_MESSAGE);
                errorCode = jsonObject.getInt(JSON_ERROR_CODE);
            } catch (JSONException e) {
                log.warn("Error parsing json, catch HttpStatus");
                HttpStatus httpStatus = HttpStatus.getByCode(error.networkResponse.statusCode);
                message = httpStatus.getName() + ": " + httpStatus.getDescription();
                errorCode = httpStatus.getCode();
            }
        } else {
            if (error.getMessage() != null)
                message = error.getMessage();
            else
                message = error.getClass().getSimpleName();
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}

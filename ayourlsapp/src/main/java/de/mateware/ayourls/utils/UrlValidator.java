package de.mateware.ayourls.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by mate on 20.10.2015.
 */
public class UrlValidator {

    public static String getValidUrl(@NonNull String text, boolean allowLocal) throws NoValidUrlExpception {
        org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(new String[]{"http", "https"}, org.apache.commons.validator.routines.UrlValidator.ALLOW_2_SLASHES + (allowLocal ? org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS : 0L));
        if (!TextUtils.isEmpty(text)) {
            if (text.length() >= 4 && !(text.substring(0, 7)
                                            .toLowerCase(Locale.getDefault())
                                            .equals("http://") || text.substring(0, 8)
                                                                      .toLowerCase(Locale.getDefault())
                                                                      .equals("https://"))) {
                text = "http://" + text;
            }
            if (urlValidator.isValid(text)) return text;
        }
        throw new NoValidUrlExpception("Not a valid url");
    }

    public static class NoValidUrlExpception extends Exception {
        public NoValidUrlExpception(String detailMessage) {
            super(detailMessage);
        }
    }

}

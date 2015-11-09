package de.mateware.ayourls.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mate on 20.10.2015.
 */
public class UrlValidator {

    private static Logger log = LoggerFactory.getLogger(UrlValidator.class);

    private static final String REGEX_NONLOCAL = "^\\b(?:(?:\\w+)://)(?:\\S+(?::\\S*)?@)?(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)(?:\\.(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)*(?:\\.(?:[a-z\u00a1-\uffff]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?\\b";
    private static final String REGEX_LOCAL = "^\\b(?:(?:\\w+)://)(?:\\S+(?::\\S*)?@)?(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)(?:\\.(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)*)(?::\\d{2,5})?(?:/[^\\s]*)?\\b";

    public static String getValidUrl(@NonNull String text, boolean allowLocal) throws NoValidUrlExpception {
        if (!TextUtils.isEmpty(text)) {
            log.debug("validating url");
            text = text.trim();

            Pattern pattern;
            if (allowLocal) pattern = Pattern.compile(REGEX_LOCAL, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            else pattern = Pattern.compile(REGEX_NONLOCAL, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                return matcher.group();
            } else
            {
                if (!text.toLowerCase(Locale.getDefault()).startsWith("http://")) {
                    text = "http://" + text;
                    try {
                        return getValidUrl(text, allowLocal);
                    } catch (NoValidUrlExpception ignored) {
                    }
                }
            }
        } throw new NoValidUrlExpception("Not a valid url");
    }

    public static class NoValidUrlExpception extends Exception {
        public NoValidUrlExpception(String detailMessage) {
            super(detailMessage);
        }
    }

}

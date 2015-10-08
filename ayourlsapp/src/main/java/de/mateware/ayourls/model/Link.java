package de.mateware.ayourls.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import de.mateware.ayourls.provider.AyourlsProvider;
import de.mateware.ayourls.yourslapi.action.ShortUrl;

/**
 * Created by mate on 07.10.2015.
 */
public class Link {

    private static Logger log = LoggerFactory.getLogger(Link.class);

    public static String NAME = Link.class.getSimpleName()
                                          .toLowerCase(Locale.getDefault());
    public static String AUTHORITY = AyourlsProvider.AUTHORITY;

    private static Uri CONTENT_URI;
    public static String CONTENT_TYPE;
    public static String CONTENT_ITEM_TYPE;

    static {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content");
        builder.authority(AUTHORITY);
        builder.path(NAME.toLowerCase(Locale.getDefault()));
        CONTENT_URI = builder.build();
        CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + NAME.toLowerCase(Locale.getDefault());
        CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + NAME.toLowerCase(Locale.getDefault());
    }

    public static final class Columns implements BaseColumns {
        public static final String KEYWORD = "keyword";
        public static final String URL = "url";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String IP = "ip";
        public static final String CLICKS = "clicks";
    }

    public static Uri getContentUri() {
        return CONTENT_URI;
    }

    public static Uri getContentUri(@NonNull String keyword) {
        return getContentUri().buildUpon()
                              .appendPath(keyword)
                              .build();
    }

    private String keyword;
    private String url;
    private String title;
    private String date;
    private String ip;
    private long clicks;

    public Link() {
    }

    public boolean load(@NonNull Context context, @NonNull String keyword) {
        setKeyword(keyword);
        Cursor cursor = context.getApplicationContext()
                               .getContentResolver()
                               .query(Link.getContentUri(keyword), null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) load(cursor);
                return true;
            } finally {
                cursor.close();
            }
        }
        return false;
    }

    public void load(@NonNull Cursor cursor) {
        setKeyword(cursor.getString(cursor.getColumnIndex(Columns.KEYWORD)));
        setUrl(cursor.getString(cursor.getColumnIndex(Columns.URL)));
        setTitle(cursor.getString(cursor.getColumnIndex(Columns.TITLE)));
        setDate(cursor.getString(cursor.getColumnIndex(Columns.DATE)));
        setIp(cursor.getString(cursor.getColumnIndex(Columns.IP)));
        setClicks(cursor.getLong(cursor.getColumnIndex(Columns.CLICKS)));
    }

    public void load(@NonNull ShortUrl action) {
        setKeyword(action.getKeyword());
        setDate(action.getDate());
        setTitle(action.getTitle());
        setUrl(action.getUrl());
        setIp(action.getIp());
        setClicks(action.getClicks());
    }

    public void save(@NonNull Context context) {
        if (TextUtils.isEmpty(getKeyword()))
            throw new IllegalStateException("Cannot save without keyword");
        Cursor cursor = context.getContentResolver()
                               .query(Link.getContentUri(getKeyword()), null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    context.getContentResolver()
                           .update(Link.getContentUri(getKeyword()), getContentValues(), null, null);
                } else {
                    context.getContentResolver()
                           .insert(Link.getContentUri(), getContentValues());
                }
            } finally {
                cursor.close();
            }
        }
    }

    public void delete(@NonNull Context context) {
        if (TextUtils.isEmpty(getKeyword()))
            throw new IllegalStateException("Cannot delete without keyword");
        context.getContentResolver().delete(Link.getContentUri(getKeyword()),null,null);
    }


    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Columns.KEYWORD, keyword);
        cv.put(Columns.URL, url);
        cv.put(Columns.TITLE, title);
        cv.put(Columns.DATE, date);
        cv.put(Columns.IP, ip);
        cv.put(Columns.CLICKS, clicks);
        return cv;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }
}

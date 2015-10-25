package de.mateware.ayourls.model;

import android.content.ContentUris;
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
import de.mateware.ayourls.yourslapi.action.UrlStats;

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
        public static final String SHORTURL = "shorturl";
    }

    public static Uri getContentUri() {
        return CONTENT_URI;
    }

    public static Uri getContentUri(@NonNull long id) {
        return ContentUris.withAppendedId(getContentUri(), id);
    }

    private long id = -1;
    private String keyword;
    private String url;
    private String title;
    private String date;
    private String ip;
    private String shorturl;
    private long clicks;

    public Link() {
    }

    public boolean load(@NonNull Context context, @NonNull long id) {
        Cursor cursor = context.getApplicationContext()
                               .getContentResolver()
                               .query(Link.getContentUri(id), null, null, null, null);
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
        setId(cursor.getLong(cursor.getColumnIndex(Columns._ID)));
        setKeyword(cursor.getString(cursor.getColumnIndex(Columns.KEYWORD)));
        setUrl(cursor.getString(cursor.getColumnIndex(Columns.URL)));
        setTitle(cursor.getString(cursor.getColumnIndex(Columns.TITLE)));
        setDate(cursor.getString(cursor.getColumnIndex(Columns.DATE)));
        setIp(cursor.getString(cursor.getColumnIndex(Columns.IP)));
        setClicks(cursor.getLong(cursor.getColumnIndex(Columns.CLICKS)));
        setShorturl(cursor.getString(cursor.getColumnIndex(Columns.SHORTURL)));
    }

    public void load(@NonNull ShortUrl action) {
        setKeyword(action.getKeyword());
        setDate(action.getDate());
        setTitle(action.getTitle());
        setUrl(action.getUrl());
        setIp(action.getIp());
        setClicks(action.getClicks());
        setShorturl(action.getShorturl());
    }

    public void load(@NonNull UrlStats action) {
        setKeyword(action.getKeyword());
        setDate(action.getDate());
        setTitle(action.getTitle());
        setUrl(action.getUrl());
        setIp(action.getIp());
        setClicks(action.getClicks());
        setShorturl(action.getShorturl());
    }

    public enum SaveResult {INSERTED, UPDATED, ERROR}

    public SaveResult save(@NonNull Context context) {
        if (TextUtils.isEmpty(getUrl()) || TextUtils.isEmpty(getKeyword())) {
            log.warn("cannot save link because to less info");
            return SaveResult.ERROR;
        }
        Cursor cursor = null;
        if (getId() != -1) {
            cursor = context.getContentResolver()
                            .query(Link.getContentUri(getId()), null, null, null, null);
        } else {
            cursor = context.getContentResolver()
                            .query(Link.getContentUri(), null, Columns.SHORTURL + " LIKE '" + getShorturl() + "'", null, null);
        }
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    setId(cursor.getLong(cursor.getColumnIndex(Columns._ID)));
                    if (context.getContentResolver()
                               .update(Link.getContentUri(getId()), getContentValues(), null, null) > 0)
                        return SaveResult.UPDATED;
                    return SaveResult.ERROR;
                }
            } finally {
                cursor.close();
            }
        }
        setId(ContentUris.parseId(context.getContentResolver()
                                         .insert(Link.getContentUri(), getContentValues())));
        return SaveResult.INSERTED;
    }

    public boolean delete(@NonNull Context context) {
        if (getId() != -1) {
            if (context.getContentResolver()
                       .delete(Link.getContentUri(getId()), null, null) > 0) {
                return true;
            }
        }
        return false;
    }


    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Columns.KEYWORD, getKeyword());
        cv.put(Columns.URL, getUrl());
        cv.put(Columns.TITLE, getTitle());
        cv.put(Columns.DATE, getDate());
        cv.put(Columns.IP, getIp());
        cv.put(Columns.SHORTURL, getShorturl());
        cv.put(Columns.CLICKS, getClicks());
        cv.put(Columns._ID, getId());
        return cv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public String getKeyword() {
        if (keyword == null && getShorturl() != null) {
            setKeyword(Uri.parse(getShorturl())
                          .getLastPathSegment());
        }
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

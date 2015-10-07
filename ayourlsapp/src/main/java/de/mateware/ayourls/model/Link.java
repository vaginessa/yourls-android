package de.mateware.ayourls.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import de.mateware.ayourls.provider.AyourlsProvider;

/**
 * Created by mate on 07.10.2015.
 */
public class Link {

    private static Logger log = LoggerFactory.getLogger(Link.class);

    public static String NAME = Link.class.getSimpleName();
    public static String AUTHORITY = AyourlsProvider.AUTHORITY;

    public static Uri CONTENT_URI;
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

    private String keyword;
    private String url;
    private String title;
    private String date;
    private String ip;
    private long clicks;

    public Link(Cursor cursor) {
        keyword = cursor.getString(cursor.getColumnIndex(Columns.KEYWORD));
        url = cursor.getString(cursor.getColumnIndex(Columns.URL));
        title = cursor.getString(cursor.getColumnIndex(Columns.TITLE));
        date = cursor.getString(cursor.getColumnIndex(Columns.DATE));
        ip = cursor.getString(cursor.getColumnIndex(Columns.IP));
        clicks = cursor.getLong(cursor.getColumnIndex(Columns.CLICKS));
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Columns.KEYWORD,keyword);
        cv.put(Columns.URL,url);
        cv.put(Columns.TITLE,title);
        cv.put(Columns.DATE,date);
        cv.put(Columns.IP,ip);
        cv.put(Columns.CLICKS,clicks);
        return cv;
    }


}

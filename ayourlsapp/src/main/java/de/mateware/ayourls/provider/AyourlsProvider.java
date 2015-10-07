package de.mateware.ayourls.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 07.10.2015.
 */
public class AyourlsProvider extends ContentProvider {

    public static final String AUTHORITY = "de.mateware.ayourls.provider";

    private static final int LINK_DIR = 1;
    private static final int LINK_KEYWORD = 2;

    private SQLiteDatabase database;

    private static UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, Link.NAME, LINK_DIR);
        sUriMatcher.addURI(AUTHORITY, Link.NAME + "/*", LINK_KEYWORD);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                return Link.CONTENT_TYPE;
            case LINK_KEYWORD:
                return Link.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown provider type: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

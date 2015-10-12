package de.mateware.ayourls.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 07.10.2015.
 */
@SuppressWarnings("ConstantConditions")
public class AyourlsProvider extends ContentProvider {

    private static final Logger log = LoggerFactory.getLogger(AyourlsProvider.class);

    public static final String AUTHORITY = "de.mateware.ayourls.provider";

    private static final int LINK_DIR = 1;
    private static final int LINK_ID = 2;

    private SQLiteDatabase database;

    private static UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, Link.NAME, LINK_DIR);
        sUriMatcher.addURI(AUTHORITY, Link.NAME + "/#", LINK_ID);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getWritableDatabase();
        return true;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                return Link.CONTENT_TYPE;
            case LINK_ID:
                return Link.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown provider type: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        log.debug("query: {} {} {} {} {}", uri, projection, selection, selectionArgs, sortOrder);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                queryBuilder.setTables(Link.NAME);
                break;
            case LINK_ID:
                queryBuilder.setTables(Link.NAME);
                long id = ContentUris.parseId(uri);
                queryBuilder.appendWhere(Link.Columns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        log.debug("insert: {} {}", uri, values);
        long rowId;
        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                values.remove(Link.Columns._ID);
                rowId = database.insert(Link.NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: " + uri);
        }
        if (rowId > -1) {
            Uri contentUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver()
                        .notifyChange(contentUri, null);
            return contentUri;
        }
        throw new SQLException("Could not insert in " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        log.debug("delete: {} {} {}", uri, selection, selectionArgs);
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                //Delete all Links from table
                count = database.delete(Link.NAME, selection, selectionArgs);
                break;
            case LINK_ID:
                long id = ContentUris.parseId(uri);
                count = database.delete(Link.NAME, Link.Columns._ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: " + uri);
        }
        if (count > 0)
            getContext().getContentResolver()
                        .notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        log.debug("update: {} {} {} {}", uri, values, selection, selectionArgs);
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                count = database.update(Link.NAME, values, selection, selectionArgs);
                break;
            case LINK_ID:
                long id = ContentUris.parseId(uri);
                count = database.update(Link.NAME, values, Link.Columns._ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: " + uri);
        }
        if (count > 0)
            getContext().getContentResolver()
                        .notifyChange(uri, null);

        return count;
    }
}

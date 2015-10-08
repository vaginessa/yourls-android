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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                queryBuilder.setTables(Link.NAME);
                break;
            case LINK_KEYWORD:
                queryBuilder.setTables(Link.NAME);
                queryBuilder.appendWhere(Link.Columns.KEYWORD + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = queryBuilder.query(database,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowId = -1;
        switch (sUriMatcher.match(uri)) {
            case LINK_DIR:
                rowId = database.insert(Link.NAME,null,values);
                if (rowId > -1) {
                    Uri contentUri = uri.buildUpon().appendPath(values.getAsString(Link.Columns.KEYWORD)).build();
                    getContext().getContentResolver().notifyChange(contentUri, null);
                    return contentUri;
                }
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: " + uri);
        }
        if (rowId > -1)
        {
            Uri contentUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(contentUri, null);
            return contentUri;
        }
        throw new SQLException("Could not insert in " + uri);
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

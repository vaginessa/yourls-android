package de.mateware.ayourls.data;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;

/**
 * Created by mate on 01.10.2015.
 */
@ContentProvider(authority = AYDatabase.AUTHORITY, databaseName = AYDatabase.NAME, baseContentUri = AYDatabase.BASE_CONTENT_URI)
@Database(name = AYDatabase.NAME, version = AYDatabase.VERSION)
public class AYDatabase {
    public static final String NAME = "aYourls";
    public static final int VERSION = 1;
    public static final String AUTHORITY = "de.mateware.ayourls.provider";
    public static final String BASE_CONTENT_URI = "content://";
}

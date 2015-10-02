package de.mateware.ayourls.data;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;

/**
 * Created by mate on 01.10.2015.
 */
@ContentProvider(authority = AyourlsDatabase.AUTHORITY, databaseName = AyourlsDatabase.NAME, baseContentUri = AyourlsDatabase.BASE_CONTENT_URI)
@Database(name = AyourlsDatabase.NAME, version = AyourlsDatabase.VERSION)
public class AyourlsDatabase {
    public static final String NAME = "aYourls";
    public static final int VERSION = 1;
    public static final String AUTHORITY = "de.mateware.ayourls.provider";
    public static final String BASE_CONTENT_URI = "content://";
}

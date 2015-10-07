package de.mateware.ayourls.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 07.10.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final String DATABASE_NAME = "ayourls";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        log.debug("Creating database");
        db.execSQL(CREATE_LINK_V1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
//            case 1:
//                upgradeFrom1To2(db);
//                if (newVersion == 2) break;
//            case 2:
//                upgradeFrom2To3(db);
//                if (newVersion == 3) break;
            default:
                break;
        }
    }

    private static final String CREATE_LINK_V1 = "CREATE TABLE " + Link.NAME + " (" +
            Link.Columns.KEYWORD + " TEXT PRIMARY KEY," +
            Link.Columns.URL + " TEXT," +
            Link.Columns.TITLE + " TEXT," +
            Link.Columns.DATE + " TEXT," +
            Link.Columns.IP + " TEXT," +
            Link.Columns.CLICKS + " INTEGER" +
            ");";

}

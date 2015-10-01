package de.mateware.ayourls.data;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;
import com.raizlabs.android.dbflow.structure.provider.BaseSyncableProviderModel;
import com.raizlabs.android.dbflow.structure.provider.ContentUtils;

/**
 * Created by mate on 01.10.2015.
 */
@TableEndpoint(name = LinkModel.NAME, contentProviderName = "AYDatabase")
@Table(databaseName = AYDatabase.NAME)
public class LinkModel extends BaseSyncableProviderModel<LinkModel> {

    public static final String NAME = "LinkModel";

    @ContentUri(path = NAME, type = ContentUri.ContentType.VND_MULTIPLE + NAME)
    public static final Uri CONTENT_URI = ContentUtils.buildUri(AYDatabase.AUTHORITY);

    @Column
    @PrimaryKey
    String keyword;

    @Column
    String url;

    @Column
    String title;

    @Column
    String date;

    @Column
    String ip;

    @Column
    long clicks;

    @Override
    public Uri getDeleteUri() {
        return CONTENT_URI;
    }

    @Override
    public Uri getInsertUri() {
        return CONTENT_URI;
    }

    @Override
    public Uri getUpdateUri() {
        return CONTENT_URI;
    }

    @Override
    public Uri getQueryUri() {
        return CONTENT_URI;
    }


}

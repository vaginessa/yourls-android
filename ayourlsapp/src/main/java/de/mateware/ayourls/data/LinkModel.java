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
@TableEndpoint(name = LinkModel.NAME, contentProviderName = "AyourlsDatabase")
@Table(databaseName = AyourlsDatabase.NAME)
public class LinkModel extends BaseSyncableProviderModel<LinkModel> {

    public static final String NAME = "LinkModel";

    @ContentUri(path = NAME, type = ContentUri.ContentType.VND_MULTIPLE + NAME)
    public static final Uri CONTENT_URI = ContentUtils.buildUri(AyourlsDatabase.AUTHORITY);

    @Column
    @PrimaryKey
    public String keyword;

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


    public String getKeyword() {
        return keyword;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getIp() {
        return ip;
    }

    public long getClicks() {
        return clicks;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }
}
